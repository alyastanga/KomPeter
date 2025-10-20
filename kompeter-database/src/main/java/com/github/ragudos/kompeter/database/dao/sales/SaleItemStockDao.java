/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.sales;

import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface SaleItemStockDao {

    public List<SaleItemStockDto> getSalesUnitById(int _saleItemStockId, int _saleId)
            throws SQLException;

    public List<SaleItemStockDto> getSalesUnitFrom(Timestamp from) throws SQLException;

    public List<SaleItemStockDto> getSalesUnitByRange(Timestamp from, Timestamp to)
            throws SQLException;

    public List<SaleItemStockDto> getTopSellingItems() throws SQLException;

    public List<SaleItemStockDto> getTopSellingItemsFrom(Timestamp from) throws SQLException;

    public List<SaleItemStockDto> getTopSellingItemsByRange(Timestamp from, Timestamp to)
            throws SQLException;
}
