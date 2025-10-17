/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.monitoring;

import com.github.ragudos.kompeter.database.dao.monitoring.SalesDao;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.TopSellingDto;
import java.sql.Timestamp;
import java.util.List;

public class SqliteSalesDao implements SalesDao {
    @Override
    public List<RevenueDto> getRevenue() {
        return null;
    }

    @Override
    public List<RevenueDto> getRevenue(Timestamp from) {
        return null;
    }

    @Override
    public List<RevenueDto> getRevenue(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<ExpensesDto> getExpenses() {
        return null;
    }

    @Override
    public List<ExpensesDto> getExpenses(Timestamp from) {
        return null;
    }

    @Override
    public List<ExpensesDto> getExpenses(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<ProfitDto> getProfit() {
        return null;
    }

    @Override
    public List<ProfitDto> getProfit(Timestamp from) {
        return null;
    }

    @Override
    public List<ProfitDto> getProfit(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<TopSellingDto> getTopSellingItems() {
        return null;
    }

    @Override
    public List<TopSellingDto> getTopSellingItems(Timestamp from) {
        return null;
    }

    @Override
    public List<TopSellingDto> getTopSellingItems(Timestamp from, Timestamp to) {
        return null;
    }
}
