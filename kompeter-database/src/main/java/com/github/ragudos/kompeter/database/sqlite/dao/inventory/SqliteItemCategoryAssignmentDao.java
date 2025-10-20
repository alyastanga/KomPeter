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
import com.github.ragudos.kompeter.database.dao.inventory.ItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteItemCategoryAssignmentDao implements ItemCategoryAssignmentDao {

    @Override
    public int setItemCategory(int itemId, int itemCategoryId) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "insert_item_category_assignment",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setInt("_item_id", itemId);
            stmt.setInt("_item_category_id", itemCategoryId);
            stmt.executeUpdate();

            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public int updateItemCategoryById(int itemId, int itemCategoryId)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("update_item_category", "items", AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("_item_category_id", itemCategoryId);
            stmt.setInt("_item_id", itemId);
            return stmt.executeUpdate();
        }
    }
}
