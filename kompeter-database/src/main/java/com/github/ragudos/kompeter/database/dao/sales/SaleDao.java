/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.sales;

import com.github.ragudos.kompeter.database.dto.sales.SaleDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface SaleDao {
    int saveTransaction(SaleDto sale) throws SQLException;

    int createSale(@NotNull Connection conn, @NotNull Timestamp saleDate, @NotNull String saleCode,
            BigDecimal vatPercent)
            throws SQLException, IOException;

    SaleDto getTransaction(int saleId) throws SQLException;

    List<SaleDto> getAllTransaction() throws SQLException;
}
