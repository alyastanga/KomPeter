/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.OnHandUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.PurchaseUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.SalesUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10OldItemsDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteStockDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hanz Mapua
 */
public class MonitoringStockService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteStockDao stockDAO;

    public MonitoringStockService(SqliteStockDao stockDAO) {
        this.stockDAO = stockDAO;
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printPurchaseUnitReport() {
        printPurchaseUnitReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printPurchaseUnitReport(Timestamp date, FromTo fromTo) {
        try {
            List<PurchaseUnitDto> results = stockDAO.getPurchaseUnit(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching revenue sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printPurchaseUnitReport(Timestamp from, Timestamp to) {
        try {
            List<PurchaseUnitDto> results = stockDAO.getPurchaseUnit(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching revenue sales report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printSalesUnitReport() {
        printSalesUnitReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printSalesUnitReport(Timestamp date, FromTo fromTo) {
        try {
            List<SalesUnitDto> results = stockDAO.getSalesUnit(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printSalesUnitReport(Timestamp from, Timestamp to) {
        try {
            List<SalesUnitDto> results = stockDAO.getSalesUnit(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printOnhandUnitReport() {
        printOnhandUnitReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printOnhandUnitReport(Timestamp date, FromTo fromTo) {
        try {
            List<OnHandUnitDto> results = stockDAO.getOnHandUnit(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printOnhandUnitReport(Timestamp from, Timestamp to) {
        try {
            List<OnHandUnitDto> results = stockDAO.getOnHandUnit(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printTop10LowStockItems() {
        try {
            List<Top10LowStockItemsDto> results = stockDAO.getTop10LowStockItems();
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 low stock items stock report", e);
        }
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printTop10OldItemsReport() {
        try {
            List<Top10OldItemsDto> results = stockDAO.getTop10OldItems();
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 old items stock report", e);
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
    //    public static void main(String[] args) throws IOException, SQLException {
    //        // find db file
    //        AbstractMigratorFactory factory =
    //                AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
    //
    //        // initialize V1 schema
    //        Migrator migrator = factory.getMigrator();
    //        migrator.migrate();
    //
    //        // initialize seeder
    //        SqliteSeeder seeder = new SqliteSeeder();
    //        seeder.seed();
    //
    //        MonitoringStockService service = new MonitoringStockService(new SqliteStockDao());
    //        // sample timestamp values
    //        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
    //        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
    //
    //        System.out.println("PURCHASE UNIT - FROM 10/14 TO 10/21");
    //        service.printPurchaseUnitReport(from, to);
    //        System.out.println("\n");
    //
    //        System.out.println("SALES UNIT - FROM 10/14 TO 10/21");
    //        service.printSalesUnitReport(from, to);
    //        System.out.println("\n");
    //
    //        System.out.println("ON HAND UNIT - FROM 10/14 TO 10/21");
    //        service.printOnhandUnitReport(from, to);
    //        System.out.println("\n");
    //
    //        System.out.println("TOP 10 LOW STOCK ITEMS");
    //        service.printTop10LowStockItems();
    //        System.out.println("\n");
    //
    //        System.out.println("TOP 10 OLD ITEMS");
    //        service.printTop10OldItemsReport();
    //
    //        Files.deleteIfExists(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
    //    }
}
