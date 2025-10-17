/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteItemCategoryDao implements ItemCategoryDao {
    private final Connection conn;

    public SqliteItemCategoryDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertItemCategory(String name, String description) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_category", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = conn.prepareStatement(query); ) {
            stmt.setString(1, name);
            stmt.setString(2, description);

            var insert = stmt.executeUpdate();
        }
    }
}
