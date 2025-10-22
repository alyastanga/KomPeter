/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.ragudos.kompeter.cryptography.PurchaseCodeGenerator;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseDto;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import com.github.ragudos.kompeter.database.dto.inventory.PurchasePaymentDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchasePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteSupplierDao;

/**
 * @author Peter M. Dela Cruz
 */
public class PurchaseService implements Purchase {
    private final SqlitePurchaseDao sqlitePurchaseDao;
    private final SqlitePurchaseItemStockDao sqlitePurchaseItemStockDao;
    private final SqlitePurchasePaymentDao sqlitePurchasePaymentDao;
    private final SqliteSupplierDao sqliteSupplierDao;

    public PurchaseService(SqlitePurchaseDao sqlitePurchaseDao, SqlitePurchaseItemStockDao sqlitePurchaseItemStockDao,
            SqlitePurchasePaymentDao sqlitePurchasePaymentDao, SqliteSupplierDao sqliteSupplierDao) {
        this.sqlitePurchaseDao = sqlitePurchaseDao;
        this.sqlitePurchaseItemStockDao = sqlitePurchaseItemStockDao;
        this.sqlitePurchasePaymentDao = sqlitePurchasePaymentDao;
        this.sqliteSupplierDao = sqliteSupplierDao;
    }

    @Override
    public int addPurchaseItem(int supplierId, Timestamp purchaseDate, Timestamp deliveryDate, BigDecimal vat,
            BigDecimal discVal, DiscountType discType) throws InventoryException {
        String purchase_code = PurchaseCodeGenerator.generateSecureHexToken();
        try {
            return sqlitePurchaseDao.insertPurchase(supplierId, purchaseDate, purchase_code, deliveryDate, vat, discVal,
                    discType);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not record new purchase order.", e);
        }
    }

    @Override
    public int addPurchaseItemStocks(int _purchaseId, int _itemStocksId, int qty_ordered, int qty_received,
            BigDecimal srp) throws InventoryException {
        try {
            return sqlitePurchaseItemStockDao.insertPurchaseItemStock(_purchaseId, _itemStocksId, qty_ordered,
                    qty_received, srp);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not assign item stocks to purchase.", e);
        }
    }

    @Override
    public int addPurchasePayments(int _purchaseId, Timestamp paymentDate, PaymentMethod paymentMethod,
            BigDecimal amount) throws InventoryException {
        try {
            String refNumber = "PO-" + UUID.randomUUID();

            return sqlitePurchasePaymentDao.insertPurchasePayment(_purchaseId, paymentDate, refNumber, paymentMethod,
                    amount);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not record purchase payment.", e);
        }
    }

    @Override
    public List<PurchasePaymentDto> getAllPurchasePayment() throws InventoryException {
        try {
            return sqlitePurchasePaymentDao.getAllPurchasePayment();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get purchase payments data", e);
        }
    }

    @Override
    public List<PurchaseDto> getAllPurchases() throws InventoryException {
        try {
            return sqlitePurchaseDao.getAllPurchase();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get all the purchases data", e);
        }
    }

    @Override
    public List<PurchaseItemStockDto> getPurchaseItemStockById(int id) throws InventoryException {
        try {
            return sqlitePurchaseItemStockDao.getAllDataByPurchaseId(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get purchases item stock data", e);
        }
    }

    @Override
    public BigDecimal getPurchaseLineItemCost(int purchaseId, int itemStockId) throws InventoryException {
        try {
            return sqlitePurchaseItemStockDao.getPurchaseLineCost(purchaseId, itemStockId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get purchase item line cost", e);
        }
    }

    @Override
    public Optional<PurchasePaymentDto> getPurchasePaymentById(int id) throws InventoryException {
        try {
            return sqlitePurchasePaymentDao.getPurchasePaymentById(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get purchase payment data of purchase id: " + id, e);
        }
    }

    @Override
    public BigDecimal getPurchaseTotalCost(int purchaseId) throws InventoryException {
        try {
            return sqlitePurchaseItemStockDao.getPurchaseTotalCost(purchaseId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get purchase item total cost", e);
        }
    }
}
