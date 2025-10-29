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
import java.util.ArrayList;
import java.util.List;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteInventoryDao implements InventoryDao {
    @Override
    public List<InventoryMetadataDto> getAllData() throws SQLException, IOException {
        List<InventoryMetadataDto> inventory = new ArrayList<>();

        var query = SqliteQueryLoader.getInstance().get("select_all_inventory_metadata", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);) {
            var rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryMetadataDto metadata = new InventoryMetadataDto(rs.getInt("_item_id"),
                        rs.getInt("_item_stock_id"), rs.getInt("_item_stock_storage_location_id"),
                        rs.getTimestamp("_created_at"), rs.getString("category_name"), rs.getString("item_name"),
                        rs.getString("description"), rs.getString("brand_name"), rs.getDouble("unit_price_php"),
                        rs.getInt("quantity"), rs.getString("location_name"), rs.getString("display_image"));
                inventory.add(metadata);
            }
        }
        return inventory;
    }

    @Override
    public List<InventoryMetadataDto> getAllData(String search) throws SQLException, IOException {
        List<InventoryMetadataDto> inventory = new ArrayList<>();

        var query = SqliteQueryLoader.getInstance().get("select_inventory_metadata_where", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);) {
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            stmt.setString(4, search);

            var rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryMetadataDto metadata = new InventoryMetadataDto(rs.getInt("_item_id"),
                        rs.getInt("_item_stock_id"), rs.getInt("_item_stock_storage_location_id"),
                        rs.getTimestamp("_created_at"), rs.getString("category_name"), rs.getString("item_name"),
                        rs.getString("description"), rs.getString("brand_name"), rs.getDouble("unit_price_php"),
                        rs.getInt("quantity"), rs.getString("location_name"), rs.getString("display_image"));
                inventory.add(metadata);
            }
        }
        return inventory;
    }
}
