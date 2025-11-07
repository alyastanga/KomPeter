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
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto;

public interface SaleDao {
    int createSale(@NotNull Connection conn, String customerName, @NotNull Timestamp saleDate, @NotNull String saleCode,
            BigDecimal vatPercent, @NotNull DiscountType discountType, @NotNull BigDecimal discountValue)
            throws SQLException, IOException;

    SaleMetadataDto[] getAllSales(@NotNull Connection conn) throws SQLException, IOException;

    Optional<SaleDto> getTransaction(@NotNull Connection conn, int saleId) throws SQLException, IOException;
}
