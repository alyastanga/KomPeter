package com.github.ragudos.kompeter.database.dao.sales;

import java.sql.Timestamp;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;

public interface SalePaymentDao {

	public List<SaleItemStockDto> getRevenue();

	public List<SaleItemStockDto> getRevenue(Timestamp from);

	public List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to);
}
