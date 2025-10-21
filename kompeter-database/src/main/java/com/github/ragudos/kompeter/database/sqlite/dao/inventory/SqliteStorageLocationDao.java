/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.StorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

/**
 * @author Peter M. Dela Cruz
 */
public class SqliteStorageLocationDao implements StorageLocationDao {

    @Override
    public int deleteStorageLocationById(int id) throws SQLException, IOException {
        var query = SqliteQueryLoader.getInstance().get("delete_storage_location", "items",
                AbstractSqlQueryLoader.SqlQueryType.DELETE);
        try (var conn = SqliteFactoryDao.getInstance().getConnection(); var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            var rs = stmt.executeUpdate();
            return rs;
        }
    }

    @Override
    public int insertStorageLocation(String setString, String description) throws SQLException, IOException {
        var query = SqliteQueryLoader.getInstance().get("insert_storage_location", "items",
                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query,
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString("name", setString);
            stmt.setString("description", description);

            stmt.executeUpdate();
            var rs = stmt.getPreparedStatement().getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
