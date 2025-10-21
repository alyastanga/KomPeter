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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class MonitoringInventoryService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteInventoryDao inventoryDAO;

    public MonitoringInventoryService(SqliteInventoryDao inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printInventoryCountReport() {
        printInventoryCountReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printInventoryCountReport(Timestamp date, FromTo fromTo) {
        try {
            List<InventoryCountDto> results = inventoryDAO.getInventoryCount(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printInventoryCountReport(Timestamp from, Timestamp to) {
        try {
            List<InventoryCountDto> results = inventoryDAO.getInventoryCount(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printInventoryValueReport() {
        printInventoryValueReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printInventoryValueReport(Timestamp date, FromTo fromTo) {
        try {
            List<InventoryValueDto> results = inventoryDAO.getInventoryValue(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printInventoryValueReport(Timestamp from, Timestamp to) {
        try {
            List<InventoryValueDto> results = inventoryDAO.getInventoryValue(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory report (range)", e);
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
    // TEST
    // public static void main(String[] args) throws IOException, SQLException {
    // AbstractMigratorFactory factory =
    // AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
    //
    // // initialize schema
    // Migrator migrator = factory.getMigrator();
    // migrator.migrate();
    //
    // // initialize seeder
    // SqliteSeeder seeder = new SqliteSeeder();
    // seeder.seed();
    //
    // MonitoringInventoryService service = new MonitoringInventoryService(new
    // SqliteInventoryDao());
    //
    // Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(14));
    // Timestamp to = Timestamp.valueOf(LocalDateTime.now());
    //
    // service.printInventoryCountReport(from, to);
    // System.out.println("\n");
    // service.printInventoryValueReport(from, to);
    //
    // Files.deleteIfExists(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
    // }
}
