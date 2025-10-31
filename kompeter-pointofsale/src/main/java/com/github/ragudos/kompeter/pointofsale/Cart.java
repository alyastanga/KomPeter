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
    private AtomicReference<ArrayList<CartItem>> items = new AtomicReference<>(new ArrayList<>());
    private AtomicReference<ArrayList<Consumer<CartEvent>>> subscribers = new AtomicReference<>(new ArrayList<>());

    public record CartEvent(CartEventType eventType, CartItem payload, CartItem previousPayload) {
    }

    public void addItem(CartItem item) {
        items.getAcquire().add(item);

        notifySubscribers(new CartEvent(CartEventType.ADD_ITEM, item, null));
    }

    public void clearCart() {
        items.getAcquire().clear();

        notifySubscribers(new CartEvent(CartEventType.CLEAR, null, null));
    }

    public void decreaseItemQty(int _itemStockId, int qty) throws NegativeQuantityException {
        CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        CartItem prev = cartItem.clone();
        cartItem.decreaseQty(qty);

        notifySubscribers(new CartEvent(CartEventType.DECREASE_ITEM_QTY, cartItem, prev));
    }

    public void decrementItem(int _itemStockId) throws NegativeQuantityException {
        CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        CartItem prev = cartItem.clone();
        cartItem.decrement();

        notifySubscribers(new CartEvent(CartEventType.DECREMENT_ITEM, cartItem, prev));
    }

    public void destroy() {
        items.getAcquire().clear();
    }

    public boolean exists(int _itemStockId) {
        for (CartItem item : items.getAcquire()) {
            if (item._itemStockId() == _itemStockId) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<CartItem> getAllItems() {
        return items.getAcquire();
    }

    public CartItem getLast() {
        return items.getAcquire().getLast();
    }

    public void increaseItemQty(int _itemStockId, int qty) throws InsufficientStockException {
        CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        CartItem prev = cartItem.clone();
        cartItem.increaseQty(qty);

        notifySubscribers(new CartEvent(CartEventType.INCREASE_ITEM_QTY, cartItem, prev));
    }

    public void incrementItem(int _itemStockId) throws InsufficientStockException {
        CartItem cartItem = items.getAcquire().stream().filter((item) -> item._itemStockId() == _itemStockId)
                .findFirst().orElseThrow();

        CartItem prev = cartItem.clone();
        cartItem.increment();

        notifySubscribers(new CartEvent(CartEventType.INCREMENT_ITEM, cartItem, prev));
    }

    public boolean isEmpty() {
        return items.getAcquire().isEmpty();
    }

    @Override
    public void notifySubscribers(CartEvent value) {
        for (Consumer<CartEvent> acquire : subscribers.getAcquire()) {
            acquire.accept(value);
        }
    }

    public void removeItem(CartItem item) {
        items.getAcquire().remove(item);

        notifySubscribers(new CartEvent(CartEventType.REMOVE_ITEM, item, null));
    }

    public void removeItem(int id) {
        Optional<CartItem> item = items.getAcquire().stream().filter(i -> i._itemStockId() == id).findFirst();

        if (item.isEmpty()) {
            return;
        }

        items.getAcquire().remove(item.get());

        notifySubscribers(new CartEvent(CartEventType.REMOVE_ITEM, item.get(), null));
    }

    @Override
    public void subscribe(Consumer<CartEvent> subscriber) {
        subscribers.getAcquire().add(subscriber);
    }

    public BigDecimal totalPrice() {
        return items.getAcquire().stream().map((item) -> item.getTotalPrice()).filter(p -> p != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int totalQuantity() {
        return items.getAcquire().stream().mapToInt((item) -> item.qty()).sum();
    }

    @Override
    public void unsubscribe(Consumer<CartEvent> subscriber) {
        subscribers.getAcquire().remove(subscriber);
    }

    public enum CartEventType {
        ADD_ITEM, CLEAR, DECREASE_ITEM_QTY, DECREMENT_ITEM, INCREASE_ITEM_QTY, INCREMENT_ITEM, REMOVE_ITEM;
    }
}
