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
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter M. Dela Cruz
 */
public class SqliteItemStockStorageLocationDao implements ItemStockStorageLocationDao {

    @Override
    public int setItemStockStorageLocation(int itemStockId, int storageLocId, int qty)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "insert_item_stock_storage_loc",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.INSERT);
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

    @Override
    public int updateItemStockQuantity(int qtyAfter, int itemStockId, int storageLocationId)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("update_item_stock_quantity", "items", AbstractSqlQueryLoader.SqlQueryType.UPDATE);
        try (var stmt =
                new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query)) {
            stmt.setInt("quantity", qtyAfter);
            stmt.setInt("_item_stock_id", itemStockId);
            stmt.setInt("_storage_location_id", storageLocationId);

            return stmt.executeUpdate();
        }
    }

    @Override
    public List<ItemStockStorageLocationDto> getAllData() throws SQLException, IOException {
        List<ItemStockStorageLocationDto> listItemStockStorageLocation = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get("select_all_issl", "items", AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query)) {

            var rs = stmt.executeQuery();
            while (rs.next()) {
                ItemStockStorageLocationDto issl =
                        new ItemStockStorageLocationDto(
                                rs.getInt("_item_stock_storage_location_id"),
                                rs.getInt("_item_stock_id"),
                                rs.getInt("_storage_location_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getInt("quantity"));
                listItemStockStorageLocation.add(issl);
            }
        }
        return listItemStockStorageLocation;
    }

    @Override
    public int deleteIssl(int id) throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "delete_item_stock_storage_loc",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.DELETE);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            var rs = stmt.executeUpdate();
            return rs;
        }
    }
}
