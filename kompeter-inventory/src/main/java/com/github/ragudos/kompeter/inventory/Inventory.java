/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface Inventory {
    // required methods
    List<InventoryMetadataDto> showInventoryItems(); // show inventory metadata

    void deleteItem(int id); // delete item in items table

    void updateItem(int id); // editing item

    List<InventoryMetadataDto> searchItem(String search); // search

    // Create: adding methods
    void addItem(String name, @Nullable String description); // items table only

    void addRestock(int itemStockId, int qty_added);

    void addBrand(String name, @Nullable String description);

    void addCategory(String name, @Nullable String description);

    void addStorageLoc(String name, @Nullable String description);

    // purchasing methods
    void addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            @Nullable Timestamp deliveryDate,
            BigDecimal vat,
            BigDecimal discVal,
            DiscountType discType);

    void addPurchasePayments(
            int _purchaseId,
            Timestamp paymentDate,
            String refNumber,
            PaymentMethod paymentMethod,
            BigDecimal amount);

    void addPurchaseItemStocks(
            int _purchaseId,
            int _itemStocksId,
            String refNumber,
            PaymentMethod paymentMethod,
            BigDecimal amount);

    // Assignment methodss
    void setItemStockStorageLoc(int _itemStockId, int _storageLocId, int qty);

    void setItemCategory(int _itemId, int _categoryId);

    void setItemStocks(
            int _itemId, int _itemBrandId, BigDecimal _unitPrice, @Nullable int min_quantity);

    int getItemStockById(int id);
}
