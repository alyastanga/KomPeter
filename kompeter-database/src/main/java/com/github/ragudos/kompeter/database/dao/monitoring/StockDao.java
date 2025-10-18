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
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Hanz Mapua
 */
public interface StockDao {
    public List<PurchaseUnitDto> getPurchaseUnit();

    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from);

    public List<PurchaseUnitDto> getPurchaseUnit(Timestamp from, Timestamp to);

    public List<SalesUnitDto> getSalesUnit();

    public List<SalesUnitDto> getSalesUnit(Timestamp from);

    public List<SalesUnitDto> getSalesUnit(Timestamp from, Timestamp to);

    public List<OnHandUnitDto> getOnHandUnit();

    public List<OnHandUnitDto> getOnHandUnit(Timestamp from);

    public List<OnHandUnitDto> getOnHandUnit(Timestamp from, Timestamp to);

    public List<LowStockItemsDto> getLowStockItems();

    public List<LowStockItemsDto> getLowStockItems(Timestamp from);

    public List<LowStockItemsDto> getLowStockItems(Timestamp from, Timestamp to);

    public List<OldItemsDto> getOldItems();

    public List<OldItemsDto> getOldItems(Timestamp from);

    public List<OldItemsDto> getOldItems(Timestamp from, Timestamp to);
}
