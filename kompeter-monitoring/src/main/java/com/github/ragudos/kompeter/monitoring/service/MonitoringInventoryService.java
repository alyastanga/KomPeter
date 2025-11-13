/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class MonitoringInventoryService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);

    private final SqliteInventoryDao inventoryDAO;

    public MonitoringInventoryService(final SqliteInventoryDao inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public List<InventoryCountDto> getInventoryCountReport() {
        try {
            return inventoryDAO.getInventoryCount();
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory count report (range)", e);
            return Collections.emptyList();
        }
    }
}
