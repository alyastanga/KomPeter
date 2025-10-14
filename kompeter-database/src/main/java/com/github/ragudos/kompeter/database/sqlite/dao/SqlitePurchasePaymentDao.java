package com.github.ragudos.kompeter.database.sqlite.dao;

import com.github.ragudos.kompeter.database.dao.PurchasePaymentDao;
import com.github.ragudos.kompeter.database.dto.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public class SqlitePurchasePaymentDao implements PurchasePaymentDao {

    @Override
    public List<PurchaseItemStockDto> getExpenses() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseItemStockDto> getExpenses(Timestamp from) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseItemStockDto> getExpenses(Timestamp from, Timestamp to) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
