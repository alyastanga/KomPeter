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

import com.github.ragudos.kompeter.database.dto.inventory.StorageLocationDto;

/**
 * @author Peter M. Dela Cruz
 */
public interface StorageLocationDao {

    StorageLocationDto[] getAllStorageLocations(Connection conn) throws SQLException, IOException;

    int insertStorageLocation(String name, String description) throws SQLException, IOException;
}
