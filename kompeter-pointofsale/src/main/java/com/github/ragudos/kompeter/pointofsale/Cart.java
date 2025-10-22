/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
 */
package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.utilities.observer.Observer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Cart implements Observer<Void> {
    private ArrayList<CartItem> items = new ArrayList<>();
    private ArrayList<Consumer<Void>> subscribers = new ArrayList<>();

    private static Cart instance;

    public static synchronized Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }

        return instance;
    }

    public void addItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);
            if (exist._itemStockId() == item._itemStockId()) {
                int newQty = exist.qty() + item.qty();
                items.set(i, new CartItem(exist._itemStockId(), exist.productName(), exist.category(), exist.brand(),
                        newQty, exist.price()));

                notifySubscribers(null);

                return;
            }
        }

        items.add(item);
        notifySubscribers(null);
    }

    public void decItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);

            if (exist._itemStockId() == item._itemStockId()) {
                int newQty = exist.qty() - item.qty();

                if (newQty > 0) {
                    items.set(i,
                            new CartItem(exist._itemStockId(), exist.productName(), exist.category(), exist.brand(),
                                    newQty, exist.price()));
                } else {
                    items.remove(i);
                }
                notifySubscribers(null);

                break;
            }
        }
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

    public double totalPrice() {
        return items.stream().mapToDouble((item) -> item.getTotalPrice()).sum();
    }

    public int totalQuantity() {
        return items.stream().mapToInt((item) -> item.qty()).sum();
    }

    public void destroy() {
        items.clear();
        subscribers.clear();
    }

    public ArrayList<CartItem> getAllItems() {
        return items;
    }

    @Override
    public void notifySubscribers(Void value) {
        subscribers.forEach((cb) -> cb.accept(value));
    }

    @Override
    public void subscribe(Consumer<Void> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Consumer<Void> subscriber) {
        subscribers.remove(subscriber);
    }
}
