/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.monitoring;

import java.sql.SQLException;
import java.util.List;

import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;

/**
 * @author Hanz Mapua
 */
public interface SalesDao {

    List<RevenueDto> getRevenue() throws SQLException;

    List<Top10SellingItemsDto> getTop10SellingItems() throws SQLException;
}
