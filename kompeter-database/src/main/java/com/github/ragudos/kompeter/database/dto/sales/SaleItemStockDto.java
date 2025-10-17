/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.sales;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record SaleItemStockDto(
        int _saleItemStockId,
        int _saleId,
        int _itemStockId,
        @NotNull Timestamp _createdAt,
        int quantity,
        @NotNull BigDecimal unitPricePhp) {}
