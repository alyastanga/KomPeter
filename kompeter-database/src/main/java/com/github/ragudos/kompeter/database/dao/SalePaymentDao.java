package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.SaleItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public interface SalePaymentDao {
    public List<SaleItemStockDto> getRevenue();

    public List<SaleItemStockDto> getRevenue(Timestamp from);

    public List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to);
}
