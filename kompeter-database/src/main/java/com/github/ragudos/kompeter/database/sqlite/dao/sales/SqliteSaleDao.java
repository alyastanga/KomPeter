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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteSaleDao implements SaleDao {
    @Override
    public int createSale(@NotNull final Connection conn, @NotNull final Timestamp saleDate,
            @NotNull final String saleCode, final BigDecimal vatPercent, @NotNull final DiscountType discountType,
            @NotNull final BigDecimal discountValue) throws SQLException, IOException {
        try (NamedPreparedStatement stmnt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("create_sale", "sales", SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS)) {
            stmnt.setTimestamp("sale_date", saleDate);
            stmnt.setString("sale_code", saleCode);
            stmnt.setBigDecimal("vat_percent", vatPercent);
            stmnt.setString("discount_type", discountType == null ? null : discountType.toString());
            stmnt.setBigDecimal("discount_value", discountValue);

            stmnt.executeUpdate();

            final ResultSet rs = stmnt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public Optional<SaleDto> getTransaction(final Connection conn, final int _saleId) throws SQLException, IOException {
        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(SqliteQueryLoader.getInstance().get("select_sale_by_id",
                        "sales", AbstractSqlQueryLoader.SqlQueryType.SELECT))) {
            ps.setInt(1, _saleId);

            final ResultSet rs = ps.executeQuery();

            return rs.next()
                    ? Optional.of(new SaleDto(rs.getInt("_sale_id"), rs.getTimestamp("_created_at"),
                            rs.getTimestamp("sale_date"), rs.getString("sale_code"), rs.getString("customer_name"),
                            rs.getBigDecimal("vat_percent"), rs.getBigDecimal("discount_value"),
                            DiscountType.valueOf(rs.getString("discount_type").toUpperCase())))
                    : Optional.empty();
        }
    }
}
