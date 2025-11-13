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
import java.util.ArrayList;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto.SaleItemStocks;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto.SaleMetadataPayments;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteSaleDao implements SaleDao {
    @Override
    public int createSale(@NotNull final Connection conn, final String customerName, @NotNull final Timestamp saleDate,
            @NotNull final String saleCode, final BigDecimal vatPercent, @NotNull final DiscountType discountType,
            @NotNull final BigDecimal discountValue) throws SQLException, IOException {
        try (NamedPreparedStatement stmnt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("create_sale", "sales", SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(saleDate);
            stmnt.setString("customer_name", customerName);
            stmnt.setString("sale_date", saleDate.toString());
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
    public SaleMetadataDto[] getAllSales(@NotNull final Connection conn) throws SQLException, IOException {
        try (final Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SqliteQueryLoader.getInstance().get("select_all_sales_metadata",
                        "sales", SqlQueryType.SELECT))) {
            final ArrayList<SaleMetadataDto> sales = new ArrayList<>();

            while (rs.next()) {
                final ObjectMapper om = new ObjectMapper();
                final SaleMetadataPayments[] payments = om.readValue(rs.getString("payments"),
                        SaleMetadataPayments[].class);
                final SaleItemStocks[] items = om.readValue(rs.getString("items"), SaleItemStocks[].class);

                sales.add(SaleMetadataDto.builder().createdAt(rs.getTimestamp("_created_at"))
                        .saleDate(rs.getTimestamp("sale_date")).customerName(rs.getString("customer_name"))
                        .saleCode(rs.getString("sale_code")).saleId(rs.getInt("_sale_id")).payments(payments)
                        .saleItemStocks(items).vatPercent(rs.getBigDecimal("vat_percent"))
                        .discountType(rs.getString("discount_type")).discountValue(rs.getBigDecimal("discount_value"))
                        .build());
            }

            return sales.toArray(new SaleMetadataDto[sales.size()]);
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
