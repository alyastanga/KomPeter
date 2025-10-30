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

public final class InventoryItemWithTotalQuantity extends InventoryItem {
    protected int totalQuantity;

    public InventoryItemWithTotalQuantity(@Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId,
            @NotNull String itemName, String displayImage, @NotNull BigDecimal unitPricePhp,
            @NotNull String[] categories, @NotNull String brand,
            @Range(from = 0, to = Integer.MAX_VALUE) int totalQuantity) {
        super(_itemStockId, itemName, displayImage, unitPricePhp, categories, brand);

        this.totalQuantity = totalQuantity;
    }

    public int totalQuantity() {
        return totalQuantity;
    }

    public static class InventoryItemWithTotalQuantityBuilder
            extends
                IBuilder<InventoryItemWithTotalQuantity, InventoryItemWithTotalQuantityBuilder> {
        protected int totalQuantity;

        @Override
        public InventoryItemWithTotalQuantity build() {
            validate();

            return new InventoryItemWithTotalQuantity(super._itemStockId, super.itemName, super.displayImage,
                    super.unitPricePhp, super.categories, super.brand, totalQuantity);
        }

        public InventoryItemWithTotalQuantityBuilder setTotalQuantity(int totalQuantity) {
            this.totalQuantity = totalQuantity;

            return self();
        }
    }
}
