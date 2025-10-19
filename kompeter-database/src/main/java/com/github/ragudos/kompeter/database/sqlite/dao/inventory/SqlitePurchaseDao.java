/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.PurchaseDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SqlitePurchaseDao implements PurchaseDao {
    @Override
    public int insertPurchase(
            int suppID,
            Timestamp purchase_date,
            String purch_code,
            Timestamp deliverydate,
            BigDecimal vat_percentage,
            BigDecimal disc_val,
            DiscountType discountType)
            throws SQLException, IOException {

        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_purchase", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setInt("_supplier_id", suppID);
            stmt.setTimestamp("purchase_date", purchase_date);
            stmt.setString("purchase_code", purch_code);
            stmt.setTimestamp("delivery_date", deliverydate);
            stmt.setBigDecimal("vat_percent", vat_percentage);
            stmt.setBigDecimal("discount_value", disc_val);
            stmt.setString("discount_type", discountType.toString());
            
            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public List<PurchaseDto> getAllPurchase() throws SQLException, IOException {
        List<PurchaseDto> purchases = new ArrayList<>();

        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_purchase", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery(); ) {
            while (rs.next()) {
                PurchaseDto purchase =
                        new PurchaseDto(
                                rs.getInt("_purchase_id"),
                                rs.getInt("_supplier_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getTimestamp("purchase_date"),
                                rs.getString("purchase_code"),
                                rs.getTimestamp("delivery_date"),
                                rs.getBigDecimal("vat_percent"),
                                rs.getBigDecimal("discount_value"),
                                DiscountType.fromString(rs.getString("discount_type")));
                purchases.add(purchase);
            }
        }
        return purchases;
    }
}
