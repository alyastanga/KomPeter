/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemCategoryDao {
    // CREATE
    int insertItemCategory(String name, String description) throws SQLException, IOException;

    // READ
    List<ItemCategoryDto> getAllCategories() throws SQLException, IOException;

    Optional<ItemCategoryDto> getCategoryById(int id) throws SQLException, IOException;
    // UPDATE
    // DELETE
}
