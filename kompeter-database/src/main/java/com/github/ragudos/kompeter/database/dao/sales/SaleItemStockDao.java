/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.sales;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;

public interface SaleItemStockDao {

    List<SaleItemStockDto> getSalesUnitById(int _saleItemStockId, int _saleId) throws SQLException;

    List<SaleItemStockDto> getSalesUnitByRange(Timestamp from, Timestamp to) throws SQLException;

    List<SaleItemStockDto> getSalesUnitFrom(Timestamp from) throws SQLException;

    List<SaleItemStockDto> getTopSellingItems() throws SQLException;

    List<SaleItemStockDto> getTopSellingItemsByRange(Timestamp from, Timestamp to) throws SQLException;

    List<SaleItemStockDto> getTopSellingItemsFrom(Timestamp from) throws SQLException;
}
