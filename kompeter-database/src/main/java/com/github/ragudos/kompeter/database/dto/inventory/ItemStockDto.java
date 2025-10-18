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
import org.jetbrains.annotations.NotNull;

public record ItemStockDto(
        int _itemStockId,
        int _itemId,
        int _itemBrandId,
        @NotNull Timestamp _createdAt,
        @NotNull BigDecimal unitPricePhp,
        int quantity,
        int minimumQuantity) {}
