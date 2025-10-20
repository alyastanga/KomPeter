/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.monitoring;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Hanz Mapua
 */
public interface SalesDao {
    public List<RevenueDto> getRevenue() throws SQLException;

    public List<RevenueDto> getRevenue(Timestamp date, FromTo fromto) throws SQLException;

    public List<RevenueDto> getRevenue(Timestamp from, Timestamp to) throws SQLException;

    public List<ExpensesDto> getExpenses() throws SQLException;

    public List<ExpensesDto> getExpenses(Timestamp date, FromTo fromto) throws SQLException;

    public List<ExpensesDto> getExpenses(Timestamp from, Timestamp to) throws SQLException;

    public List<ProfitDto> getProfit() throws SQLException;

    public List<ProfitDto> getProfit(Timestamp date, FromTo fromto) throws SQLException;

    public List<ProfitDto> getProfit(Timestamp from, Timestamp to) throws SQLException;

    public List<Top10SellingItemsDto> getTop10SellingItems() throws SQLException;

    public List<Top10SellingItemsDto> getTop10SellingItems(Timestamp date, FromTo fromto)
            throws SQLException;

    public List<Top10SellingItemsDto> getTop10SellingItems(Timestamp from, Timestamp to)
            throws SQLException;
}
