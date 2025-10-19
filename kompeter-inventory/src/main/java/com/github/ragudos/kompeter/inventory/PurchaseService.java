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
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchaseItemStockDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqlitePurchasePaymentDao;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteSupplierDao;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Peter M. Dela Cruz
 */
public class PurchaseService implements Purchase {
    private final SqlitePurchaseDao sqlitePurchaseDao;
    private final SqlitePurchaseItemStockDao sqlitePurchaseItemStockDao;
    private final SqlitePurchasePaymentDao sqlitePurchasePaymentDao;
    private final SqliteSupplierDao sqliteSupplierDao;

    public PurchaseService(
            SqlitePurchaseDao sqlitePurchaseDao,
            SqlitePurchaseItemStockDao sqlitePurchaseItemStockDao,
            SqlitePurchasePaymentDao sqlitePurchasePaymentDao,
            SqliteSupplierDao sqliteSupplierDao) {
        this.sqlitePurchaseDao = sqlitePurchaseDao;
        this.sqlitePurchaseItemStockDao = sqlitePurchaseItemStockDao;
        this.sqlitePurchasePaymentDao = sqlitePurchasePaymentDao;
        this.sqliteSupplierDao = sqliteSupplierDao;
    }

    @Override
    public void addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            Timestamp deliveryDate,
            BigDecimal vat,
            BigDecimal discVal,
            DiscountType discType)
            throws InventoryException {
        String purchase_code = PurchaseCodeGenerator.generateSecureHexToken();
        try {
            sqlitePurchaseDao.insertPurchase(
                    supplierId, purchaseDate, purchase_code, deliveryDate, vat, discVal, discType);
        } catch (SQLException | IOException e) {
            System.err.println("Failed to register new purchase for supplier ID: " + supplierId);
            throw new InventoryException("Could not record new purchase order.", e);
        }
    }

    @Override
    public void addPurchasePayments(
            int _purchaseId,
            Timestamp paymentDate,
            String refNumber,
            PaymentMethod paymentMethod,
            BigDecimal amount)
            throws InventoryException {
        try {
            sqlitePurchasePaymentDao.insertPurchasePayment(
                    _purchaseId, paymentDate, refNumber, paymentMethod, amount);
        } catch (SQLException | IOException e) {
            System.err.println("Failed to register payment for Purchase ID: " + _purchaseId);
            throw new InventoryException("Could not record purchase payment.", e);
        }
    }

    @Override
    public void addPurchaseItemStocks(
            int _purchaseId, int _itemStocksId, int qty_ordered, int qty_received, BigDecimal srp)
            throws InventoryException {
        try {
            sqlitePurchaseItemStockDao.insertPurchaseItemStock(
                    _purchaseId, _itemStocksId, qty_ordered, qty_received, srp);
        } catch (SQLException | IOException e) {
            System.err.println("Failed to assign stocks to Purchase ID: " + _purchaseId);
            throw new InventoryException("Could not assign item stocks to purchase.", e);
        }
    }

    @Override
    public BigDecimal getPurchaseLineItemCost(int purchaseId, int itemStockId)
            throws InventoryException {
        try {
            return sqlitePurchaseItemStockDao.getPurchaseLineCost(purchaseId, itemStockId);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Could not get purchase item line cost", e);
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
