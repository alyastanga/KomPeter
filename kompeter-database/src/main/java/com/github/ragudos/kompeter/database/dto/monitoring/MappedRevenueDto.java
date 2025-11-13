/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.monitoring;

import java.math.BigDecimal;

/**
 * @author Hanz Mapua
 */
public record MappedRevenueDto(int ordinalDay, BigDecimal totalRevenue) {
}
