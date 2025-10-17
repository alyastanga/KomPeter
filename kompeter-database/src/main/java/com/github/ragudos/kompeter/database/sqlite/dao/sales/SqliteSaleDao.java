/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteSaleDao implements SaleDao {

    @Override
    public int save(SaleDto sale) throws SQLException {
        int result;
        String sql =
                """
                INSERT INTO sales ( _created_at, sale_date, sale_code, customer_name, vat_percent, discount_value, discount_type)
                VALUES (?, ?, ?, ?, ?, ?, ?)\
                """;

        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

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

            System.out.println("This is Working");

            result = ps.executeUpdate();
        }
        return result;
    }

    private boolean codeExist(Connection con, String saleCode) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM sales WHERE sale_code = ?";

        try (PreparedStatement ps = con.prepareStatement(checkSql)) {
            ps.setString(1, saleCode);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("This is working");
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public SaleDto get(int saleId) throws SQLException {
        SaleDto saleDto = null;
        String sql =
                "SELECT _sale_id, _created_at, sale_date, sale_code, customer_name, vat_percent,"
                        + " discount_value, discount_type FROM sales WHERE _sale_id = ?";
        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, saleId);

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
    public List<SaleDto> getAll() throws SQLException {
        List<SaleDto> list = new ArrayList<>();
        String sql =
                "SELECT _sale_id, _created_at, sale_date, sale_code, customer_name, vat_percent,"
                        + " discount_value, discount_type FROM sales";
        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

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
