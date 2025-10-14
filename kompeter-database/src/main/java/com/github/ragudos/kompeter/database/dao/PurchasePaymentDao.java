package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public interface PurchasePaymentDao {

    public List<PurchaseItemStockDto> getExpenses();

    public List<PurchaseItemStockDto> getExpenses(Timestamp from);

    public List<PurchaseItemStockDto> getExpenses(Timestamp from, Timestamp to);
}
