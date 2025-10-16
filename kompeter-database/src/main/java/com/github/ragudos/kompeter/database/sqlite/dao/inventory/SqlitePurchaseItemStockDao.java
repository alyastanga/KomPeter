/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.PurchaseItemStockDao;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class SqlitePurchaseItemStockDao implements PurchaseItemStockDao {
    private final Connection conn;
    
    public SqlitePurchaseItemStockDao(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public List<PurchaseItemStockDto> getPurchaseStock() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from, Timestamp to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void insertPurchaseItemStock(int purchaseId, int itemStockId, int qty_ordered, int qty_received, double unit_cost_php) throws SQLException, IOException {
        var query=
                SqliteQueryLoader.getInstance()
                        .get(
                        "insert_purchase_item_stock",
                        "items",
                        AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try(var stmt = conn.prepareStatement(query);){
            stmt.setInt(1, purchaseId);
            stmt.setInt(2, itemStockId);
            stmt.setInt(3, qty_ordered);
            stmt.setInt(4, qty_received);
            stmt.setDouble(5, unit_cost_php);
            
            
            var insert = stmt.executeUpdate();
        }
    }
}
