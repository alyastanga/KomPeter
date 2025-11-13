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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.ragudos.kompeter.database.dto.monitoring.MappedRevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;

/**
 * @author Hanz Mapua
 */
public class SalesRegressionProcessor {
    // Helper method
    private static <T> Timestamp findMinTimestamp(final List<T> data, final Function<T, Timestamp> timestampExtractor) {

        // Handle null or empty list immediately
        if (data == null || data.isEmpty()) {
            return null;
        }

        // Start with the Timestamp from the first element
        Timestamp min = timestampExtractor.apply(data.get(0));

        // Iterate through the rest of the list
        for (final T dto : data) {
            final Timestamp current = timestampExtractor.apply(dto);
            // We must check if 'current' is null before calling .before()
            if (current != null && current.before(min)) {
                min = current;
            }
        }
        return min;
    }

    private final SqliteSalesDao salesDao;

    public SalesRegressionProcessor(final SqliteSalesDao salesDao) {
        this.salesDao = salesDao;
    }

    /* --- 3. Core Range Method (from, to) --- */
    public List<MappedRevenueDto> getMappedRevenue() throws SQLException {
        // 1. Fetch the raw data using the DAO
        final List<RevenueDto> rawData = salesDao.getRevenue();

        // Handle empty results immediately
        if (rawData.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Determine the true start date for mapping (X=1)
        // If 'from' was provided, use it. If not, we must find the min date in the
        // result set.
        final Timestamp startDate = findMinTimestamp(rawData, RevenueDto::date);

        // Since we checked for empty results, startDate should not be null here.
        if (startDate == null) {
            throw new IllegalStateException("Could not determine a starting date for mapping.");
        }

        // 3. Initialize the Mapper with the determined start date
        final DateToOrdinalMapper mapper = new DateToOrdinalMapper(startDate);

        final List<MappedRevenueDto> mappedResults = new ArrayList<>();

        // 4. Iterate through the raw data and perform the mapping
        for (final RevenueDto dto : rawData) {

            // Get the X-value (integer day)
            final int ordinalDay = mapper.mapTimestampToOrdinalDay(dto.date());

            // Get the Y-value (revenue)
            final BigDecimal totalRevenue = dto.totalRevenue();

            mappedResults.add(new MappedRevenueDto(ordinalDay, totalRevenue));
        }

        return mappedResults;
    }
}
