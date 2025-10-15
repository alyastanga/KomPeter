/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public interface PurchaseItemStockDao {
    public List<PurchaseItemStockDto> getPurchaseStock();

    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from);

    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from, Timestamp to);
}
