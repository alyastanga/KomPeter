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
import java.util.List;

import com.github.ragudos.kompeter.database.dto.inventory.ItemRestockDto;

public interface ItemRestockDao {
    // DELETE
    int deleteRestockById(int id) throws SQLException, IOException;

    // READ
    List<ItemRestockDto> getAllData() throws SQLException, IOException;

    // CREATE
    int insertItemRestock(int itemStockId, int qty_before, int qty_after, int qty_added)
            throws SQLException, IOException;

    int updateRestockQtyAddedById(int qtyAdded, int id) throws SQLException, IOException;

    // UPDATE
    int updateRestockQtyAfterById(int qtyAfter, int id) throws SQLException, IOException;

    int updateRestockQtyBeforeById(int qtyBefore, int id) throws SQLException, IOException;
}
