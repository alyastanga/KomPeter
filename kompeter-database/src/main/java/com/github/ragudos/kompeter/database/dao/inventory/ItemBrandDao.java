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
import java.util.Optional;

import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;

public interface ItemBrandDao {
    // READ
    List<ItemBrandDto> getAllBrands() throws SQLException, IOException;

    Optional<ItemBrandDto> getBrandById(int id) throws SQLException, IOException;

    // UPDATE
    // DELETE

    // CREATE
    int insertItemBrand(String name, String description) throws SQLException, IOException;
}
