/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteItemCategoryAssignmentDao implements ItemCategoryAssignmentDao {
    @Override
    public int setItemCategory(Connection conn, int itemId, String categoryName) throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("insert_item_category", "item_category_assignments",
                        AbstractSqlQueryLoader.SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt("_item_id", itemId);
            stmt.setString("category_name", categoryName);
            stmt.executeUpdate();

            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
