/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.seeder.SqliteSeeder;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.Collections;

public class MonitoringInventoryService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);

    
    private final SqliteInventoryDao inventoryDAO;

    public MonitoringInventoryService(SqliteInventoryDao inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

  public List<InventoryCountDto> getInventoryCountReport(Timestamp from, Timestamp to) {
        try {
            return inventoryDAO.getInventoryCount(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory count report (range)", e);
            return Collections.emptyList();
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public List<InventoryValueDto> getInventoryValueReport(Timestamp from, Timestamp to) {
        try {
            return inventoryDAO.getInventoryValue(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory value report (range)", e);
            return Collections.emptyList();
        }
    }
    
    // Shared printer logic
    private <T> void printResults(List<T> results) {
        if (results == null || results.isEmpty()) {
            LOGGER.log(Level.WARNING, "No inventory records found.");
            return;
        }

        for (T dto : results) {
            System.out.println(dto);
        }
    }
}
