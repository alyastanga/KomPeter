/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteSaleDao implements SaleDao {

    @Override
    public int saveTransaction(SaleDto sale) throws SQLException {
        int generatedId = -1;

        String sqlFileName = "insert_transaction";
        String query;

        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(sqlFileName, "sales", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        } catch (IOException e) {
            throw new SQLException("Error loading SQL file", e);
        }

        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            if (codeExist(con, sale.saleCode())) {
                throw new SQLException("Duplicate Sale Code: " + sale.saleCode());
            }

            ps.setTimestamp(1, sale._createdAt());
            ps.setTimestamp(2, sale.saleDate());
            ps.setString(3, sale.saleCode());
            ps.setString(4, sale.customerName());
            ps.setBigDecimal(5, sale.vatPercent());
            ps.setBigDecimal(6, sale.discountValue());
            ps.setString(7, sale.discountType().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }
        return generatedId;
    }

    private boolean codeExist(Connection con, String saleCode) throws SQLException {
        String sqlFileName = "select_from";
        String query;

        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(sqlFileName, "sales", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Error loading SQL file", e);
        }

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, saleCode);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public SaleDto getTransaction(int _saleId) throws SQLException {
        SaleDto saleDto = null;
        String sqlFileName = "sales";
        String query;

        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(sqlFileName, "sales", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Error loading SQL file", e);
        }

        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, _saleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    saleDto =
                            new SaleDto(
                                    rs.getInt("_sale_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getTimestamp("sale_date"),
                                    rs.getString("sale_code"),
                                    rs.getString("customer_name"),
                                    rs.getBigDecimal("vat_percent"),
                                    rs.getBigDecimal("discount_value"),
                                    DiscountType.valueOf(rs.getString("discount_type").toUpperCase()));
                }
            }
        }
        return saleDto;
    }

    @Override
    public List<SaleDto> getAllTransaction() throws SQLException {
        List<SaleDto> list = new ArrayList<>();
        String sqlFileName = "select_from_sales";
        String query;

        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(sqlFileName, "sales", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Error loading SQL file", e);
        }

        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(
                            new SaleDto(
                                    rs.getInt("_sale_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getTimestamp("sale_date"),
                                    rs.getString("sale_code"),
                                    rs.getString("customer_name"),
                                    rs.getBigDecimal("vat_percent"),
                                    rs.getBigDecimal("discount_value"),
                                    DiscountType.valueOf(rs.getString("discount_type").toUpperCase())));
                }
            }
        }
        return list;
    }
}
