/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.monitoring.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * @author Hanz Mapua
 */
public class DateToOrdinalMapper {

    private final LocalDate referenceDate;

    public DateToOrdinalMapper(Timestamp startDate) {
        // Use the system default time zone to ensure the date aligns with local
        // midnight
        this.referenceDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public int mapTimestampToOrdinalDay(Timestamp currentTimestamp) {
        LocalDate currentDate = currentTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysDifference = ChronoUnit.DAYS.between(this.referenceDate, currentDate);

        // Add 1 to make the start date (difference of 0) map to the integer 1
        return (int) daysDifference + 1;
    }
}
