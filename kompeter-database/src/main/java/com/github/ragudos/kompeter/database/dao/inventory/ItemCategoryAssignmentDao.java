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

public interface ItemCategoryAssignmentDao {
    // CREATE
    int setItemCategory(int itemId, int itemCategoryId) throws SQLException, IOException;
    // READ
    // UPDATE
    // DELETE
}
