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
import com.github.ragudos.kompeter.database.dao.DateUtils;
import com.github.ragudos.kompeter.database.dao.monitoring.InventoryDao;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

/**
 * @author Hanz Mapua
 */
public class SqliteInventoryDao implements InventoryDao {
    @Override
    public List<InventoryCountDto> getInventoryCount() throws SQLException {
        final List<InventoryCountDto> results = new ArrayList<>();
        String query;

        try {
            query = SqliteQueryLoader.getInstance().get("inventory_count_range", // filename without .sql
                    "items", // folder name under /select/
                    AbstractSqlQueryLoader.SqlQueryType.SELECT);
        } catch (final IOException e) {
            throw new SQLException("Failed to load SQL file for inventory count", e);
        }

        try (Connection conn = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    final InventoryCountDto dto = new InventoryCountDto(DateUtils.safeGetTimestamp(rs, "day"),
                            rs.getInt("total_quantity"), rs.getInt("total_quantity_added"),
                            rs.getInt("total_quantity_before"));
                    results.add(dto);
                }
            }
        }

        return results;
    }

}
