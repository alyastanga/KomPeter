/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.monitoring.MappedRevenueDto;

/**
 * @author Hanz Mapua
 */
public class PredictedValues {
    private final LinearRegressionImpl linearRegression;

    private final SalesRegressionProcessor salesRegression;

    public PredictedValues(final SalesRegressionProcessor salesRegression,
            final LinearRegressionImpl linearRegression) {
        this.salesRegression = salesRegression;
        this.linearRegression = linearRegression;
    }

    public List<MappedRevenueDto> getPredictedRevenue(final int DaystoPredict)
            throws SQLException {
        final List<MappedRevenueDto> rawData = salesRegression.getMappedRevenue();

        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }

        final List<Double> XValues = new ArrayList<>();
        final List<Double> YValues = new ArrayList<>();

        for (final MappedRevenueDto dto : rawData) {
            XValues.add((double) dto.ordinalDay());
            YValues.add(dto.totalRevenue().doubleValue());
        }

        final int maxHistoricalDay = rawData.get(rawData.size() - 1).ordinalDay();

        final List<MappedRevenueDto> predictedData = new ArrayList<>();

        for (int i = 0; i < DaystoPredict; i++) {
            final int predictedDayNumber = maxHistoricalDay + i + 1;

            final Double predictedNextValue = linearRegression.LinearRegression(XValues, YValues,
                    (double) predictedDayNumber);

            final MappedRevenueDto predictedDto = new MappedRevenueDto(predictedDayNumber,
                    new BigDecimal(predictedNextValue));

            predictedData.add(predictedDto);

            XValues.add((double) predictedDayNumber);
            YValues.add(predictedNextValue);
        }

        return predictedData;
    }
}
