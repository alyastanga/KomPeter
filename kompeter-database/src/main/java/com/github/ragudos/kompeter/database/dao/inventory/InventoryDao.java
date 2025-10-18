/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface InventoryDao {
    List<InventoryMetadataDto> getAllData() throws SQLException, IOException;

    List<InventoryMetadataDto> getAllData(String search) throws SQLException, IOException;
}
