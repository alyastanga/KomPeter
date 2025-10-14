package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public interface PurchasePaymentDao {
    public List<PurchaseItemStockDto> getExpenses();

    public List<PurchaseItemStockDto> getExpenses(Timestamp from);

    public List<PurchaseItemStockDto> getExpenses(Timestamp from, Timestamp to);
}
