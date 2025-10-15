/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.dao.inventory.PurchasePaymentDao;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public class SqlitePurchasePaymentDao implements PurchasePaymentDao {

    @Override
    public List<PurchaseItemStockDto> getExpenses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseItemStockDto> getExpenses(Timestamp from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseItemStockDto> getExpenses(Timestamp from, Timestamp to) {
        // TODO Auto-generated method stub
        return null;
    }
}
