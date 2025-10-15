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

public record ItemRestockDto(
        int _itemRestockId,
        @NotNull Timestamp _createdAt,
        int _itemStockId,
        int quantityBefore,
        int quantityAfter,
        int quantityAdded) {}
