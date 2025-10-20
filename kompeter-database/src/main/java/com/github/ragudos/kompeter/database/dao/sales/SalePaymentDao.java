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
import com.github.ragudos.kompeter.database.dto.sales.SalePaymentDto;

public interface SalePaymentDao {

    List<SaleItemStockDto> getRevenue();

    List<SaleItemStockDto> getRevenue(Timestamp from);

    List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to);

    int savePayment(SalePaymentDto salePayment) throws SQLException;
}
