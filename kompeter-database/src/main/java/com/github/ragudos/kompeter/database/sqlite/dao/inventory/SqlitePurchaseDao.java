/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.PurchaseDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SqlitePurchaseDao implements PurchaseDao {
    private final Connection conn;
    public SqlitePurchaseDao(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insertPurchase(
            int suppID, Timestamp purchase_date, String purch_code,
            Timestamp deliverydate, float vat_percentage, float disc_val, 
            DiscountType discountType) throws SQLException, IOException {
        
        
        var query =
             SqliteQueryLoader.getInstance()
                    .get(
                            "insert_purchase",
                            "items",
                            AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try(var stmt= conn.prepareStatement(query);){
            stmt.setInt(1, suppID);
            stmt.setTimestamp(2, purchase_date);
            stmt.setString(3, purch_code);
            stmt.setTimestamp(4, deliverydate);
            stmt.setFloat(5, vat_percentage);
            stmt.setFloat(6, disc_val);
            stmt.setString(7, discountType.toString());
        }
    }

    @Override
    public List<PurchaseDto> getAllPurchase() throws SQLException, IOException {
        List<PurchaseDto> purchases = new ArrayList<>();
        
        var query =
                SqliteQueryLoader.getInstance()
                    .get(
                            "select_all_purchase",
                            "items",
                            AbstractSqlQueryLoader.SqlQueryType.SELECT);
       try(var stmt = conn.prepareStatement(query);
           var rs = stmt.executeQuery();){
           while(rs.next()){
               PurchaseDto purchase = new PurchaseDto(
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
