/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.monitoring;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.TopSellingDto;

/**
 * @author Hanz Mapua
 */
public interface SalesDao {
    List<ExpensesDto> getExpenses() throws SQLException;

    List<ExpensesDto> getExpenses(Timestamp from) throws SQLException;

    List<ExpensesDto> getExpenses(Timestamp from, Timestamp to) throws SQLException;

    List<ProfitDto> getProfit() throws SQLException;

    List<ProfitDto> getProfit(Timestamp from) throws SQLException;

    List<ProfitDto> getProfit(Timestamp from, Timestamp to) throws SQLException;

    List<RevenueDto> getRevenue() throws SQLException;

    List<RevenueDto> getRevenue(Timestamp from) throws SQLException;

    List<RevenueDto> getRevenue(Timestamp from, Timestamp to) throws SQLException;

    List<TopSellingDto> getTopSellingItems() throws SQLException;

    List<TopSellingDto> getTopSellingItems(Timestamp from) throws SQLException;

    List<TopSellingDto> getTopSellingItems(Timestamp from, Timestamp to) throws SQLException;
}
