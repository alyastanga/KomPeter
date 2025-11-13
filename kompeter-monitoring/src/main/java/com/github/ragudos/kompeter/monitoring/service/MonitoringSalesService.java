package com.github.ragudos.kompeter.monitoring.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedRevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class MonitoringSalesService {

    private static final Logger LOGGER = KompeterLogger.getLogger(MonitoringInventoryService.class);
    private final SqliteSalesDao salesDAO;
    private final SalesRegressionProcessor salesRegression;
    private final PredictedValues predictedValues;

    public MonitoringSalesService(final SqliteSalesDao salesDAO) {
        this.salesDAO = salesDAO;
        this.salesRegression = new SalesRegressionProcessor(salesDAO);
        this.predictedValues = new PredictedValues(this.salesRegression, new LinearRegressionImpl());
    }

    public record PredictionPoint(Timestamp date, double value) {
    }

    public record RevenuePredictionReport(List<RevenueDto> actualData, List<PredictionPoint> predictedData) {
    }

    public record ExpensePredictionReport(List<ExpensesDto> actualData, List<PredictionPoint> predictedData) {
    }

    public record ProfitPredictionReport(List<ProfitDto> actualData, List<PredictionPoint> predictedData) {
    }

    public void printExpensesReport(final Timestamp date, final FromTo fromTo) {
        try {
            final List<ExpensesDto> results = salesDAO.getExpenses(date, fromTo);
            printResults(results);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (single-date)", e);
        }
    }

    public List<ExpensesDto> getExpensesReport(final Timestamp from, final Timestamp to) {
        try {
            return salesDAO.getExpenses(from, to);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching expenses sales report (range)", e);
            return Collections.emptyList();
        }
    }

    public List<ProfitDto> getProfitReport(final Timestamp from, final Timestamp to) {
        try {
            return salesDAO.getProfit(from, to);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (range)", e);
            return java.util.Collections.emptyList();
        }
    }

    public RevenuePredictionReport getRevenuePredictionReport(final Timestamp from, final Timestamp to,
            final int futureDays)
            throws SQLException {

        final List<RevenueDto> actualData = getRevenueReport(from, to);

        final List<MappedRevenueDto> predictedMappedData = predictedValues.getPredictedRevenue(from, to, futureDays);

        Timestamp startDate;
        if (from != null) {
            startDate = from;
        } else {
            startDate = findMinTimestamp(actualData, RevenueDto::date);
        }

        final List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (final MappedRevenueDto dto : predictedMappedData) {
                final long daysToAdd = dto.ordinalDay() - 1;
                final long millisToAdd = TimeUnit.DAYS.toMillis(daysToAdd);
                final Timestamp predictedDate = new Timestamp(startDate.getTime() + millisToAdd);

                predictedData.add(new PredictionPoint(predictedDate, (double) dto.totalRevenue()));
            }
        }

        return new RevenuePredictionReport(actualData, predictedData);
    }

    public ExpensePredictionReport getExpensePredictionReport(final Timestamp from, final Timestamp to,
            final int futureDays)
            throws SQLException {
        final List<ExpensesDto> actualData = getExpensesReport(from, to);
        final List<MappedExpensesDto> predictedMappedData = predictedValues.getPredictedExpenses(from, to, futureDays);
        final Timestamp startDate = (from != null) ? from : findMinTimestamp(actualData, ExpensesDto::date);

        final List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (final MappedExpensesDto dto : predictedMappedData) {
                final long daysToAdd = dto.ordinalDay() - 1;
                final Timestamp predictedDate = new Timestamp(startDate.getTime() + TimeUnit.DAYS.toMillis(daysToAdd));
                predictedData.add(new PredictionPoint(predictedDate, (double) dto.totalExpenses()));
            }
        }
        return new ExpensePredictionReport(actualData, predictedData);
    }

    public ProfitPredictionReport getProfitPredictionReport(final Timestamp from, final Timestamp to,
            final int futureDays)
            throws SQLException {
        final List<ProfitDto> actualData = getProfitReport(from, to);
        final List<MappedProfitDto> predictedMappedData = predictedValues.getPredictedProfit(from, to, futureDays);
        final Timestamp startDate = (from != null) ? from : findMinTimestamp(actualData, ProfitDto::date);

        final List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (final MappedProfitDto dto : predictedMappedData) {
                final long daysToAdd = dto.ordinalDay() - 1;
                final Timestamp predictedDate = new Timestamp(startDate.getTime() + TimeUnit.DAYS.toMillis(daysToAdd));
                predictedData.add(new PredictionPoint(predictedDate, (double) dto.totalProfit()));
            }
        }
        return new ProfitPredictionReport(actualData, predictedData);
    }

    private <T> Timestamp findMinTimestamp(final List<T> data,
            final java.util.function.Function<T, Timestamp> extractor) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        Timestamp min = null;
        for (final T dto : data) {
            final Timestamp current = extractor.apply(dto);
            if (current != null) {
                if (min == null || current.before(min)) {
                    min = current;
                }
            }
        }
        return min;
    }

    public void printProfitReport(final Timestamp from, final Timestamp to) {
        try {
            final List<ProfitDto> results = salesDAO.getProfit(from, to);
            printResults(results);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching profit sales report (range)", e);
        }
    }

    public List<RevenueDto> getRevenueReport(final Timestamp from, final Timestamp to) {
        try {
            return salesDAO.getRevenue(from, to);
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching revenue sales report (range)", e);
            return java.util.Collections.emptyList();
        }
    }

    public List<Top10SellingItemsDto> getTop10SellingItemsReport() {
        try {
            return salesDAO.getTop10SellingItems();
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching top 10 selling items sales report (range)", e);
            return java.util.Collections.emptyList();
        }
    }

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
