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

/**
 * @author Peter M. Dela Cruz
 */
public interface StorageLocationDao {
    int insertStorageLocation(String name, String description) throws SQLException, IOException;

    int deleteStorageLocationById(int id) throws SQLException, IOException;
}
