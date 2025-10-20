/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Peter M. Dela Cruz
 */
public interface ItemStockStorageLocationDao {
    int setItemStockStorageLocation(int itemStockId, int storageLocId, int qty)
            throws SQLException, IOException;

    int updateItemStockQuantity(int qtyAfter, int itemStockId, int storageLocationId)
            throws SQLException, IOException;
}
