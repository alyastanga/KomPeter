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

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Peter M. Dela Cruz
 */
public record ItemStockStorageLocationDto(int _itemStockStorageLocationId, int _itemStockId, int _storageLocationId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @NotNull Timestamp _createdAt,
        @NotNull String name, @NotNull String description, int quantity) {
}
