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
import java.util.Optional;

import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;

public interface ItemBrandDao {
    ItemBrandDto[] getAllBrands(Connection conn) throws SQLException, IOException;

    Optional<ItemBrandDto> getBrandById(Connection conn, int _itemBrandId) throws SQLException, IOException;

    int insertItemBrand(Connection conn, String name, String description) throws SQLException, IOException;
}
