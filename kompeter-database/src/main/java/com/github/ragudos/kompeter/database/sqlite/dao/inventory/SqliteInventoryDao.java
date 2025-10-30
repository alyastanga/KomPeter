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

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import com.github.ragudos.kompeter.utilities.StringUtils;

public class SqliteInventoryDao implements InventoryDao {
    @Override
    public InventoryMetadataDto[] getAllInventoryItems(@NotNull Connection conn) throws SQLException, IOException {
        ArrayList<InventoryMetadataDto> inventory = new ArrayList<>();

        var query = SqliteQueryLoader.getInstance().get("select_all_inventory_metadata", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);

        try (var stmt = conn.prepareStatement(query);) {
            var rs = stmt.executeQuery();

            while (rs.next()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ItemStockStorageLocationDto[] storageLocations = objectMapper
                        .readValue(rs.getString("item_storage_locations"), ItemStockStorageLocationDto[].class);

                InventoryMetadataDto metadata = new InventoryMetadataDto.InventoryMetadataDtoBuilder()
                        .setStatus(ItemStatus.fromString(rs.getString("status")))
                        .setItemStockId(rs.getInt("_item_stock_id")).setItemId(rs.getInt("_item_id"))
                        .setCreatedAt(rs.getTimestamp("_created_at")).setItemName(rs.getString("name"))
                        .setItemDescription(rs.getString("description")).setDisplayImage(rs.getString("display_image"))
                        .setCategories(StringUtils.splitTrim(rs.getString("categories"), ","))
                        .setBrand(rs.getString("brand")).setMinimumQuantity(rs.getInt("minimum_quantity"))
                        .setUnitPricePhp(rs.getBigDecimal("unit_price_php")).setItemStockLocations(storageLocations)
                        .build();

                inventory.add(metadata);
            }
        }

        return inventory.toArray(new InventoryMetadataDto[inventory.size()]);
    }
}
