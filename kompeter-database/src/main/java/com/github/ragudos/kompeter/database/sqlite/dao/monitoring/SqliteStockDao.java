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
import com.github.ragudos.kompeter.database.dao.monitoring.StockDao;
import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.OnHandUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.PurchaseUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.SalesUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10OldItemsDto;
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

/**
 * @author Hanz Mapua
 */
public class SqliteStockDao implements StockDao {
    @Override
    public List<PurchaseUnitDto> getPurchaseUnit() throws SQLException {
        return getPurchaseUnit((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getPurchaseUnit(date, (Timestamp) null);
        } else {
            return getPurchaseUnit((Timestamp) null, date);
        }
    }

    @Override
    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from, Timestamp to) throws SQLException {
        List<PurchaseUnitDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "purchase_unit_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "purchase_unit_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "purchase_unit_to";
        } else {
            sqlFileName = "purchase_unit_range"; // if both not null, put both in sql
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
                    PurchaseUnitDto dto =
                            new PurchaseUnitDto(
                                    DateUtils.safeGetTimestamp(rs, "date"),
                                    rs.getInt("total_purchase_unit"),
                                    rs.getInt("cumulative_purchased_units"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<SalesUnitDto> getSalesUnit() throws SQLException {
        return getSalesUnit((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<SalesUnitDto> getSalesUnit(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getSalesUnit(date, (Timestamp) null);
        } else {
            return getSalesUnit((Timestamp) null, date);
        }
    }

    @Override
    public List<SalesUnitDto> getSalesUnit(Timestamp from, Timestamp to) throws SQLException {
        List<SalesUnitDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "sales_unit_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "sales_unit_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "sales_unit_to";
        } else {
            sqlFileName = "sales_unit_range"; // if both not null, put both in sql
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
                    SalesUnitDto dto =
                            new SalesUnitDto(
                                    DateUtils.safeGetTimestamp(rs, "date"),
                                    rs.getInt("total_sales_unit"),
                                    rs.getInt("cumulative_sales_units"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<OnHandUnitDto> getOnHandUnit() throws SQLException {
        return getOnHandUnit((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<OnHandUnitDto> getOnHandUnit(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getOnHandUnit(date, (Timestamp) null);
        } else {
            return getOnHandUnit((Timestamp) null, date);
        }
    }

    @Override
    public List<OnHandUnitDto> getOnHandUnit(Timestamp from, Timestamp to) throws SQLException {
        List<OnHandUnitDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null
                && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "onhand_unit_all";
        } else if (to == null
                && from
                        != null) { // if from is not null, put it in sql, if to is null, to is 'now' date in sql
            sqlFileName = "onhand_unit_from";
        } else if (from == null
                && to
                        != null) { // if from is null, from is the latest date, if to is not null, put it in sql
            sqlFileName = "onhand_unit_to";
        } else {
            sqlFileName = "onhand_unit_range"; // if both not null, put both in sql
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
                    OnHandUnitDto dto =
                            new OnHandUnitDto(
                                    DateUtils.safeGetTimestamp(rs, "date"),
                                    rs.getInt("total_purchased"),
                                    rs.getInt("total_sold"),
                                    rs.getInt("total_on_hand"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<Top10LowStockItemsDto> getTop10LowStockItems() throws SQLException {
        List<Top10LowStockItemsDto> results = new ArrayList<>();

        String sqlFileName = "top_10_low_stock_items";
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

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Top10LowStockItemsDto dto =
                            new Top10LowStockItemsDto(
                                    rs.getString("item_name"),
                                    rs.getString("brand_name"),
                                    rs.getString("category_name"),
                                    rs.getInt("quantity"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<Top10OldItemsDto> getTop10OldItems() throws SQLException {
        List<Top10OldItemsDto> results = new ArrayList<>();

        String sqlFileName = "top_10_old_items";
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

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Top10OldItemsDto dto =
                            new Top10OldItemsDto(
                                    rs.getString("item_name"),
                                    rs.getString("brand_name"),
                                    rs.getString("category_name"),
                                    rs.getInt("total_quantity"),
                                    DateUtils.safeGetTimestamp(rs, "stocked_date"),
                                    rs.getInt("days_in_stock"));
                    results.add(dto);
                }
            }
        }

        return results;
    }
}
