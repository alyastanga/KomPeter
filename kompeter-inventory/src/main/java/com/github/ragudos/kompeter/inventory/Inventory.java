/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao.Direction;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import java.sql.Timestamp;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface Inventory {
    // required methods
    List<InventoryMetadataDto> showInventoryItems(); // show inventory metadata

    void deleteItem(int id); // delete item in items table

    void updateItem(int id); // editing item

    List<InventoryMetadataDto> searchItem(String search); // search

    void refresh(); // refresh trigger

    // sorting methods
    List<InventoryMetadataDto> sortByDateAdded(@Nullable Direction direction); // sort by date

    List<InventoryMetadataDto> sortByName(); // sort by names

    List<InventoryMetadataDto> sortByCategory(); // sort by category

    List<InventoryMetadataDto> sortByPrice(@Nullable Direction direction); 

    List<InventoryMetadataDto> sortByQuantity(@Nullable Direction direction);



    // Create: adding methods
    void addItem(String name, @Nullable String description); // items table only

    void addRestock(
            String category,
            String brand,
            String itemName,
            int quantity,
            String supplier); // for restocking

    void addBrand(String name, @Nullable String description);

    void addCategory(String name, @Nullable String description);

    void addStorageLoc(String name, @Nullable String description);

    // purchasing methods
    void addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            @Nullable Timestamp deliveryDate,
            float vat,
            double discVal,
            DiscountType discType);

    void addPurchasePayments(
            int _purchaseId,
            Timestamp paymentDate,
            String refNumber,
            PaymentMethod paymentMethod,
            double amount);

    void addPurchaseItemStocks(
            int _purchaseId,
            int _itemStocksId,
            String refNumber,
            PaymentMethod paymentMethod,
            double amount);

    // Assignment methods
    void setItemStockStorageLoc(
            int _itemStockId, int quantity_ordered, int quantity_received, int quantity, double unitCost);

    void setItemCategory(int _itemId, int _categoryId);

    void setItemStocks(int _itemId, int _itemBrandId, double _unitPrice, @Nullable int min_quantity);
}
