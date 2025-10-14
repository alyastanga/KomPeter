package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public interface PurchaseItemStockDao {
    public List<PurchaseItemStockDto> getPurchaseStock();

    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from);

    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from, Timestamp to);
}
