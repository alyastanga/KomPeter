/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteSalePaymentDao implements SalePaymentDao {
    @Override
    public int createPayment(@NotNull Connection conn, @Range(from = 0, to = 2147483647) int _saleId,
            @NotNull PaymentMethod paymentMethod, @NotNull String referenceNumber, @NotNull BigDecimal amount,
            @NotNull Timestamp paymentDate) throws SQLException, IOException {
        try (NamedPreparedStatement ps = new NamedPreparedStatement(conn, SqliteQueryLoader.getInstance()
                .get("create_sale_payment", "sale_payments", AbstractSqlQueryLoader.SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt("_sale_id", _saleId);
            ps.setString("payment_method", paymentMethod.toString());
            ps.setString("reference_number", referenceNumber);
            ps.setTimestamp("payment_date", paymentDate);
            ps.setBigDecimal("amount_php", amount);

            ps.executeUpdate();

            ResultSet rs = ps.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
