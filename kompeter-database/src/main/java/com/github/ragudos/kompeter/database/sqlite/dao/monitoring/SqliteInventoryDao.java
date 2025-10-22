/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.monitoring;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.DateUtils;
import com.github.ragudos.kompeter.database.dao.monitoring.InventoryDao;
import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

/**
 * @author Hanz Mapua
 */
public class SqliteInventoryDao implements InventoryDao {

    @Override
    public List<InventoryCountDto> getInventoryCount() throws SQLException {
        return getInventoryCount((Timestamp) null, (Timestamp) null);
    }

    @Override
    public List<InventoryCountDto> getInventoryCount(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getInventoryCount(date, (Timestamp) null);
        } else {
            return getInventoryCount((Timestamp) null, date);
        }
    }

    @Override
    public List<InventoryCountDto> getInventoryCount(Timestamp from, Timestamp to) throws SQLException {
        List<InventoryCountDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "inventory_count_all";
        } else if (to == null && from != null) { // if from is not null, put it in sql, if to is null, to is 'now' date
            // in sql
            sqlFileName = "inventory_count_from";
        } else if (from == null && to != null) { // if from is null, from is the latest date, if to is not null, put it
            // in sql
            sqlFileName = "inventory_count_to";
        } else {
            sqlFileName = "inventory_count_range"; // if both not null, put both in sql
        }

        String query;
        try {
            query = SqliteQueryLoader.getInstance().get(sqlFileName, // filename without .sql
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
                    InventoryCountDto dto = new InventoryCountDto(DateUtils.safeGetTimestamp(rs, "date"),
                            rs.getInt("total_inventory"), rs.getInt("total_purchased"), rs.getInt("total_sold"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<InventoryValueDto> getInventoryValue() throws SQLException {
        return getInventoryValue((Timestamp) null, (Timestamp) null);
    }

    public List<InventoryValueDto> getInventoryValue(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            return getInventoryValue(date, (Timestamp) null);
        } else {
            return getInventoryValue((Timestamp) null, date);
        }
    }

    @Override
    public List<InventoryValueDto> getInventoryValue(Timestamp from, Timestamp to) throws SQLException {
        List<InventoryValueDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) { // if both null, from is the latest date and to is the now date in sql
            sqlFileName = "inventory_value_all";
        } else if (to == null && from != null) { // if from is not null, put it in sql, if to is null, to is 'now' date
            // in sql
            sqlFileName = "inventory_value_from";
        } else if (from == null && to != null) { // if from is null, from is the latest date, if to is not null, put it
            // in sql
            sqlFileName = "inventory_value_to";
        } else {
            sqlFileName = "inventory_value_range"; // if both not null, put both in sql
        }

        String query;
        try {
            query = SqliteQueryLoader.getInstance().get(sqlFileName, // filename without .sql
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
                    InventoryValueDto dto = new InventoryValueDto(DateUtils.safeGetTimestamp(rs, "date"),
                            rs.getFloat("total_inventory_value"), rs.getFloat("total_purchased_value"),
                            rs.getFloat("total_sold_value"));
                    results.add(dto);
                }
            }
        }

        return results;
    }
}
