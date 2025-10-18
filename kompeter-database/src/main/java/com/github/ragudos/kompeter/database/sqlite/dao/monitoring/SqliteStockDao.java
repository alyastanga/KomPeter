/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.monitoring;

import com.github.ragudos.kompeter.database.dao.monitoring.StockDao;
import com.github.ragudos.kompeter.database.dto.monitoring.LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.OldItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.OnHandUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.PurchaseUnitDto;
import com.github.ragudos.kompeter.database.dto.monitoring.SalesUnitDto;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Hanz Mapua
 */
public class SqliteStockDao implements StockDao {
    @Override
    public List<PurchaseUnitDto> getPurchaseUnit() {
        return getPurchaseUnit(null, null);
    }

    @Override
    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from) {
        return getPurchaseUnit(from, null);
    }

    @Override
    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<SalesUnitDto> getSalesUnit() {
        return getSalesUnit(null, null);
    }

    @Override
    public List<SalesUnitDto> getSalesUnit(Timestamp from) {
        return getSalesUnit(from, null);
    }

    @Override
    public List<SalesUnitDto> getSalesUnit(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<OnHandUnitDto> getOnHandUnit() {
        return getOnHandUnit(null, null);
    }

    @Override
    public List<OnHandUnitDto> getOnHandUnit(Timestamp from) {
        return getOnHandUnit(from, null);
    }

    @Override
    public List<OnHandUnitDto> getOnHandUnit(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<LowStockItemsDto> getLowStockItems() {
        return getLowStockItems(null, null);
    }

    @Override
    public List<LowStockItemsDto> getLowStockItems(Timestamp from) {
        return getLowStockItems(from, null);
    }

    @Override
    public List<LowStockItemsDto> getLowStockItems(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<OldItemsDto> getOldItems() {
        return getOldItems(null, null);
    }

    @Override
    public List<OldItemsDto> getOldItems(Timestamp from) {
        return getOldItems(from, null);
    }

    @Override
    public List<OldItemsDto> getOldItems(Timestamp from, Timestamp to) {
        return null;
    }
}
