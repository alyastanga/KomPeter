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

public interface ItemRestockDao {
    //CREATE
    void insertItemRestock(int itemStockId, int qty_before, int qty_after, int qty_added) throws SQLException, IOException;
    //READ
    //UPDATE
    //DELETE
}
