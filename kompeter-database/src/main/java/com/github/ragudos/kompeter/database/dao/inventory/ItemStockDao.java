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

public interface ItemStockDao {
    //CREATE
    void insertItemStock(int itemId, int itemBrandId, double unit_price, int min_qty) throws SQLException, IOException;
    //READ
    //UPDATE
    //DELETE
}
