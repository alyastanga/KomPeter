/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.ItemDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ItemDao {
    // CREATE
    // READ
    List<ItemDto> getAllItems() throws SQLException, IOException;

    List<ItemDto> getItemsById(int id) throws SQLException, IOException;

    // UPDATE
    // DELETE
}
