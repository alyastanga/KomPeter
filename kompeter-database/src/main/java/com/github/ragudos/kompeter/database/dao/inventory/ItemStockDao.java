/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.ItemStockDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemStockDao {
    // CREATE
    int insertItemStock(int itemId, int itemBrandId, BigDecimal unit_price, int min_qty)
            throws SQLException, IOException;

    // READ
    List<ItemStockDto> getAllData() throws SQLException, IOException;

    Optional<ItemStockDto> getItemStockById(int id) throws SQLException, IOException;

    // UPDATE
    int updateItemBrandById(int brandID, int id) throws SQLException, IOException;

    int updateItemMinimumQtyById(int minimumQty, int id) throws SQLException, IOException;

    int updateItemUnitPriceById(BigDecimal unitPrice, int id) throws SQLException, IOException;

    // DELETE
    int deleteItemStockById(int id) throws SQLException, IOException;
}
