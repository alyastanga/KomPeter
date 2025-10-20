/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.monitoring;

import java.sql.Timestamp;

/**
 * @author Hanz Mapua
 */
public record RevenueDto(Timestamp date, float totalRevenue) {

    @Override
    public String toString() {
        return "RevenueDto{" + "date=" + date + ", totalRevenue=" + totalRevenue + '}';
    }
}
