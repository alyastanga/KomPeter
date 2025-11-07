/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import java.math.BigDecimal;

public class CartItem implements Cloneable {
    private final int _itemStockId;
    private final String name;
    private final BigDecimal price;
    private int qty;
    private final int stockQty;

    public BigDecimal vatUnitPrice() {
        return price.multiply(Transaction.VAT_RATE).multiply(new BigDecimal(qty));
    }

    public BigDecimal netPrice() {
        return price.subtract(vatUnitPrice()).multiply(new BigDecimal(qty));
    }

    public CartItem(final int _itemStockId, final String name, final int stockQty, final int qty,
            final BigDecimal price) throws InsufficientStockException, NegativeQuantityException {
        this._itemStockId = _itemStockId;
        this.name = name;
        this.qty = qty;
        this.stockQty = stockQty;
        this.price = price;

        if (this.qty < 0) {
            throw new NegativeQuantityException(String.format("Quantity of %s cannot exceed below 0.", name));
        }

        if (this.qty > stockQty) {
            throw new InsufficientStockException(
                    String.format("%s only has %s in stock, but %s was specified in the Cart.", name, stockQty, qty));
        }
    }

    public int _itemStockId() {
        return _itemStockId;
    }

    public void decreaseQty(final int qty) throws NegativeQuantityException {
        if (this.qty == 0 || this.qty - qty < 0) {
            throw new NegativeQuantityException(
                    String.format("Quantity of %s cannot exceed below 0, but %s was requested.", name, this.qty - qty));
        }

        this.qty -= qty;
    }

    public void decrement() throws NegativeQuantityException {
        if (qty == 0) {
            throw new NegativeQuantityException(
                    String.format("Quantity of %s cannot exceed below 0, but %s was requested.", name, qty - 1));
        }

        qty--;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(String.format("%s", qty)));
    }

    public void increaseQty(final int qty) throws InsufficientStockException {
        if (this.qty == Integer.MAX_VALUE || this.qty + qty > Integer.MAX_VALUE) {
            return;
        }

        if (this.qty == stockQty || this.qty + qty > stockQty) {
            throw new InsufficientStockException(
                    String.format("%s only has %s in stock, but %s was requested", name, stockQty, this.qty + qty));
        }

        this.qty += qty;
    }

    public void increment() throws InsufficientStockException {
        if (qty == Integer.MAX_VALUE) {
            return;
        }

        if (this.qty == stockQty || this.qty + 1 > stockQty) {
            throw new InsufficientStockException(
                    String.format("%s only has %s in stock, but %s was requested", name, stockQty, this.qty + 1));
        }

        qty++;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
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
        } catch (final CloneNotSupportedException err) {
            throw new AssertionError();
        }
    }
}
