/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;

public interface ItemStockDao {
    // CREATE
    int insertItemStock(Connection conn, int itemId, int itemBrandId, BigDecimal unit_price, int min_qty)
            throws SQLException, IOException;

    void setItemStocksStatusByName(Connection conn, String name, ItemStatus status) throws SQLException, IOException;

    int updateItemMinimumQtyById(Connection conn, int id, int qty) throws SQLException, IOException;

    int updateItemUnitPriceById(Connection conn, int id, BigDecimal unitPricePhp) throws SQLException, IOException;
}
