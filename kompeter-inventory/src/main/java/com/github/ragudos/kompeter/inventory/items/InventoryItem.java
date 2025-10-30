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

public class InventoryItem {
    protected final int _itemStockId;
    protected String brand;
    protected String[] categories;
    protected String displayImage;
    protected final String itemName;
    protected BigDecimal unitPricePhp;

    public InventoryItem(@Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId, @NotNull String itemName,
            String displayImage, @NotNull BigDecimal unitPricePhp, @NotNull String[] categories,
            @NotNull String brand) {
        this._itemStockId = _itemStockId;
        this.itemName = itemName;
        this.displayImage = displayImage;
        this.unitPricePhp = unitPricePhp;
        this.categories = categories;
        this.brand = brand;
    }

    public int _itemStockId() {
        return _itemStockId;
    }

    public String brand() {
        return brand;
    }

    public String[] categories() {
        return categories;
    }

    public String displayImage() {
        return displayImage;
    }

    public boolean isBrandOf(String[] brands) {
        for (String thisB : brands) {
            if (brand.toLowerCase().trim().equals(thisB.toLowerCase().trim())) {
                return true;
            }
        }

        return false;
    }

    public boolean isCategoryOf(String[] categories) {
        for (String c : categories) {
            for (String thisC : this.categories) {
                if (c.toLowerCase().trim().equals(thisC.toLowerCase().trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    public String itemName() {
        return itemName;
    }

    public String stringifiedCategories() {
        return String.join(",", categories);
    }

    public BigDecimal unitPricePhp() {
        return unitPricePhp;
    }

    // create a generic builder for builders that have subclass builders
    public abstract static class IBuilder<I extends InventoryItem, B extends IBuilder<I, B>> {
        protected int _itemStockId;
        protected String brand; // here we pass the concrete subclass that will be constructed
        protected String[] categories = new String[0];
        protected String displayImage;
        protected String itemName;
        protected BigDecimal unitPricePhp;

        public InventoryItem build() {
            validate();
            return new InventoryItem(_itemStockId, itemName, displayImage, unitPricePhp, categories.clone(), brand);
        }

        public B setBrand(String brand) {
            this.brand = brand;

            return self();
        }

        public B setCategories(String[] categories) {
            this.categories = categories;

            return self();
        }

        public B setDisplayImage(String displayImage) {
            this.displayImage = displayImage;

            return self();
        }

        public B setItemName(String itemName) {
            this.itemName = itemName;

            return self();
        }

        public B setItemStockId(int _itemStockId) {
            this._itemStockId = _itemStockId;

            return self();
        }

        public B setUnitPricePhp(BigDecimal unitPricePhp) {
            this.unitPricePhp = unitPricePhp;

            return self();
        }

        protected void validate() {
            if (_itemStockId < 0) {
                throw new IllegalStateException("_itemStockId must be >= 0");
            }

            if (itemName == null || itemName.isBlank()) {
                throw new IllegalStateException("itemName cannot be null or blank");
            }

            if (unitPricePhp == null) {
                throw new IllegalStateException("unitPricePhp cannot be null");
            }

            if (brand == null || brand.isBlank()) {
                throw new IllegalStateException("brand cannot be null or blank");
            }

            if (categories == null) {
                throw new IllegalStateException("categories cannot be null");
            }
        }

        @SuppressWarnings("unchecked")
        final B self() {
            return (B) this;
        }
    }

    public static class InventoryItemBuilder extends IBuilder<InventoryItem, InventoryItemBuilder> {
    }
}
