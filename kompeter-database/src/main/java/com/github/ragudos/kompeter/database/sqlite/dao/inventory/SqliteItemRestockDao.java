/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.ItemRestockDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteItemRestockDao implements ItemRestockDao {
    private final Connection conn;

    public SqliteItemRestockDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertItemRestock(int itemStockId, int qty_before, int qty_after, int qty_added)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_restock", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = conn.prepareStatement(query); ) {
            stmt.setInt(1, itemStockId);
            stmt.setInt(2, qty_before);
            stmt.setInt(3, qty_after);
            stmt.setInt(4, qty_added);

            var insert = stmt.executeUpdate();
        }
    }
}
