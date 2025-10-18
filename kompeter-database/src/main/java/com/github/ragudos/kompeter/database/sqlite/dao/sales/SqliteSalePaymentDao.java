/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SqliteSalePaymentDao implements SalePaymentDao {

    @Override
    public List<SaleItemStockDto> getRevenue() {
        List<SaleItemStockDto> list = new ArrayList<>();

        String sql =
                """

                """;

        return null;
    }

    @Override
    public List<SaleItemStockDto> getRevenue(Timestamp from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SaleItemStockDto> getRevenue(Timestamp from, Timestamp to) {
        // TODO Auto-generated method stub
        return null;
    }
}
