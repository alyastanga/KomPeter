/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface PurchaseItemStockDao {
    //CREATE
    void insertPurchaseItemStock(int purchaseId,int itemStockId, int qty_ordered, int qty_received, double unit_cost_php) throws SQLException, IOException;
    //READ
    public List<PurchaseItemStockDto> getPurchaseStock() throws SQLException, IOException;

    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from) throws SQLException, IOException;

    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from, Timestamp to) throws SQLException, IOException;
}
