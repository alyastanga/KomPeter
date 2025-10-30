/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory.items;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public final class InventoryItemWithStockLocations extends InventoryItem {
    private InventoryStockLocation[] locations;

    public InventoryItemWithStockLocations(@Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId,
            @NotNull String itemName, String displayImage, @NotNull BigDecimal unitPricePhp,
            @NotNull String[] categories, @NotNull String brand, InventoryStockLocation[] locations) {
        super(_itemStockId, itemName, displayImage, unitPricePhp, categories, brand);

        this.locations = locations;
    }
}
