/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.monitoring;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.monitoring.InventoryDao;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;
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

/**
 * @author Hanz Mapua
 */
public class SqliteInventoryDao implements InventoryDao {

    @Override
    public List<InventoryCountDto> getInventoryCount() throws SQLException {
        return getInventoryCount(null, null);
    }

    @Override
    public List<InventoryCountDto> getInventoryCount(Timestamp from) throws SQLException {
        return getInventoryCount(from, null);
    }

    @Override
    public List<InventoryCountDto> getInventoryCount(Timestamp from, Timestamp to)
            throws SQLException {
        List<InventoryCountDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) {
            sqlFileName = "inventory_count_all";
        } else if (to == null) {
            sqlFileName = "inventory_count_to";
        } else {
            sqlFileName = "inventory_count_range";
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
                    InventoryCountDto dto =
                            new InventoryCountDto(
                                    rs.getTimestamp("date"),
                                    rs.getInt("total_inventory"),
                                    rs.getInt("total_purchased"),
                                    rs.getInt("total_sold"));
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }

    @Override
    public List<InventoryValueDto> getInventoryValue() throws SQLException {
        return getInventoryValue(null, null);
    }

    @Override
    public List<InventoryValueDto> getInventoryValue(Timestamp from) throws SQLException {
        return getInventoryValue(from, null);
    }

    @Override
    public List<InventoryValueDto> getInventoryValue(Timestamp from, Timestamp to)
            throws SQLException {
        List<InventoryValueDto> results = new ArrayList<>();

        String sqlFileName;
        if (from == null && to == null) {
            sqlFileName = "inventory_value_all";
        } else if (to == null) {
            sqlFileName = "inventory_value_to";
        } else {
            sqlFileName = "inventory_value_range";
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
                    InventoryValueDto dto =
                            new InventoryValueDto(
                                    rs.getTimestamp("date"),
                                    rs.getFloat("total_inventory_value"),
                                    rs.getFloat("total_purchased_value"),
                                    rs.getFloat("total_sales_value"));
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }
}
