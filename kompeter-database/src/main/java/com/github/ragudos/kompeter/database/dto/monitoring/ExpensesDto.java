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
public record ExpensesDto(Timestamp date, float totalExpenses) {

    @Override
    public String toString() {
        return "ExpensesDto{" + "date=" + date + ", totalExpenses=" + totalExpenses + '}';
    }
}
