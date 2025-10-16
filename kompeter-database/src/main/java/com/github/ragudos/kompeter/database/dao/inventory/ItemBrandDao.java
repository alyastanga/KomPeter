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

public interface ItemBrandDao {
    //CREATE
    void insertItemBrand(String name, String description) throws SQLException, IOException;
    //READ
    //UPDATE
    //DELETE
}

