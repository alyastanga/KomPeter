/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.seeder.SqliteSeeder;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitoringInventoryService {
    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteInventoryDao inventoryDAO;

    public MonitoringInventoryService(SqliteInventoryDao inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public void printInventoryReport(Timestamp from, Timestamp to) {
        try {
            List<InventoryCountDto> results = inventoryDAO.getInventoryCount(from, to);

            if (results.isEmpty()) {
                LOGGER.log(Level.WARNING, "No inventory records found for range: {0} -> {1}");
            } else {
                LOGGER.log(Level.INFO, "Fetched {0} inventory records for range: {1} -> {2}");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory report", e);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        AbstractMigratorFactory factory =
                AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
        Migrator migrator = factory.getMigrator();
        migrator.migrate();

        SqliteSeeder seeder = new SqliteSeeder();
        seeder.seed();

        MonitoringInventoryService service = new MonitoringInventoryService(new SqliteInventoryDao());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(14));
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());

        service.printInventoryReport(from, to);

        Files.deleteIfExists(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
    }
}
