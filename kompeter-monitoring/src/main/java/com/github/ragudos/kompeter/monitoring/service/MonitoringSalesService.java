package com.github.ragudos.kompeter.monitoring.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
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

    public record PredictionPoint(Timestamp date, BigDecimal value) {
    }

    public record RevenuePredictionReport(List<RevenueDto> actualData, List<PredictionPoint> predictedData) {
    }

    public record ExpensePredictionReport(List<ExpensesDto> actualData, List<PredictionPoint> predictedData) {
    }

    public record ProfitPredictionReport(List<ProfitDto> actualData, List<PredictionPoint> predictedData) {
    }

    public RevenuePredictionReport getRevenuePredictionReport()
            throws SQLException {

        final List<RevenueDto> actualData = getRevenueReport();
        final List<MappedRevenueDto> predictedMappedData = predictedValues.getPredictedRevenue(7);

        System.out.println(predictedMappedData);

        Timestamp startDate;

        startDate = findMinTimestamp(actualData, RevenueDto::date);

        final List<PredictionPoint> predictedData = new ArrayList<>();
        if (startDate != null && !predictedMappedData.isEmpty()) {
            for (final MappedRevenueDto dto : predictedMappedData) {
                final long daysToAdd = dto.ordinalDay() - 1;
                final long millisToAdd = TimeUnit.DAYS.toMillis(daysToAdd);
                final Timestamp predictedDate = new Timestamp(startDate.getTime() + millisToAdd);

                predictedData.add(new PredictionPoint(predictedDate, dto.totalRevenue()));
            }
        }

        return new RevenuePredictionReport(actualData, predictedData);
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

    public List<RevenueDto> getRevenueReport() {
        try {
            return salesDAO.getRevenue();
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
}
