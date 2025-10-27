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
import java.util.function.Function;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedExpensesDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.MappedRevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;

/**
 * @author Hanz Mapua
 */
public class SalesRegressionProcessor {

    public static void main(String[] args) throws SQLException {
        SqliteSalesDao salesDao = new SqliteSalesDao();
        SalesRegressionProcessor test = new SalesRegressionProcessor(salesDao);

        for (MappedRevenueDto dto : test.getMappedRevenue()) {
            System.out.println(dto);
        }
        System.out.println("\n");
        for (MappedExpensesDto dto : test.getMappedExpenses()) {
            System.out.println(dto);
        }
        System.out.println("\n");
        for (MappedProfitDto dto : test.getMappedProfit()) {
            System.out.println(dto);
        }
    }

    // Helper method
    private static <T> Timestamp findMinTimestamp(List<T> data, Function<T, Timestamp> timestampExtractor) {

        // Handle null or empty list immediately
        if (data == null || data.isEmpty()) {
            return null;
        }

        // Start with the Timestamp from the first element
        Timestamp min = timestampExtractor.apply(data.get(0));

        // Iterate through the rest of the list
        for (T dto : data) {
            Timestamp current = timestampExtractor.apply(dto);
            // We must check if 'current' is null before calling .before()
            if (current != null && current.before(min)) {
                min = current;
            }
        }
        return min;
    }

    private final SqliteSalesDao salesDao;

    public SalesRegressionProcessor(SqliteSalesDao salesDao) {
        this.salesDao = salesDao;
    }

    public List<MappedExpensesDto> getMappedExpenses() throws SQLException {

        return getMappedExpenses((Timestamp) null, (Timestamp) null);
    }

    public List<MappedExpensesDto> getMappedExpenses(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {

            return getMappedExpenses(date, (Timestamp) null);
        } else {

            return getMappedExpenses((Timestamp) null, date);
        }
    }

    public List<MappedExpensesDto> getMappedExpenses(Timestamp from, Timestamp to) throws SQLException {

        List<ExpensesDto> rawData = salesDao.getExpenses(from, to);

        if (rawData.isEmpty()) {
            return new ArrayList<>();
        }

        Timestamp startDate = (from != null) ? from : findMinTimestamp(rawData, ExpensesDto::date);

        if (startDate == null) {
            throw new IllegalStateException("Could not determine a starting date for mapping.");
        }

        DateToOrdinalMapper mapper = new DateToOrdinalMapper(startDate);

        List<MappedExpensesDto> mappedResults = new ArrayList<>();

        for (ExpensesDto dto : rawData) {

            int ordinalDay = mapper.mapTimestampToOrdinalDay(dto.date());

            float totalExpenses = dto.totalExpenses();

            mappedResults.add(new MappedExpensesDto(ordinalDay, totalExpenses));
        }

        return mappedResults;
    }

    public List<MappedProfitDto> getMappedProfit() throws SQLException {

        return getMappedProfit((Timestamp) null, (Timestamp) null);
    }

    public List<MappedProfitDto> getMappedProfit(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {

            return getMappedProfit(date, (Timestamp) null);
        } else {

            return getMappedProfit((Timestamp) null, date);
        }
    }

    public List<MappedProfitDto> getMappedProfit(Timestamp from, Timestamp to) throws SQLException {

        List<ProfitDto> rawData = salesDao.getProfit(from, to);

        if (rawData.isEmpty()) {
            return new ArrayList<>();
        }

        Timestamp startDate = (from != null) ? from : findMinTimestamp(rawData, ProfitDto::date);

        if (startDate == null) {
            throw new IllegalStateException("Could not determine a starting date for mapping.");
        }

        DateToOrdinalMapper mapper = new DateToOrdinalMapper(startDate);

        List<MappedProfitDto> mappedResults = new ArrayList<>();

        for (ProfitDto dto : rawData) {

            int ordinalDay = mapper.mapTimestampToOrdinalDay(dto.date());

            float totalProfit = dto.totalProfit();

            mappedResults.add(new MappedProfitDto(ordinalDay, totalProfit));
        }

        return mappedResults;
    }

    /* --- 1. No Parameters (All/Default Range) --- */
    public List<MappedRevenueDto> getMappedRevenue() throws SQLException {
        // Calls the core method with both dates null, which tells the DAO to use the
        // widest range.
        return getMappedRevenue((Timestamp) null, (Timestamp) null);
    }

    /* --- 2. Single Date with FromTo Enum --- */
    public List<MappedRevenueDto> getMappedRevenue(Timestamp date, FromTo fromto) throws SQLException {
        if (fromto == FromTo.FROM) {
            // Data starting FROM 'date' until NOW
            return getMappedRevenue(date, (Timestamp) null);
        } else {
            // Data FROM the earliest date (null) until 'date'
            return getMappedRevenue((Timestamp) null, date);
        }
    }

    /* --- 3. Core Range Method (from, to) --- */
    public List<MappedRevenueDto> getMappedRevenue(Timestamp from, Timestamp to) throws SQLException {

        // 1. Fetch the raw data using the DAO
        List<RevenueDto> rawData = salesDao.getRevenue(from, to);

        // Handle empty results immediately
        if (rawData.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Determine the true start date for mapping (X=1)
        // If 'from' was provided, use it. If not, we must find the min date in the
        // result set.
        Timestamp startDate = (from != null) ? from : findMinTimestamp(rawData, RevenueDto::date);

        // Since we checked for empty results, startDate should not be null here.
        if (startDate == null) {
            throw new IllegalStateException("Could not determine a starting date for mapping.");
        }

        // 3. Initialize the Mapper with the determined start date
        DateToOrdinalMapper mapper = new DateToOrdinalMapper(startDate);

        List<MappedRevenueDto> mappedResults = new ArrayList<>();

        // 4. Iterate through the raw data and perform the mapping
        for (RevenueDto dto : rawData) {

            // Get the X-value (integer day)
            int ordinalDay = mapper.mapTimestampToOrdinalDay(dto.date());

            // Get the Y-value (revenue)
            float totalRevenue = dto.totalRevenue();

            mappedResults.add(new MappedRevenueDto(ordinalDay, totalRevenue));
        }

        return mappedResults;
    }
}
