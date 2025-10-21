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
import com.github.ragudos.kompeter.database.dto.monitoring.OnHandUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.PurchaseUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.SalesUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10OldItemsDto;

/**
 * @author Hanz Mapua
 */
public interface StockDao {

    List<OnHandUnitDto> getOnHandUnit() throws SQLException;

    List<OnHandUnitDto> getOnHandUnit(Timestamp date, FromTo fromto) throws SQLException;

    List<OnHandUnitDto> getOnHandUnit(Timestamp from, Timestamp to) throws SQLException;

    List<PurchaseUnitDto> getPurchaseUnit() throws SQLException;

    List<PurchaseUnitDto> getPurchaseUnit(Timestamp date, FromTo fromto) throws SQLException;

    List<PurchaseUnitDto> getPurchaseUnit(Timestamp from, Timestamp to) throws SQLException;

    List<SalesUnitDto> getSalesUnit() throws SQLException;

    List<SalesUnitDto> getSalesUnit(Timestamp date, FromTo fromto) throws SQLException;

    List<SalesUnitDto> getSalesUnit(Timestamp from, Timestamp to) throws SQLException;

    List<Top10LowStockItemsDto> getTop10LowStockItems() throws SQLException;

    List<Top10OldItemsDto> getTop10OldItems() throws SQLException;
}
