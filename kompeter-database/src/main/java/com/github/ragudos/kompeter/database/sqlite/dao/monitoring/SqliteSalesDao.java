/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.monitoring;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.DateUtils;
import com.github.ragudos.kompeter.database.dao.monitoring.SalesDao;
import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
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
        return getRevenue((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<RevenueDto> getRevenue(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getRevenue(date, (Timestamp) null);
        } else {
            return getRevenue((Timestamp) null, date);
        }
    }

    @Override
    public List<RevenueDto> getRevenue(Timestamp from, Timestamp to) throws SQLException {
        List<RevenueDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "revenue_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "revenue_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "revenue_to";
        } else {
            sqlFileName = "revenue_range"; // if both not null, put both in sql
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
                stmt.setString(paramIndex++, from.toString());
            }
            if (to != null) {
                stmt.setString(paramIndex, to.toString());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RevenueDto dto =
                            new RevenueDto(DateUtils.safeGetTimestamp(rs, "date"), rs.getFloat("total_revenue"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<ExpensesDto> getExpenses() throws SQLException {
        return getExpenses((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<ExpensesDto> getExpenses(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getExpenses(date, (Timestamp) null);
        } else {
            return getExpenses((Timestamp) null, date);
        }
    }

    @Override
    public List<ExpensesDto> getExpenses(Timestamp from, Timestamp to) throws SQLException {
        List<ExpensesDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "expenses_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "expenses_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "expenses_to";
        } else {
            sqlFileName = "expenses_range"; // if both not null, put both in sql
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
                stmt.setString(paramIndex++, from.toString());
            }
            if (to != null) {
                stmt.setString(paramIndex, to.toString());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ExpensesDto dto =
                            new ExpensesDto(DateUtils.safeGetTimestamp(rs, "date"), rs.getFloat("total_expense"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<ProfitDto> getProfit() throws SQLException {
        return getProfit((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<ProfitDto> getProfit(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getProfit(date, (Timestamp) null);
        } else {
            return getProfit((Timestamp) null, date);
        }
    }

    @Override
    public List<ProfitDto> getProfit(Timestamp from, Timestamp to) throws SQLException {
        List<ProfitDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "profit_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "profit_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "profit_to";
        } else {
            sqlFileName = "profit_range"; // if both not null, put both in sql
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
                stmt.setString(paramIndex++, from.toString());
            }
            if (to != null) {
                stmt.setString(paramIndex, to.toString());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProfitDto dto =
                            new ProfitDto(
                                    DateUtils.safeGetTimestamp(rs, "date"),
                                    rs.getFloat("total_profit"),
                                    rs.getFloat("total_revenue"),
                                    rs.getFloat("total_expense"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<Top10SellingItemsDto> getTop10SellingItems() throws SQLException {
        return getTop10SellingItems((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<Top10SellingItemsDto> getTop10SellingItems(Timestamp date, FromTo fromto)
            throws SQLException {
        if (fromto == FromTo.FROM) {
            return getTop10SellingItems(date, (Timestamp) null);
        } else {
            return getTop10SellingItems((Timestamp) null, date);
        }
    }

    @Override
    public List<Top10SellingItemsDto> getTop10SellingItems(Timestamp from, Timestamp to)
            throws SQLException {
        List<Top10SellingItemsDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "top_10_selling_items_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "top_10_selling_items_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "top_10_selling_items_to";
        } else {
            sqlFileName = "top_10_selling_items_range"; // if both not null, put both in sql
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
                stmt.setString(paramIndex++, from.toString());
            }
            if (to != null) {
                stmt.setString(paramIndex, to.toString());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Top10SellingItemsDto dto =
                            new Top10SellingItemsDto(
                                    rs.getString("item_name"),
                                    rs.getString("brand_name"),
                                    rs.getString("category_name"),
                                    rs.getInt("total_sold"),
                                    rs.getFloat("total_revenue"));
                    results.add(dto);
                }
            }
        }

        return results;
    }
}
