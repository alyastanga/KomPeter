/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class MonitoringInventoryService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);

    private final SqliteInventoryDao inventoryDAO;

    public MonitoringInventoryService(final SqliteInventoryDao inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public List<InventoryCountDto> getInventoryCountReport(final Timestamp from, final Timestamp to) {
        try {
            return inventoryDAO.getInventoryCount(from, to);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory count report (range)", e);
            return Collections.emptyList();
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public List<InventoryValueDto> getInventoryValueReport(final Timestamp from, final Timestamp to) {
        try {
            return inventoryDAO.getInventoryValue(from, to);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory value report (range)", e);
            return Collections.emptyList();
        }
    }

    // Shared printer logic
    private <T> void printResults(final List<T> results) {
        if (results == null || results.isEmpty()) {
            LOGGER.log(Level.WARNING, "No inventory records found.");
            return;
        }

        for (final T dto : results) {
            System.out.println(dto);
        }
    }
}
