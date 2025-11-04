package com.github.ragudos.kompeter.monitoring.service;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedRevenueDto;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;
import com.github.ragudos.kompeter.database.sqlite.seeder.SqliteSeeder;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

import com.github.ragudos.kompeter.monitoring.service.SalesRegressionProcessor;
import com.github.ragudos.kompeter.monitoring.service.PredictedValues;
import java.util.ArrayList;
import java.util.Collections;

public class MonitoringSalesService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteSalesDao salesDAO;
    private final SalesRegressionProcessor salesRegression;
    private final PredictedValues predictedValues;
    
    public MonitoringSalesService(SqliteSalesDao salesDAO) {
        this.salesDAO = salesDAO;
        this.salesRegression = new SalesRegressionProcessor(salesDAO);
        this.predictedValues = new PredictedValues(this.salesRegression, new LinearRegressionImpl());
    }
    
    public record PredictionPoint(Timestamp date, double value) {}
    public record RevenuePredictionReport(List<RevenueDto> actualData, List<PredictionPoint> predictedData) {}
    public record ExpensePredictionReport(List<ExpensesDto> actualData, List<PredictionPoint> predictedData) {}
    public record ProfitPredictionReport(List<ProfitDto> actualData, List<PredictionPoint> predictedData) {}
    
    public void printExpensesReport(Timestamp date, FromTo fromTo) {
        try {
            List<ExpensesDto> results = salesDAO.getExpenses(date, fromTo);
            printResults(results);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (single-date)", e);
        }
    }

    public List<ExpensesDto> getExpensesReport(Timestamp from, Timestamp to) {
        try {
            return salesDAO.getExpenses(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (range)", e);
            return Collections.emptyList();
        }
    }

    public List<ProfitDto> getProfitReport(Timestamp from, Timestamp to) {
        try {
            return salesDAO.getProfit(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (range)", e);
            return java.util.Collections.emptyList();
        }
    }
    
    public RevenuePredictionReport getRevenuePredictionReport(Timestamp from, Timestamp to, int futureDays) throws SQLException {
        
        List<RevenueDto> actualData = getRevenueReport(from, to);
        
        List<MappedRevenueDto> predictedMappedData = predictedValues.getPredictedRevenue(from, to, futureDays);

        Timestamp startDate;
        if (from != null) {
            startDate = from;
        } else {
            startDate = findMinTimestamp(actualData, RevenueDto::date);
        }

        List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (MappedRevenueDto dto : predictedMappedData) {
                long daysToAdd = dto.ordinalDay() - 1;
                long millisToAdd = TimeUnit.DAYS.toMillis(daysToAdd);
                Timestamp predictedDate = new Timestamp(startDate.getTime() + millisToAdd);
                
                predictedData.add(new PredictionPoint(predictedDate, (double) dto.totalRevenue()));
            }
        }
        
        return new RevenuePredictionReport(actualData, predictedData);
    }
    
    public ExpensePredictionReport getExpensePredictionReport(Timestamp from, Timestamp to, int futureDays) throws SQLException {
        List<ExpensesDto> actualData = getExpensesReport(from, to);
        List<MappedExpensesDto> predictedMappedData = predictedValues.getPredictedExpenses(from, to, futureDays);
        Timestamp startDate = (from != null) ? from : findMinTimestamp(actualData, ExpensesDto::date);
        
        List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (MappedExpensesDto dto : predictedMappedData) {
                long daysToAdd = dto.ordinalDay() - 1;
                Timestamp predictedDate = new Timestamp(startDate.getTime() + TimeUnit.DAYS.toMillis(daysToAdd));
                predictedData.add(new PredictionPoint(predictedDate, (double) dto.totalExpenses()));
            }
        }
        return new ExpensePredictionReport(actualData, predictedData);
    }
    
    public ProfitPredictionReport getProfitPredictionReport(Timestamp from, Timestamp to, int futureDays) throws SQLException {
        List<ProfitDto> actualData = getProfitReport(from, to);
        List<MappedProfitDto> predictedMappedData = predictedValues.getPredictedProfit(from, to, futureDays);
        Timestamp startDate = (from != null) ? from : findMinTimestamp(actualData, ProfitDto::date);
        
        List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (MappedProfitDto dto : predictedMappedData) {
                long daysToAdd = dto.ordinalDay() - 1;
                Timestamp predictedDate = new Timestamp(startDate.getTime() + TimeUnit.DAYS.toMillis(daysToAdd));
                predictedData.add(new PredictionPoint(predictedDate, (double) dto.totalProfit()));
            }
        }
        return new ProfitPredictionReport(actualData, predictedData);
    }
    
    private <T> Timestamp findMinTimestamp(List<T> data, java.util.function.Function<T, Timestamp> extractor) {
        if (data == null || data.isEmpty()) {
            return null; 
        }
        Timestamp min = null;
        for (T dto : data) {
            Timestamp current = extractor.apply(dto);
            if (current != null) {
                if (min == null || current.before(min)) {
                    min = current;
                }
            }
        }
        return min;
    }
    
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

    public List<Top10SellingItemsDto> getTop10SellingItemsReport(Timestamp from, Timestamp to) {
        try {
            return salesDAO.getTop10SellingItems(from, to);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 selling items sales report (range)", e);
            return java.util.Collections.emptyList(); 
        }
    }
    
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