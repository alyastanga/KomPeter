/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao.Direction;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao.OrderBy;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteItemDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseDao;
import com.github.ragudos.kompeter.cryptography.PurchaseCodeGenerator;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class InventoryService implements Inventory {
    private final SqliteItemDao sqliteItemDao;
    private final SqliteInventoryDao sqliteInventoryDao;
    private final SqlitePurchaseDao sqlitePurchaseDao;

    public InventoryService(SqliteItemDao sqliteItemDao, SqliteInventoryDao sqliteInventoryDao, SqlitePurchaseDao sqlitePurchaseDao) {
        this.sqliteItemDao = sqliteItemDao;
        this.sqliteInventoryDao = sqliteInventoryDao;
        this.sqlitePurchaseDao = sqlitePurchaseDao;
    }
    
    //sorted by _item_id by default
    @Override
    public List<InventoryMetadataDto> showInventoryItems() {
        try {
            return sqliteInventoryDao.getAllData(null, null, null);
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
    public List<InventoryMetadataDto> searchItem(String search){
        try {
            return sqliteInventoryDao.getAllData(search, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public void addRestock(
            String category, String brand, String itemName, int quantity, String supplier) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addBrand(String name, String description) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addCategory(String name, String description) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addStorageLoc(String name, String description) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            Timestamp deliveryDate,
            float vat,
            double discVal,
            DiscountType discType) {
                    String code = PurchaseCodeGenerator.generateSecureHexToken();
                         
        try{
            sqlitePurchaseDao.insertPurchase(supplierId, purchaseDate,code, deliveryDate, supplierId, supplierId, discType);
        }catch(SQLException e){
            
        }catch(IOException e){
            
        }
    }

    @Override
    public void addPurchasePayments(
            int _purchaseId,
            Timestamp paymentDate,
            String refNumber,
            PaymentMethod paymentMethod,
            double amount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addPurchaseItemStocks(
            int _purchaseId,
            int _itemStocksId,
            String refNumber,
            PaymentMethod paymentMethod,
            double amount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setItemStockStorageLoc(
            int _itemStockId,
            int quantity_ordered,
            int quantity_received,
            int quantity,
            double unitCost) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setItemCategory(int _itemId, int _categoryId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setItemStocks(int _itemId, int _itemBrandId, double _unitPrice, int min_quantity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<InventoryMetadataDto> sortByDateAdded(Direction direction) {
        try {
            return sqliteInventoryDao.getAllData(null, OrderBy.DATE, direction);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InventoryMetadataDto> sortByName() {
       try {
            return sqliteInventoryDao.getAllData(null, OrderBy.ITEM_NAME, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InventoryMetadataDto> sortByCategory() {
        try {
            return sqliteInventoryDao.getAllData(null, OrderBy.CATEGORY_NAME, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InventoryMetadataDto> sortByPrice(Direction direction) {
        try {
            return sqliteInventoryDao.getAllData(null, OrderBy.PRICE,  direction);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InventoryMetadataDto> sortByQuantity(Direction direction) {
        try {
            return sqliteInventoryDao.getAllData(null, OrderBy.QUANTITY, direction);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
