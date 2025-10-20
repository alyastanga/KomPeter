/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;
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

/**
 * @author Hanz Mapua
 */
public class MonitoringSalesService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteSalesDao salesDAO;

    public MonitoringSalesService(SqliteSalesDao salesDAO) {
        this.salesDAO = salesDAO;
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printRevenueReport() {
        printRevenueReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printRevenueReport(Timestamp date, FromTo fromTo) {
        try {
            List<RevenueDto> results = salesDAO.getRevenue(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching revenue sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printRevenueReport(Timestamp from, Timestamp to) {
        try {
            List<RevenueDto> results = salesDAO.getRevenue(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching revenue sales report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printExpensesReport() {
        printExpensesReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printExpensesReport(Timestamp date, FromTo fromTo) {
        try {
            List<ExpensesDto> results = salesDAO.getExpenses(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printExpensesReport(Timestamp from, Timestamp to) {
        try {
            List<ExpensesDto> results = salesDAO.getExpenses(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printProfitReport() {
        printProfitReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printProfitReport(Timestamp date, FromTo fromTo) {
        try {
            List<ProfitDto> results = salesDAO.getProfit(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printProfitReport(Timestamp from, Timestamp to) {
        try {
            List<ProfitDto> results = salesDAO.getProfit(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (range)", e);
        }
    }

    // 1️⃣ No date filter — calls DAO with both nulls
    public void printTop10SellingItemsReport() {
        printTop10SellingItemsReport((Timestamp) null, (Timestamp) null);
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public void printTop10SellingItemsReport(Timestamp date, FromTo fromTo) {
        try {
            List<Top10SellingItemsDto> results = salesDAO.getTop10SellingItems(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 selling items sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public void printTop10SellingItemsReport(Timestamp from, Timestamp to) {
        try {
            List<Top10SellingItemsDto> results = salesDAO.getTop10SellingItems(from, to);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 selling items sales report (range)", e);
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

    public static void main(String[] args) throws IOException, SQLException {
        AbstractMigratorFactory factory =
                AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);

        // initialize schema
        Migrator migrator = factory.getMigrator();
        migrator.migrate();

        // initialize seeder
        SqliteSeeder seeder = new SqliteSeeder();
        seeder.seed();

        MonitoringSalesService service = new MonitoringSalesService(new SqliteSalesDao());
        // sample timestamp values
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());

        System.out.println("REVENUE - FROM 10/14 TO 10/21");
        service.printRevenueReport(from, to);
        System.out.println("\n");

        System.out.println("EXPENSES - FROM 10/14 TO 10/21");
        service.printExpensesReport(from, to);
        System.out.println("\n");

        System.out.println("PROFIT - FROM 10/14 TO 10/21");
        service.printProfitReport(from, to);
        System.out.println("\n");

        System.out.println("TOP 10 SELLING ITEMS");
        service.printTop10SellingItemsReport(from, to);

        Files.deleteIfExists(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
    }
}
