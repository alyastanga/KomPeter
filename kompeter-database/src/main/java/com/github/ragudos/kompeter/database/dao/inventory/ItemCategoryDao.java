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
import java.util.Optional;

import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;

public interface ItemCategoryDao {
    List<ItemCategoryDto> getAllCategories(Connection conn) throws SQLException, IOException;

    Optional<ItemCategoryDto> getCategoryById(Connection conn, int id) throws SQLException, IOException;

    int insertItemCategory(Connection conn, String name, String description) throws SQLException, IOException;
}
