/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.sales;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;

public interface SaleItemStockDao {
    void createSaleItemStock(@NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _saleId,
            @Range(from = 0, to = Integer.MAX_VALUE) int _itemStockId,
            @Range(from = 0, to = Integer.MAX_VALUE) int quantity,
            @NotNull BigDecimal unitPricePhp) throws IOException, SQLException;
}
