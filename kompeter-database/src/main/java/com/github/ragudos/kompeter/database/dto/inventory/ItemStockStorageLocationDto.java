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

/**
 * @author Peter M. Dela Cruz
 */
public record ItemStockStorageLocationDto(
        int _itemStockStorageLoc,
        int _itemStockId,
        int storageLocId,
        @NotNull Timestamp created_at,
        int quantity) {}
