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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;

public interface SalePaymentDao {
    int createPayment(@NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _saleId,
            @NotNull PaymentMethod paymentMethod, @NotNull String referenceNumber, @NotNull BigDecimal amount,
            @NotNull Timestamp paymentDate)
            throws SQLException, IOException;
}
