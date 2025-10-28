/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.github.ragudos.kompeter.utilities.observer.Observer;

public class Cart implements Observer<Void> {
    private ArrayList<CartItem> items = new ArrayList<>();
    private ArrayList<Consumer<Void>> subscribers = new ArrayList<>();

    public Cart() {
    }

    public int addItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);
            if (exist._itemStockId() == item._itemStockId()) {
                int newQty = exist.qty() + item.qty();
                items.set(i, new CartItem(exist._itemStockId(), exist.productName(), exist.category(), exist.brand(),
                        newQty, exist.price()));

                notifySubscribers(null);

                return newQty;
            }
        }

        items.add(item);
        notifySubscribers(null);
        return -1;
    }

    public void clearCart() {
        items.clear();
    }

    public void decItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);

            if (exist._itemStockId() == item._itemStockId()) {
                int newQty = exist.qty() - item.qty();

                if (newQty > 0) {
                    items.set(i, new CartItem(exist._itemStockId(), exist.productName(), exist.category(),
                            exist.brand(), newQty, exist.price()));
                } else {
                    items.remove(i);
                }
                notifySubscribers(null);

                break;
            }
        }
    }

    public void destroy() {
        items.clear();
        subscribers.clear();
    }

    public boolean exists(int _itemStockId) {
        for (CartItem item : items) {
            if (item._itemStockId() == _itemStockId) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<CartItem> getAllItems() {
        return items;
    }

    public CartItem getLast() {
        return items.getLast();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void notifySubscribers(Void value) {
        subscribers.forEach((cb) -> cb.accept(value));
    }

    public void removeItem(int _itemStockId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i)._itemStockId() == _itemStockId) {
                items.remove(i);
                notifySubscribers(null);

                break;
            }
        }
    }

    public void replaceItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);

            if (exist._itemStockId() == item._itemStockId()) {
                items.set(i, item);

                notifySubscribers(null);
                return;
            }
        }

        items.add(item);
        notifySubscribers(null);
    }

    @Override
    public void subscribe(Consumer<Void> subscriber) {
        subscribers.add(subscriber);
    }

    public double totalPrice() {
        return items.stream().mapToDouble((item) -> item.getTotalPrice()).sum();
    }

    public int totalQuantity() {
        return items.stream().mapToInt((item) -> item.qty()).sum();
    }

    @Override
    public void unsubscribe(Consumer<Void> subscriber) {
        subscribers.remove(subscriber);
    }
}
