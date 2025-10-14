package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.SaleItemStockDto;
import java.sql.Timestamp;
import java.util.List;

public interface SaleItemStockDao {
    public List<SaleItemStockDto> getSalesUnit();

    public List<SaleItemStockDto> getSalesUnit(Timestamp from);

    public List<SaleItemStockDto> getSalesUnit(Timestamp from, Timestamp to);

    public List<SaleItemStockDto> getTopSellingItems();

    public List<SaleItemStockDto> getTopSellingItems(Timestamp from);

    public List<SaleItemStockDto> getTopSellingItems(Timestamp from, Timestamp to);
}
