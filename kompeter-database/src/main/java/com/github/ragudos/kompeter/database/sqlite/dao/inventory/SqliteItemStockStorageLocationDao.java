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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

/**
 * @author Peter M. Dela Cruz
 */
public class SqliteItemStockStorageLocationDao implements ItemStockStorageLocationDao {
    @Override
    public ItemStockStorageLocationDto[] getAllData(final Connection conn, final int _itemStockId)
            throws SQLException, IOException {
        final List<ItemStockStorageLocationDto> listItemStockStorageLocation = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                SqliteQueryLoader.getInstance().get("select_all_issl", "items",
                        AbstractSqlQueryLoader.SqlQueryType.SELECT));) {
            stmt.setInt(1, _itemStockId);
            final ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                final ItemStockStorageLocationDto issl = new ItemStockStorageLocationDto(
                        rs.getInt("_item_stock_storage_location_id"), rs.getInt("_item_stock_id"),
                        rs.getInt("_storage_location_id"), rs.getTimestamp("_created_at"), rs.getString("name"),
                        rs.getString("description"), rs.getInt("quantity"), true);

                listItemStockStorageLocation.add(issl);
            }
        }
        return listItemStockStorageLocation.toArray(ItemStockStorageLocationDto[]::new);
    }

    @Override
    public int setItemStockStorageLocation(final Connection conn, final int itemStockId, final int storageLocId,
            final int qty) throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn,
                SqliteQueryLoader.getInstance().get("insert_item_stock_storage_location",
                        "item_stock_storage_locations", AbstractSqlQueryLoader.SqlQueryType.INSERT),
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt("_item_stock_id", itemStockId);
            stmt.setInt("_storage_location_id", storageLocId);
            stmt.setInt("quantity", qty);

            stmt.executeUpdate();
            final var rs = stmt.getPreparedStatement().getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public int updateItemStockQuantity(final Connection conn, final int qtyAfter, final int _itemStockStorageLocationId)
            throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn, SqliteQueryLoader.getInstance().get("update_quantity_by_id",
                "item_stock_storage_locations", SqlQueryType.UPDATE))) {
            stmt.setInt("quantity", qtyAfter);
            stmt.setInt("_item_stock_storage_location_id", _itemStockStorageLocationId);

            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateItemStockQuantity(final Connection conn, final int qtyAfter, final int itemStockId,
            final int storageLocationId) throws SQLException, IOException {
        try (var stmt = new NamedPreparedStatement(conn, SqliteQueryLoader.getInstance().get("update_quantity",
                "item_stock_storage_locations", SqlQueryType.UPDATE))) {
            stmt.setInt("quantity", qtyAfter);
            stmt.setInt("_item_stock_id", itemStockId);
            stmt.setInt("_storage_location_id", storageLocationId);

            return stmt.executeUpdate();
        }
    }
}
