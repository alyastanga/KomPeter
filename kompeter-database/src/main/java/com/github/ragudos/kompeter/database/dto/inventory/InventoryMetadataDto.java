/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.inventory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record InventoryMetadataDto(@Range(from = 0, to = Integer.MAX_VALUE) int _itemId,
        @Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId, @NotNull Timestamp _createdAt,
        @NotNull String itemName, @NotNull String itemDescription, String displayImage,
        @Range(from = 0, to = Integer.MAX_VALUE) int minimumQuantity, @NotNull BigDecimal unitPricePhp,
        @NotNull String[] categories, @NotNull String brand, @NotNull ItemStockStorageLocationDto[] itemStockLocations,
        @NotNull ItemStatus status) {

    public static final class InventoryMetadataDtoBuilder {
        private int _itemId;
        private int _itemStockId;
        private Timestamp _createdAt;
        private String itemName;
        private String itemDescription;
        private String displayImage;
        private int minimumQuantity;
        private BigDecimal unitPricePhp;
        private String[] categories;
        private String brand;
        private ItemStockStorageLocationDto[] itemStockLocations;
        private ItemStatus status;

        public InventoryMetadataDtoBuilder setItemId(final int _itemId) {
            this._itemId = _itemId;
            return this;
        }

        public InventoryMetadataDtoBuilder setStatus(final ItemStatus status) {
            this.status = status;
            return this;
        }

        public InventoryMetadataDtoBuilder setItemStockId(final int _itemStockId) {
            this._itemStockId = _itemStockId;
            return this;
        }

        public InventoryMetadataDtoBuilder setCreatedAt(final Timestamp _createdAt) {
            this._createdAt = _createdAt;
            return this;
        }

        public InventoryMetadataDtoBuilder setItemName(final String itemName) {
            this.itemName = itemName;
            return this;
        }

        public InventoryMetadataDtoBuilder setItemDescription(final String itemDescription) {
            this.itemDescription = itemDescription;
            return this;
        }

        public InventoryMetadataDtoBuilder setDisplayImage(final String displayImage) {
            this.displayImage = displayImage;
            return this;
        }

        public InventoryMetadataDtoBuilder setMinimumQuantity(final int minimumQuantity) {
            this.minimumQuantity = minimumQuantity;
            return this;
        }

        public InventoryMetadataDtoBuilder setUnitPricePhp(final BigDecimal unitPricePhp) {
            this.unitPricePhp = unitPricePhp;
            return this;
        }

        public InventoryMetadataDtoBuilder setCategories(final String[] categories) {
            this.categories = categories;
            return this;
        }

        public InventoryMetadataDtoBuilder setBrand(final String brand) {
            this.brand = brand;
            return this;
        }

        public InventoryMetadataDtoBuilder setItemStockLocations(
                final ItemStockStorageLocationDto[] itemStockLocations) {
            this.itemStockLocations = itemStockLocations;
            return this;
        }

        public InventoryMetadataDto build() {
            return new InventoryMetadataDto(_itemId, _itemStockId, _createdAt, itemName, itemDescription, displayImage,
                    minimumQuantity, unitPricePhp, categories, brand, itemStockLocations, status);
        }
    }

    public String stringifiedCategories() {
        return String.join(",", categories);
    }

    public boolean isCategoryOf(final String[] categories) {
        for (final String c : categories) {
            if (c == null) {
                continue;
            }

            for (final String thisC : this.categories) {
                if (c.toLowerCase().trim().equals(thisC.toLowerCase().trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isBrandOf(final String[] brands) {
        for (final String thisB : brands) {
            if (thisB == null) {
                continue;
            }

            if (brand.toLowerCase().trim().equals(thisB.toLowerCase().trim())) {
                return true;
            }
        }

        return false;
    }

    public int totalQuantity() {
        return this.itemStockLocations == null ? 0
                : Arrays.stream(this.itemStockLocations).mapToInt((loc) -> loc.quantity()).sum();
    }
};
