/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

public class CartItem implements Cloneable {
    private final int _itemStockId;
    private final String brand;
    private final String category;
    private final double price;
    private final String productName;
    private int qty;
    private final int stockQty;

    public CartItem(int _itemStockId, String productName, String category, String brand, int stockQty, int qty,
            double price) throws InsufficientStockException, NegativeQuantityException {
        this._itemStockId = _itemStockId;
        this.productName = productName;
        this.category = category;
        this.brand = brand;
        this.qty = qty;
        this.stockQty = stockQty;
        this.price = price;

        if (this.qty < 0) {
            throw new NegativeQuantityException(String.format("Quantity of %s cannot exceed below 0.", productName));
        }

        if (this.qty > stockQty) {
            throw new InsufficientStockException(String
                    .format("%s only has %s in stock, but %s was specified in the Cart.", productName, stockQty, qty));
        }
    }

    public int _itemStockId() {
        return _itemStockId;
    }

    public String brand() {
        return brand;
    }

    public String category() {
        return category;
    }

    public void decreaseQty(int qty) throws NegativeQuantityException {
        if (this.qty == 0 || this.qty - qty < 0) {
            throw new NegativeQuantityException(String.format(
                    "Quantity of %s cannot exceed below 0, but %s was requested.", productName, this.qty - qty));
        }

        this.qty -= qty;
    }

    public void decrement() throws NegativeQuantityException {
        if (qty == 0) {
            throw new NegativeQuantityException(
                    String.format("Quantity of %s cannot exceed below 0, but %s was requested.", productName, qty - 1));
        }

        qty--;
    }

    public double getTotalPrice() {
        return qty * price;
    }

    public void increaseQty(int qty) throws InsufficientStockException {
        if (this.qty == Integer.MAX_VALUE || this.qty + qty > Integer.MAX_VALUE) {
            return;
        }

        if (this.qty == stockQty || this.qty + qty > stockQty) {
            throw new InsufficientStockException(String.format("%s only has %s in stock, but %s was requested",
                    productName, stockQty, this.qty + qty));
        }

        this.qty += qty;
    }

    public void increment() throws InsufficientStockException {
        if (qty == Integer.MAX_VALUE) {
            return;
        }

        if (this.qty == stockQty || this.qty + 1 > stockQty) {
            throw new InsufficientStockException(String.format("%s only has %s in stock, but %s was requested",
                    productName, stockQty, this.qty + 1));
        }

        qty++;
    }

    public double price() {
        return price;
    }

    public String productName() {
        return productName;
    }

    public int qty() {
        return qty;
    }

    public int stockQty() {
        return stockQty;
    }

    @Override
    protected CartItem clone() {
        try {
            return (CartItem) super.clone();
        } catch (CloneNotSupportedException err) {
            throw new AssertionError();
        }
    }
}
