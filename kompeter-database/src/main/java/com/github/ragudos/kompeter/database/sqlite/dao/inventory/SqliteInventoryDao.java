/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteInventoryDao implements InventoryDao {
    private final Connection conn;

    public SqliteInventoryDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<InventoryMetadataDto> getAllData(
             String search, OrderBy orderBy, Direction direction) throws SQLException, IOException {
        List<InventoryMetadataDto> inventory = new ArrayList<>();
        var query =
                SqliteQueryLoader.getInstance()
                        .get(
                                "select_all_inventory_metadata",
                                "items",
                                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        StringBuilder sql = new StringBuilder(query);
        if(search != null){
            sql.append("WHERE i.name LIKE ?,"
                    + "ic.name LIKE ? "
                    + "ib.name LIKE ? "
                    + "sl.name Like ? "
                    + "i._item_id Like ?");
        }
        
        sql.append("GROUP BY "
                + "i._item_id, "
                + "i._created_at, "
                + "ic.name, "
                + "i.name, "
                + "ib.name ");
                
        if (orderBy != null) {
            sql.append(" ORDER BY " + orderBy.toString());
        }else{
            sql.append(" ORDER BY i._item_id");
        }
        if (direction != null) {
            sql.append(direction == Direction.ASC ? " ASC " : " DESC "); 
        }

        try (var stmt = conn.prepareStatement(sql.toString()); ) {
            if(search != null){
                String searchPattern = search.trim() + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                stmt.setString(4, searchPattern);
                stmt.setString(5, searchPattern);
            }
            var rs = stmt.executeQuery();
            while (rs.next()) {
                InventoryMetadataDto metadata =
                        new InventoryMetadataDto(
                                rs.getInt("_item_id"),
                                rs.getInt("_item_stock_id"),
                                rs.getInt("_item_stock_storage_location_id"),
                                rs.getTimestamp("_created_at"),
                                rs.getString("category_name"),
                                rs.getString("item_name"),
                                rs.getString("description"),
                                rs.getString("brand_name"),
                                rs.getDouble("unit_price_php"),
                                rs.getInt("quantity"),
                                rs.getString("location_name"));
                inventory.add(metadata);
            }
        }
        return inventory;
    }
}
