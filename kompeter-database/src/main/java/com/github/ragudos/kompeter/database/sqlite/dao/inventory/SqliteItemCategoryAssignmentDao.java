/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteItemCategoryAssignmentDao implements ItemCategoryAssignmentDao {
    private final Connection conn;

    public SqliteItemCategoryAssignmentDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setItemCategory(int itemId, int itemCategoryId) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "insert_item_category_assignment",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = conn.prepareStatement(query); ) {
            stmt.setInt(1, itemId);
            stmt.setInt(2, itemCategoryId);

            var insert = stmt.executeUpdate();
        }
    }
}
