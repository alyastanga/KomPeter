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
import java.util.ArrayList;
import java.util.List;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.monitoring.SalesDao;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteSalesDao implements SalesDao {
    @Override
    public List<RevenueDto> getRevenue() throws SQLException {
        final List<RevenueDto> results = new ArrayList<>();

        String query;
        try {
            query = SqliteQueryLoader.getInstance().get("revenue_range", // filename without .sql
                    "items", // folder name under /select/
                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (final IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    final RevenueDto dto = new RevenueDto(rs.getTimestamp("day"),
                            rs.getBigDecimal("total_item_revenue"), rs.getBigDecimal("total_paid"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

    @Override
    public List<Top10SellingItemsDto> getTop10SellingItems() throws SQLException {
        final List<Top10SellingItemsDto> results = new ArrayList<>();

        String query;
        try {
            query = SqliteQueryLoader.getInstance().get("top_10_selling_items_range", // filename without .sql
                    "items", // folder name under /select/
                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (final IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    final Top10SellingItemsDto dto = new Top10SellingItemsDto(rs.getString("item_name"),
                            rs.getString("brand_name"), rs.getString("category_name"), rs.getInt("total_sold"),
                            rs.getFloat("total_revenue"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

}
