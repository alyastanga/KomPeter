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
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;
import com.github.ragudos.kompeter.database.sqlite.seeder.SqliteSeeder;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.Collections;

/**
 * @author Hanz Mapua
 */
public class MonitoringSalesService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteSalesDao salesDAO;

    public MonitoringSalesService(SqliteSalesDao salesDAO) {
        this.salesDAO = salesDAO;
    }

    public void printExpensesReport(Timestamp date, FromTo fromTo) {
        try {
            List<ExpensesDto> results = salesDAO.getExpenses(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (single-date)", e);
        }
    }

    // 3️⃣ Date range
    public List<ExpensesDto> getExpensesReport(Timestamp from, Timestamp to) {
        try {
            return salesDAO.getExpenses(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (range)", e);
            // Return an empty list so the UI doesn't crash
            return Collections.emptyList();
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

   public List<RevenueDto> getRevenueReport(Timestamp from, Timestamp to) {
        try {
            return salesDAO.getRevenue(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching revenue sales report (range)", e);
            return java.util.Collections.emptyList();
        }
    }

    // 2️⃣ Single date + direction (FROM or TO)
    public List<Top10SellingItemsDto> getTop10SellingItemsReport(Timestamp from, Timestamp to) {
        try {
            // This is the same call your print method makes
            return salesDAO.getTop10SellingItems(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 selling items sales report (range)", e);
            // Return an empty list so the UI doesn't crash
            return java.util.Collections.emptyList(); 
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
