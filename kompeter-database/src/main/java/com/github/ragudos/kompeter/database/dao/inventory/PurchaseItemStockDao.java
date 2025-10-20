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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PurchaseItemStockDao {
    // CREATE
    int insertPurchaseItemStock(
            int purchaseId, int itemStockId, int qty_ordered, int qty_received, BigDecimal unit_cost_php)
            throws SQLException, IOException;

    List<PurchaseItemStockDto> getAllData() throws SQLException, IOException;

    List<PurchaseItemStockDto> getAllDataByPurchaseId(int purchaseId)
            throws SQLException, IOException;

    BigDecimal getPurchaseLineCost(int purchaseId, int itemStockId) throws SQLException, IOException;

    BigDecimal getPurchaseTotalCost(int purchaseId) throws SQLException, IOException;
}
