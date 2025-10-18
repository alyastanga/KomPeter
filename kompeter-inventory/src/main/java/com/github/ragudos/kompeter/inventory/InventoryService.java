/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.cryptography.PurchaseCodeGenerator;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemBrandDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemCategoryAssignmentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemCategoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemRestockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchasePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteStorageLocationDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteSupplierDao;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class InventoryService implements Inventory {
    private final SqliteItemDao sqliteItemDao;
    private final SqliteInventoryDao sqliteInventoryDao;
    private final SqlitePurchaseDao sqlitePurchaseDao;
    private final SqliteItemBrandDao sqliteItemBrandDao;
    private final SqliteItemCategoryDao sqliteItemCategoryDao;
    private final SqliteItemCategoryAssignmentDao sqliteItemCategoryAssignmentDao;
    private final SqliteItemRestockDao sqliteItemRestockDao;
    private final SqliteItemStockDao sqliteItemStockDao;
    private final SqlitePurchaseItemStockDao sqlitePurchaseItemStockDao;
    private final SqlitePurchasePaymentDao sqlitePurchasePaymentDao;
    private final SqliteSupplierDao sqliteSupplierDao;
    private final SqliteStorageLocationDao sqliteStorageLocationDao;
    private final SqliteItemStockStorageLocationDao sqliteItemStockStorageLocationDao;

    public InventoryService(
            SqliteItemDao sqliteItemDao,
            SqliteInventoryDao sqliteInventoryDao,
            SqliteItemBrandDao sqliteItemBrandDao,
            SqliteItemCategoryDao sqliteItemCategoryDao,
            SqliteItemCategoryAssignmentDao sqliteItemCategoryAssignmentDao,
            SqliteItemStockDao sqliteItemStockDao,
            SqliteItemRestockDao sqliteItemRestockDao,
            SqlitePurchaseDao sqlitePurchaseDao,
            SqlitePurchaseItemStockDao sqlitePurchaseItemStockDao,
            SqlitePurchasePaymentDao sqlitePurchasePaymentDao,
            SqliteSupplierDao sqliteSupplierDao,
            SqliteStorageLocationDao sqliteStorageLocationDao,
            SqliteItemStockStorageLocationDao sqliteItemStockStorageLocationDao) {
        this.sqliteItemDao = sqliteItemDao;
        this.sqliteInventoryDao = sqliteInventoryDao;
        this.sqliteItemBrandDao = sqliteItemBrandDao;
        this.sqliteItemCategoryDao = sqliteItemCategoryDao;
        this.sqliteItemCategoryAssignmentDao = sqliteItemCategoryAssignmentDao;
        this.sqliteItemStockDao = sqliteItemStockDao;
        this.sqliteItemRestockDao = sqliteItemRestockDao;
        this.sqlitePurchaseDao = sqlitePurchaseDao;
        this.sqlitePurchaseItemStockDao = sqlitePurchaseItemStockDao;
        this.sqlitePurchasePaymentDao = sqlitePurchasePaymentDao;
        this.sqliteSupplierDao = sqliteSupplierDao;
        this.sqliteStorageLocationDao = sqliteStorageLocationDao;
        this.sqliteItemStockStorageLocationDao = sqliteItemStockStorageLocationDao;
    }

    // sorted by _item_id by default
    @Override
    public List<InventoryMetadataDto> showInventoryItems() {
        try {
            return sqliteInventoryDao.getAllData();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteItem(int id) {
        try {
            sqliteItemDao.deleteItemById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItem(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<InventoryMetadataDto> searchItem(String search) {
        try {
            return sqliteInventoryDao.getAllData(search);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addItem(String name, String description) {
        try {
            sqliteItemDao.insertItem(name, description);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRestock(int itemStockId, int qty_added) {
        int qty_before = getItemStockById(itemStockId);
        int qty_after = qty_before + qty_added;
        try {
            sqliteItemRestockDao.insertItemRestock(itemStockId, qty_before, qty_after, qty_added);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBrand(String name, String description) {
        try {
            sqliteItemBrandDao.insertItemBrand(name, description);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCategory(String name, String description) {
        try {
            sqliteItemCategoryDao.insertItemCategory(name, description);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addStorageLoc(String name, String description) {
        try {
            sqliteStorageLocationDao.insertStorageLocation(name, description);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            Timestamp deliveryDate,
            BigDecimal vat,
            BigDecimal discVal,
            DiscountType discType) {
        String code = PurchaseCodeGenerator.generateSecureHexToken();
        try {
            sqlitePurchaseDao.insertPurchase(
                    supplierId, purchaseDate, code, deliveryDate, vat, discVal, discType);
        } catch (SQLException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public void addPurchasePayments(
            int _purchaseId,
            Timestamp paymentDate,
            String refNumber,
            PaymentMethod paymentMethod,
            BigDecimal amount) {
        try {
            sqlitePurchasePaymentDao.insertPurchasePayment(
                    _purchaseId, paymentDate, refNumber, paymentMethod, amount);
        } catch (SQLException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public void addPurchaseItemStocks(
            int _purchaseId,
            int _itemStocksId,
            String refNumber,
            PaymentMethod paymentMethod,
            BigDecimal amount) {
        try {
            sqlitePurchaseItemStockDao.insertPurchaseItemStock(
                    _purchaseId, _itemStocksId, _purchaseId, _purchaseId, _itemStocksId);
        } catch (SQLException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public void setItemCategory(int _itemId, int _categoryId) {
        try {
            sqliteItemCategoryAssignmentDao.setItemCategory(_itemId, _categoryId);
        } catch (SQLException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public void setItemStocks(
            int _itemId, int _itemBrandId, BigDecimal _unitPrice, int min_quantity) {
        try {
            sqliteItemStockDao.insertItemStock(_itemId, _itemBrandId, _unitPrice, min_quantity);
        } catch (SQLException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public void setItemStockStorageLoc(int _itemStockId, int _storageLocId, int qty) {
        try {
            sqliteItemStockStorageLocationDao.setItemStockStorageLocation(
                    _itemStockId, _storageLocId, qty);
        } catch (SQLException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public int getItemStockById(int id) {
        int qty = 0;
        try {
            Optional<ItemStockDto> optionalItemStock = sqliteItemStockDao.getItemStockById(id);
            qty = optionalItemStock.map(ItemStockDto::quantity).orElse(0);

        } catch (SQLException e) {

        } catch (IOException e) {

        }
        return qty;
    }
}
