/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.monitoring;

import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.TopSellingDto;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Hanz Mapua
 */
public interface SalesDao {
    public List<RevenueDto> getRevenue();

    public List<RevenueDto> getRevenue(Timestamp from);

    public List<RevenueDto> getRevenue(Timestamp from, Timestamp to);

    public List<ExpensesDto> getExpenses();

    public List<ExpensesDto> getExpenses(Timestamp from);

    public List<ExpensesDto> getExpenses(Timestamp from, Timestamp to);

    public List<ProfitDto> getProfit();

    public List<ProfitDto> getProfit(Timestamp from);

    public List<ProfitDto> getProfit(Timestamp from, Timestamp to);

    public List<TopSellingDto> getTopSellingItems();

    public List<TopSellingDto> getTopSellingItems(Timestamp from);

    public List<TopSellingDto> getTopSellingItems(Timestamp from, Timestamp to);
}
