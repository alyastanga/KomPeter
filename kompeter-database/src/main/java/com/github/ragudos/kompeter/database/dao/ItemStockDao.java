package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.PurchaseItemStockDto;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface ItemStockDao {

    public List<PurchaseItemStockDto> getInventoryCount() throws SQLException;

    public List<PurchaseItemStockDto> getInventoryCount(Timestamp from) throws SQLException;

    public List<PurchaseItemStockDto> getInventoryCount(Timestamp from, Timestamp to) throws SQLException;

    
    public List<PurchaseItemStockDto> getInventoryValue() throws SQLException;

    public List<PurchaseItemStockDto> getInventoryValue(Timestamp from) throws SQLException;

    public List<PurchaseItemStockDto> getInventoryValue(Timestamp from, Timestamp to) throws SQLException;

    
    public List<PurchaseItemStockDto> getOnHandUnit();

    public List<PurchaseItemStockDto> getOnHandUnit(Timestamp from);

    public List<PurchaseItemStockDto> getOnHandUnit(Timestamp from, Timestamp to);

    
    public List<PurchaseItemStockDto> getLowStockItems();

    public List<PurchaseItemStockDto> getLowStockItems(Timestamp from);

    public List<PurchaseItemStockDto> getLowStockItems(Timestamp from, Timestamp to);

    
    public List<PurchaseItemStockDto> getOldItems();

    public List<PurchaseItemStockDto> getOldItems(Timestamp from);

    public List<PurchaseItemStockDto> getOldItems(Timestamp from, Timestamp to);
}
