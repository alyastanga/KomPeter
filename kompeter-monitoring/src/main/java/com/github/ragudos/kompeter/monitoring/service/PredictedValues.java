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
import java.util.ArrayList;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedRevenueDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;

/**
 * @author Hanz Mapua
 */
public class PredictedValues {

    public static void main(String[] args) throws SQLException {
        SqliteSalesDao salesDao = new SqliteSalesDao();
        SalesRegressionProcessor salesRegression = new SalesRegressionProcessor(salesDao);
        LinearRegressionImpl linearRegression = new LinearRegressionImpl();
        PredictedValues PV = new PredictedValues(salesRegression, linearRegression);

        for (MappedRevenueDto dto : salesRegression.getMappedRevenue()) {
            System.out.println(dto);
        }
        for (MappedRevenueDto dto : PV.getPredictedRevenue(5)) {
            System.out.println(dto + "--PREDICTED REVENUE VALUE--");
        }
        System.out.println("\n");

        for (MappedExpensesDto dto : salesRegression.getMappedExpenses()) {
            System.out.println(dto);
        }
        for (MappedExpensesDto dto : PV.getPredictedExpenses(5)) {
            System.out.println(dto + "--PREDICTED REVENUE VALUE--");
        }
        System.out.println("\n");

        for (MappedProfitDto dto : salesRegression.getMappedProfit()) {
            System.out.println(dto);
        }
        for (MappedProfitDto dto : PV.getPredictedProfit(5)) {
            System.out.println(dto + "--PREDICTED REVENUE VALUE--");
        }
    }

    private final LinearRegressionImpl linearRegression;

    private final SalesRegressionProcessor salesRegression;

    public PredictedValues(SalesRegressionProcessor salesRegression, LinearRegressionImpl linearRegression) {
        this.salesRegression = salesRegression;
        this.linearRegression = linearRegression;
    }

    public List<MappedExpensesDto> getPredictedExpenses(Timestamp date, FromTo fromto, int DaystoPredict)
            throws SQLException {
        if (fromto == FromTo.FROM) {

            return getPredictedExpenses(date, (Timestamp) null, DaystoPredict);
        } else {

            return getPredictedExpenses((Timestamp) null, date, DaystoPredict);
        }
    }

    public List<MappedExpensesDto> getPredictedExpenses(Timestamp from, Timestamp to, int DaystoPredict)
            throws SQLException {

        List<MappedExpensesDto> rawData = salesRegression.getMappedExpenses(from, to);

        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }

        List<Double> XValues = new ArrayList<>();
        List<Double> YValues = new ArrayList<>();

        for (MappedExpensesDto dto : rawData) {
            XValues.add((double) dto.ordinalDay());
            YValues.add((double) dto.totalExpenses());
        }

        int maxHistoricalDay = rawData.get(rawData.size() - 1).ordinalDay();

        List<MappedExpensesDto> predictedData = new ArrayList<>();

        for (int i = 0; i < DaystoPredict; i++) {
            int predictedDayNumber = maxHistoricalDay + i + 1;

            Double predictedNextValue = linearRegression.LinearRegression(XValues, YValues, (double) predictedDayNumber // The
            // new
            // X-value
            // we
            // want
            // to
            // predict
            // for
            );

            MappedExpensesDto predictedDto = new MappedExpensesDto(predictedDayNumber, predictedNextValue.floatValue());

            predictedData.add(predictedDto);

            XValues.add((double) predictedDayNumber);
            YValues.add(predictedNextValue);
        }

        return predictedData;
    }

    public List<MappedExpensesDto> getPredictedExpenses(int DaystoPredict) throws SQLException {

        return getPredictedExpenses((Timestamp) null, (Timestamp) null, DaystoPredict);
    }

    public List<MappedProfitDto> getPredictedProfit(Timestamp date, FromTo fromto, int DaystoPredict)
            throws SQLException {
        if (fromto == FromTo.FROM) {

            return getPredictedProfit(date, (Timestamp) null, DaystoPredict);
        } else {

            return getPredictedProfit((Timestamp) null, date, DaystoPredict);
        }
    }

    public List<MappedProfitDto> getPredictedProfit(Timestamp from, Timestamp to, int DaystoPredict)
            throws SQLException {

        List<MappedProfitDto> rawData = salesRegression.getMappedProfit(from, to);

        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }

        List<Double> XValues = new ArrayList<>();
        List<Double> YValues = new ArrayList<>();

        for (MappedProfitDto dto : rawData) {
            XValues.add((double) dto.ordinalDay());
            YValues.add((double) dto.totalProfit());
        }

        int maxHistoricalDay = rawData.get(rawData.size() - 1).ordinalDay();

        List<MappedProfitDto> predictedData = new ArrayList<>();

        for (int i = 0; i < DaystoPredict; i++) {
            int predictedDayNumber = maxHistoricalDay + i + 1;

            Double predictedNextValue = linearRegression.LinearRegression(XValues, YValues, (double) predictedDayNumber // The
            // new
            // X-value
            // we
            // want
            // to
            // predict
            // for
            );

            MappedProfitDto predictedDto = new MappedProfitDto(predictedDayNumber, predictedNextValue.floatValue());

            predictedData.add(predictedDto);

            XValues.add((double) predictedDayNumber);
            YValues.add(predictedNextValue);
        }

        return predictedData;
    }

    public List<MappedProfitDto> getPredictedProfit(int DaystoPredict) throws SQLException {

        return getPredictedProfit((Timestamp) null, (Timestamp) null, DaystoPredict);
    }

    public List<MappedRevenueDto> getPredictedRevenue(Timestamp date, FromTo fromto, int DaystoPredict)
            throws SQLException {
        if (fromto == FromTo.FROM) {

            return getPredictedRevenue(date, (Timestamp) null, DaystoPredict);
        } else {

            return getPredictedRevenue((Timestamp) null, date, DaystoPredict);
        }
    }

    public List<MappedRevenueDto> getPredictedRevenue(Timestamp from, Timestamp to, int DaystoPredict)
            throws SQLException {

        List<MappedRevenueDto> rawData = salesRegression.getMappedRevenue(from, to);

        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }

        List<Double> XValues = new ArrayList<>();
        List<Double> YValues = new ArrayList<>();

        for (MappedRevenueDto dto : rawData) {
            XValues.add((double) dto.ordinalDay());
            YValues.add((double) dto.totalRevenue());
        }

        int maxHistoricalDay = rawData.get(rawData.size() - 1).ordinalDay();

        List<MappedRevenueDto> predictedData = new ArrayList<>();

        for (int i = 0; i < DaystoPredict; i++) {
            int predictedDayNumber = maxHistoricalDay + i + 1;

            Double predictedNextValue = linearRegression.LinearRegression(XValues, YValues, (double) predictedDayNumber // The
            // new
            // X-value
            // we
            // want
            // to
            // predict
            // for
            );

            MappedRevenueDto predictedDto = new MappedRevenueDto(predictedDayNumber, predictedNextValue.floatValue());

            predictedData.add(predictedDto);

            XValues.add((double) predictedDayNumber);
            YValues.add(predictedNextValue);
        }

        return predictedData;
    }

    public List<MappedRevenueDto> getPredictedRevenue(int DaystoPredict) throws SQLException {

        return getPredictedRevenue((Timestamp) null, (Timestamp) null, DaystoPredict);
    }
}
