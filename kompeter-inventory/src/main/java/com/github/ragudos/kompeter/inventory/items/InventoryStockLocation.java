/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory.items;

import org.jetbrains.annotations.NotNull;

public final class InventoryStockLocation {
    private int _stockLocationId;
    private int itemQuantity;
    private String locationName;

    public InventoryStockLocation(int _stockLocationId, @NotNull String locationName, int itemQuantity) {
        this._stockLocationId = _stockLocationId;
        this.locationName = locationName;
        this.itemQuantity = itemQuantity;
    }

    public int _stockLocationId() {
        return _stockLocationId;
    }

    public int itemQuantity() {
        return itemQuantity;
    }

    public String locationName() {
        return locationName;
    }
}
