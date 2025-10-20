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
import java.util.Optional;

public interface ItemDao {
    // CREATE
    int insertItem(String name, String description) throws SQLException, IOException;

    // READ
    List<ItemDto> getAllItems() throws SQLException, IOException;

    Optional<ItemDto> getItemsById(int id) throws SQLException, IOException;

    // UPDATE
    int updateItemNameById(String name, int id) throws SQLException, IOException;

    // DELETE
    int deleteItemById(int id) throws SQLException, IOException;
}
