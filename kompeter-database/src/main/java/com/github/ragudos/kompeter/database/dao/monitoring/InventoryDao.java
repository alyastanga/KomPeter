/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.monitoring;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.enums.FromTo;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;

/**
 * @author Hanz Mapua
 */
public interface InventoryDao {
    List<InventoryCountDto> getInventoryCount() throws SQLException;

    List<InventoryCountDto> getInventoryCount(Timestamp date, FromTo fromto) throws SQLException;

    List<InventoryCountDto> getInventoryCount(Timestamp from, Timestamp to) throws SQLException;

    List<InventoryValueDto> getInventoryValue() throws SQLException;

    List<InventoryValueDto> getInventoryValue(Timestamp date, FromTo fromto) throws SQLException;

    List<InventoryValueDto> getInventoryValue(Timestamp from, Timestamp to) throws SQLException;
}
