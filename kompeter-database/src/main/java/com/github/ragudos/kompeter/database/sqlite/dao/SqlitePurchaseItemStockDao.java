package com.github.ragudos.kompeter.database.sqlite.dao;

import com.github.ragudos.kompeter.database.dao.PurchaseItemStockDao;
import com.github.ragudos.kompeter.database.dto.PurchaseItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public class SqlitePurchaseItemStockDao implements PurchaseItemStockDao {

    @Override
    public List<PurchaseItemStockDto> getPurchaseStock() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PurchaseItemStockDto> getPurchaseStock(Timestamp from, Timestamp to) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
