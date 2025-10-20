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

import com.github.ragudos.kompeter.database.dto.monitoring.LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.OldItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.OnHandUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.PurchaseUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.SalesUnitDto;

/**
 * @author Hanz Mapua
 */
public interface StockDao {
    List<LowStockItemsDto> getLowStockItems() throws SQLException;

    List<OldItemsDto> getOldItems() throws SQLException;

    List<OnHandUnitDto> getOnHandUnit() throws SQLException;

    List<OnHandUnitDto> getOnHandUnit(Timestamp from) throws SQLException;

    List<OnHandUnitDto> getOnHandUnit(Timestamp from, Timestamp to) throws SQLException;

    List<PurchaseUnitDto> getPurchaseUnit() throws SQLException;

    List<PurchaseUnitDto> getPurchaseUnit(Timestamp from) throws SQLException;

    List<PurchaseUnitDto> getPurchaseUnit(Timestamp from, Timestamp to) throws SQLException;

    List<SalesUnitDto> getSalesUnit() throws SQLException;

    List<SalesUnitDto> getSalesUnit(Timestamp from) throws SQLException;

    List<SalesUnitDto> getSalesUnit(Timestamp from, Timestamp to) throws SQLException;
}
