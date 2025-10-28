/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

public record CartItem(int _itemStockId, String productName, String category, String brand, int qty, double price) {
    public double getTotalPrice() {
        return qty * price;
    }

    public CartItem decrement() {
        return new CartItem(_itemStockId, productName, category, brand, qty - 1, price);
    }

    public CartItem increment() {
        return new CartItem(_itemStockId, productName, category, brand, qty + 1, price);
    }
}
