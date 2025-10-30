/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory.items;

import java.math.BigDecimal;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;

public final class InventoryItemWithStockLocations extends InventoryItem {
    private InventoryStockLocation[] locations;
    private ItemStatus status;

    public InventoryItemWithStockLocations(@Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId,
            @NotNull String itemName, String displayImage, @NotNull BigDecimal unitPricePhp,
            @NotNull String[] categories, @NotNull String brand, InventoryStockLocation[] locations,
            ItemStatus status) {
        super(_itemStockId, itemName, displayImage, unitPricePhp, categories, brand);

        this.locations = locations;
        this.status = status;
    }

    public InventoryStockLocation[] locations() {
        return locations;
    }

    public ItemStatus status() {
        return status;
    }

    public int totalQuantity() {
        return Arrays.stream(locations).mapToInt((itemLocation) -> itemLocation.itemQuantity()).sum();
    }

    public static class InventoryItemWithStockLocationsBuilder
            extends
                IBuilder<InventoryItemWithStockLocations, InventoryItemWithStockLocationsBuilder> {
        protected InventoryStockLocation[] locations;
        protected ItemStatus status;

        @Override
        public InventoryItemWithStockLocations build() {
            validate();

            return new InventoryItemWithStockLocations(super._itemStockId, super.itemName, super.displayImage,
                    super.unitPricePhp, super.categories, super.brand, locations, status);
        }

        public InventoryItemWithStockLocationsBuilder setLocations(InventoryStockLocation[] locations) {
            this.locations = locations;

            return self();
        }

        public InventoryItemWithStockLocationsBuilder setStatus(ItemStatus status) {
            this.status = status;

            return self();
        }
    }
}
