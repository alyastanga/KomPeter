/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;

/**
 * @author Peter M. Dela Cruz
 */
public interface ItemStockStorageLocationDao {
    List<ItemStockStorageLocationDto> getAllData(Connection conn) throws SQLException, IOException;

    int setItemStockStorageLocation(Connection conn, int itemStockId, int storageLocId, int qty)
            throws SQLException, IOException;

    int updateItemStockQuantity(Connection conn, int qtyAfter, int itemStockId, int storageLocationId)
            throws SQLException, IOException;
}
