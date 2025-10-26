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
public record OnHandUnitDto(Timestamp date, int totalPurchased, int totalSold, int totalOnHand) {
}
