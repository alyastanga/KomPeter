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
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Peter M. Dela Cruz
 */
public class SqliteItemStockStorageLocationDao implements ItemStockStorageLocationDao {

    @Override
    public int setItemStockStorageLocation(int itemStockId, int storageLocId, int qty)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_item_storage_loc", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmt.setInt("_item_stock_id", itemStockId);
            stmt.setInt("_storage_location_id", storageLocId);
            stmt.setInt("quantity", qty);

            stmt.executeUpdate();
            var rs = stmt.getPreparedStatement().getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
