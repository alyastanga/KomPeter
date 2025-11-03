/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.github.ragudos.kompeter.utilities.observer.Observer;

public class Cart implements Observer<com.github.ragudos.kompeter.pointofsale.Cart.CartEvent> {
    private final AtomicReference<ArrayList<CartItem>> items = new AtomicReference<>(new ArrayList<>());
    private final AtomicReference<ArrayList<Consumer<CartEvent>>> subscribers = new AtomicReference<>(
            new ArrayList<>());

    public record CartEvent(CartEventType eventType, CartItem payload, CartItem previousPayload) {
    }

    public void addItem(final CartItem item) {
        items.getAcquire().add(item);

        notifySubscribers(new CartEvent(CartEventType.ADD_ITEM, item, null));
    }

    public void clearCart() {
        items.getAcquire().clear();

        notifySubscribers(new CartEvent(CartEventType.CLEAR, null, null));
    }

    public void decreaseItemQty(final int _itemStockId, final int qty) throws NegativeQuantityException {
        final CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        final CartItem prev = cartItem.clone();
        cartItem.decreaseQty(qty);

        notifySubscribers(new CartEvent(CartEventType.DECREASE_ITEM_QTY, cartItem, prev));
    }

    public void decrementItem(final int _itemStockId) throws NegativeQuantityException {
        final CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        final CartItem prev = cartItem.clone();
        cartItem.decrement();

        notifySubscribers(new CartEvent(CartEventType.DECREMENT_ITEM, cartItem, prev));
    }

    public void destroy() {
        items.getAcquire().clear();
    }

    public boolean exists(final int _itemStockId) {
        for (final CartItem item : items.getAcquire()) {
            if (item._itemStockId() == _itemStockId) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<CartItem> getAllItems() {
        return items.getAcquire();
    }

    public Optional<CartItem> getItem(final int _itemStockId) {
        return items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId).findFirst();
    }

    public CartItem getLast() {
        return items.getAcquire().getLast();
    }

    public void increaseItemQty(final int _itemStockId, final int qty) throws InsufficientStockException {
        final CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        final CartItem prev = cartItem.clone();
        cartItem.increaseQty(qty);

        notifySubscribers(new CartEvent(CartEventType.INCREASE_ITEM_QTY, cartItem, prev));
    }

    public void incrementItem(final int _itemStockId) throws InsufficientStockException {
        final CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        final CartItem prev = cartItem.clone();
        cartItem.increment();

        notifySubscribers(new CartEvent(CartEventType.INCREMENT_ITEM, cartItem, prev));
    }

    public boolean isEmpty() {
        return items.getAcquire().isEmpty();
    }

    @Override
    public void notifySubscribers(final CartEvent value) {
        for (final Consumer<CartEvent> acquire : subscribers.get()) {
            acquire.accept(value);
        }
    }

    public void removeItem(final CartItem item) {
        items.getAcquire().remove(item);

        notifySubscribers(new CartEvent(CartEventType.REMOVE_ITEM, item, null));
    }

    public void removeItem(final int id) {
        final Optional<CartItem> item = items.getAcquire().stream().filter(i -> i._itemStockId() == id).findFirst();

        if (item.isEmpty()) {
            return;
        }

        items.getAcquire().remove(item.get());

        notifySubscribers(new CartEvent(CartEventType.REMOVE_ITEM, item.get(), null));
    }

    @Override
    public void subscribe(final Consumer<CartEvent> subscriber) {
        subscribers.get().add(subscriber);
    }

    public BigDecimal totalPrice() {
        return items.getAcquire().stream().map((item) -> item.getTotalPrice()).filter(p -> p != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int totalQuantity() {
        return items.getAcquire().stream().mapToInt((item) -> item.qty()).sum();
    }

    @Override
    public void unsubscribe(final Consumer<CartEvent> subscriber) {
        subscribers.get().remove(subscriber);
    }

    public enum CartEventType {
        ADD_ITEM, CLEAR, DECREASE_ITEM_QTY, DECREMENT_ITEM, INCREASE_ITEM_QTY, INCREMENT_ITEM, REMOVE_ITEM;
    }
}
