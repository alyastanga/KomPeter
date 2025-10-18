/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.monitoring;

import com.github.ragudos.kompeter.database.dto.monitoring.LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.OldItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.OnHandUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.PurchaseUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.SalesUnitDto;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Hanz Mapua
 */
public interface StockDao {
    public List<PurchaseUnitDto> getPurchaseUnit() throws SQLException;

    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from) throws SQLException;

    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from, Timestamp to) throws SQLException;

    public List<SalesUnitDto> getSalesUnit() throws SQLException;

    public List<SalesUnitDto> getSalesUnit(Timestamp from) throws SQLException;

    public List<SalesUnitDto> getSalesUnit(Timestamp from, Timestamp to) throws SQLException;

    public List<OnHandUnitDto> getOnHandUnit() throws SQLException;

    public List<OnHandUnitDto> getOnHandUnit(Timestamp from) throws SQLException;

    public List<OnHandUnitDto> getOnHandUnit(Timestamp from, Timestamp to) throws SQLException;

    public List<LowStockItemsDto> getLowStockItems() throws SQLException;

    public List<LowStockItemsDto> getLowStockItems(Timestamp from) throws SQLException;

    public List<LowStockItemsDto> getLowStockItems(Timestamp from, Timestamp to) throws SQLException;

    public List<OldItemsDto> getOldItems() throws SQLException;

    public List<OldItemsDto> getOldItems(Timestamp from) throws SQLException;

    public List<OldItemsDto> getOldItems(Timestamp from, Timestamp to) throws SQLException;
}
