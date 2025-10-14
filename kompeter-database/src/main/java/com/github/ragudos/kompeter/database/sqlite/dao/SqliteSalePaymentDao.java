package com.github.ragudos.kompeter.database.sqlite.dao;

import com.github.ragudos.kompeter.database.dao.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.SaleItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public class SqliteSalePaymentDao implements SalePaymentDao {

    @Override
    public List<SaleItemStockDto> getRevenue() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getRevenue(Timestamp from) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
