/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.monitoring;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.monitoring.SalesDao;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.TopSellingDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SqliteSalesDao implements SalesDao {

    @Override
    public List<RevenueDto> getRevenue() throws SQLException {
        return getRevenue(null, null);
    }

    @Override
    public List<RevenueDto> getRevenue(Timestamp from) throws SQLException {
        return getRevenue(from, null);
    }

    @Override
    public List<RevenueDto> getRevenue(Timestamp from, Timestamp to) throws SQLException {
        List<RevenueDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) {
            sqlFileName = "revenue_all";
        } else if (to == null) {
            sqlFileName = "revenue_to";
        } else {
            sqlFileName = "revenue_range";
        }

        String query;
        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(
                                    sqlFileName, // filename without .sql
                                    "items", // folder name under /select/
                                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (from != null) {
                stmt.setTimestamp(paramIndex++, from);
            }
            if (to != null) {
                stmt.setTimestamp(paramIndex, to);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RevenueDto dto = new RevenueDto(rs.getTimestamp("date"), rs.getFloat("total_revenue"));
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }

    @Override
    public List<ExpensesDto> getExpenses() throws SQLException {
        return getExpenses(null, null);
    }

    @Override
    public List<ExpensesDto> getExpenses(Timestamp from) throws SQLException {
        return getExpenses(from, null);
    }

    @Override
    public List<ExpensesDto> getExpenses(Timestamp from, Timestamp to) throws SQLException {
        List<ExpensesDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) {
            sqlFileName = "expenses_all";
        } else if (to == null) {
            sqlFileName = "expenses_to";
        } else {
            sqlFileName = "expenses_range";
        }

        String query;
        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(
                                    sqlFileName, // filename without .sql
                                    "items", // folder name under /select/
                                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (from != null) {
                stmt.setTimestamp(paramIndex++, from);
            }
            if (to != null) {
                stmt.setTimestamp(paramIndex, to);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExpensesDto dto = new ExpensesDto(rs.getTimestamp("date"), rs.getFloat("total_expense"));
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }

    @Override
    public List<ProfitDto> getProfit() throws SQLException {
        return getProfit(null, null);
    }

    @Override
    public List<ProfitDto> getProfit(Timestamp from) throws SQLException {
        return getProfit(from, null);
    }

    @Override
    public List<ProfitDto> getProfit(Timestamp from, Timestamp to) throws SQLException {
        List<ProfitDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) {
            sqlFileName = "profit_all";
        } else if (to == null) {
            sqlFileName = "profit_to";
        } else {
            sqlFileName = "profit_range";
        }

        String query;
        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(
                                    sqlFileName, // filename without .sql
                                    "items", // folder name under /select/
                                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (from != null) {
                stmt.setTimestamp(paramIndex++, from);
            }
            if (to != null) {
                stmt.setTimestamp(paramIndex, to);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProfitDto dto =
                            new ProfitDto(
                                    rs.getTimestamp("date"),
                                    rs.getFloat("total_profit"),
                                    rs.getFloat("total_revenue"),
                                    rs.getFloat("total_expenses"));
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }

    @Override
    public List<TopSellingDto> getTopSellingItems() throws SQLException {
        return getTopSellingItems(null, null);
    }

    @Override
    public List<TopSellingDto> getTopSellingItems(Timestamp from) throws SQLException {
        return getTopSellingItems(from, null);
    }

    @Override
    public List<TopSellingDto> getTopSellingItems(Timestamp from, Timestamp to) throws SQLException {
        List<TopSellingDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) {
            sqlFileName = "topsellingitems_all";
        } else if (to == null) {
            sqlFileName = "topsellingitems_to";
        } else {
            sqlFileName = "topsellingitems_range";
        }

        String query;
        try {
            query =
                    SqliteQueryLoader.getInstance()
                            .get(
                                    sqlFileName, // filename without .sql
                                    "items", // folder name under /select/
                                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (from != null) {
                stmt.setTimestamp(paramIndex++, from);
            }
            if (to != null) {
                stmt.setTimestamp(paramIndex, to);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TopSellingDto dto =
                            new TopSellingDto(
                                    rs.getString("item_name"),
                                    rs.getString("brand_name"),
                                    rs.getString("category_name"),
                                    rs.getInt("total_sold"),
                                    rs.getFloat("total_revenue"));
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }
}
