package com.github.ragudos.kompeter.database.sqlite.dao;

import com.github.ragudos.kompeter.database.dao.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dto.SaleItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public class SqliteSaleItemStockDao implements SaleItemStockDao {

    @Override
    public List<SaleItemStockDto> getSalesUnit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getSalesUnit(Timestamp from) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getSalesUnit(Timestamp from, Timestamp to) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getTopSellingItems() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getTopSellingItems(Timestamp from) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getTopSellingItems(Timestamp from, Timestamp to) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
