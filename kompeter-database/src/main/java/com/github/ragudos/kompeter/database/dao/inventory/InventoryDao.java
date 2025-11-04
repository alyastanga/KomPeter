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

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;

public interface InventoryDao {
    InventoryMetadataDto[] getAllInventoryItems(@NotNull Connection conn) throws SQLException, IOException;
}
