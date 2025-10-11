package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import java.util.List;
import java.sql.Timestamp;
import org.jetbrains.annotations.Nullable;

public interface Inventory {
    // required methods
    List<InventoryMetadataDto> showInventoryItems(); //show inventory metadata
    void deleteItem(); // delete item in items table
    void updateItem(); // editing item
    void searchItem(); // search
    void refresh(); // refresh trigger
    
    // sorting methods
    void sortByDateAdded(); //sort by date
    void sortAlphabetically(); // sort by names
    void sortByCategory(); // sort by category
    void sortByItemId(); // sort by item id
    void sortByAscPrice(); //min to max
    void sortByDescPrice(); // max to min
    void sortByAscQuantity(); 
    void sortByDescQuantity(); 

    // Create: adding methods
    void addItem(String name, @Nullable String description); // items table only 
    void addRestock(String category, String brand, String itemName, int quantity, String supplier); // for restocking
    void addBrand(String name, @Nullable String description);
    void addCategory(String name, @Nullable String description);
    void addStorageLoc(String name, @Nullable String description);
    
    //purchasing methods
    void addPurchaseItem(int supplierId, Timestamp purchaseDate, String code, @Nullable Timestamp deliveryDate, double vat, double discVal, DiscountType discType);
    void addPurchasePayments(int _purchaseId, Timestamp paymentDate, String refNumber, PaymentMethod paymentMethod, double amount);
    void addPurchaseItemStocks(int _purchaseId, int _itemStocksId, String refNumber, PaymentMethod paymentMethod, double amount);
    
    // Assignment methods
    void setItemStockStorageLoc(int _itemStockId, int quantity_ordered, int quantity_received, int quantity, double unitCost);
    void setItemCategory(int _itemId, int _categoryId);
    void setItemStocks(int _itemId, int _itemBrandId, double _unitPrice, @Nullable int min_quantity);
    
  
}
