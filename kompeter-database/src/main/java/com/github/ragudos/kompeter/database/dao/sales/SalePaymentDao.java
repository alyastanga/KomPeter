/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.sales;

import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;
import com.github.ragudos.kompeter.database.dto.sales.SalePaymentDto;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface SalePaymentDao {

    public int savePayment(SalePaymentDto salePayment) throws SQLException;

    public List<SaleItemStockDto> getRevenue();

    public List<SaleItemStockDto> getRevenue(Timestamp from);

    public List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to);
}
