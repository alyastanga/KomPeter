/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Peter M. Dela Cruz
 */
public interface ItemStockStorageLocationDao {
    int setItemStockStorageLocation(int itemStockId, int storageLocId, int qty)
            throws SQLException, IOException;

    int updateItemStockQuantity(int qtyAfter, int itemStockId, int storageLocationId)
            throws SQLException, IOException;

    List<ItemStockStorageLocationDto> getAllData() throws SQLException, IOException;

    int deleteIssl(int id) throws SQLException, IOException;
    ;
}
