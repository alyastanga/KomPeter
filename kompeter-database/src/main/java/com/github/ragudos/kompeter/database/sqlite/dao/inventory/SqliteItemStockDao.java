/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class SqliteItemStockDao implements ItemStockDao {
    private final Connection conn;
    
    public SqliteItemStockDao(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insertItemStock(int itemId, int itemBrandId, double unit_price, int min_qty) throws SQLException, IOException {
        var query=
                SqliteQueryLoader.getInstance()
                        .get(
                        "insert_item_stock",
                        "items",
                        AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try(var stmt = conn.prepareStatement(query);){
            stmt.setInt(1, itemId);
            stmt.setInt(2, itemBrandId);
            stmt.setDouble(3, unit_price);
            stmt.setInt(4, min_qty);
            
            var insert = stmt.executeUpdate();
        }
    }
}
