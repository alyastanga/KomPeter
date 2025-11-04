/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.inventory;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

/**
 * @author Peter M. Dela Cruz
 */
@ToString
public class ItemStockStorageLocationDto {
    Timestamp _createdAt;
    int _itemStockId;
    int _itemStockStorageLocationId;
    int _storageLocationId;
    String description;
    boolean isInitialized;
    String name;
    int quantity;

    @JsonCreator
    public ItemStockStorageLocationDto(
            @JsonProperty("_itemStockStorageLocationId") final int _itemStockStorageLocationId,
            @JsonProperty("_itemStockId") final int _itemStockId,
            @JsonProperty("_storageLocationId") final int _storageLocationId,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("_createdAt") @NotNull final Timestamp _createdAt,
            @JsonProperty("name") @NotNull final String name,
            @JsonProperty("description") @NotNull final String description,
            @JsonProperty("quantity") final int quantity, @JsonProperty("isInitialized") final boolean isInitialized) {
        this._createdAt = _createdAt;
        this._itemStockId = _itemStockId;
        this._itemStockStorageLocationId = _itemStockStorageLocationId;
        this._storageLocationId = _storageLocationId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.isInitialized = isInitialized;
    }

    public Timestamp _createdAt() {
        return _createdAt;
    }

    public int _itemStockId() {
        return _itemStockId;
    }

    public int _itemStockStorageLocationId() {
        return _itemStockStorageLocationId;
    }

    public int _storageLocationId() {
        return _storageLocationId;
    }

    public String description() {
        return description;
    }

    /**
     * @return whether the item with _itemStockId already has this specific location
     *         since we just get all storage locations and default to 0 if it's
     *         non-existent yet.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    public String name() {
        return name;
    }

    public int quantity() {
        return quantity;
    }

    public void updateQuantiy(final int newQty) {
        if (newQty < 0) {
            throw new IllegalArgumentException(
                    String.format("Quantity cannot be < %s for item stock storage location %s", 0, name));
        }

        this.quantity = newQty;
    }
}
