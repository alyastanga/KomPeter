/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.cryptography.PurchaseCodeGenerator;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockDao;
import com.github.ragudos.kompeter.database.dao.inventory.ItemStockStorageLocationDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStatus;
import com.github.ragudos.kompeter.database.dto.inventory.ItemStockStorageLocationDto;
import com.github.ragudos.kompeter.database.dto.sales.SaleMetadataDto;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class Transaction {
    public static final BigDecimal VAT_RATE = new BigDecimal("0.12");

    private static final Logger LOGGER = KompeterLogger.getLogger(Transaction.class);

    public static int createTransaction(@NotNull final Cart cart, final String customerName,
            @NotNull final BigDecimal paymentAmount, @NotNull final PaymentMethod paymentMethod,
            @NotNull final DiscountType discountType, @NotNull final BigDecimal discountAmount) throws Exception {
        final Timestamp saleDate = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
        final String saleCode = PurchaseCodeGenerator.generateSecureHexToken();
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final SaleDao saleDao = factoryDao.getSaleDao();
        final SalePaymentDao salePaymentDao = factoryDao.getSalePaymentDao();
        final SaleItemStockDao saleItemStockDao = factoryDao.getSaleItemStockDao();
        final ItemStockStorageLocationDao itemStockStorageLocationDao = factoryDao.getItemStockStorageLocationDao();
        final ItemStockDao itemStockDao = factoryDao.getItemStockDao();
        final Connection conn = factoryDao.getConnection();

        try {
            conn.setAutoCommit(false);
            final int _saleId = saleDao.createSale(conn, saleDate, saleCode, VAT_RATE, discountType, discountAmount);

            for (final CartItem item : cart.getAllItems()) {
                saleItemStockDao.createSaleItemStock(conn, _saleId, item._itemStockId(), item.qty(), item.price());

                int totalRemaining = item.qty();
                int totalLocQty = 0;
                final ItemStockStorageLocationDto[] locations = itemStockStorageLocationDao.getAllData(conn);

                for (final ItemStockStorageLocationDto loc : locations) {
                    if (totalRemaining <= 0) {
                        break;
                    }

                    if (loc.quantity() == 0) {
                        continue;
                    }

                    final int available = loc.quantity();
                    final int toTake = Math.min(totalRemaining, available);

                    totalRemaining -= toTake;
                    totalLocQty += loc.quantity();
                    itemStockStorageLocationDao.updateItemStockQuantity(conn, loc.quantity() - toTake,
                            loc._itemStockStorageLocationId());
                }

                if (totalLocQty == item.qty()) {
                    itemStockDao.setItemStocksStatusByName(conn, item.name(), ItemStatus.INACTIVE);
                }
            }

            salePaymentDao.createPayment(conn, _saleId, paymentMethod,
                    (paymentMethod == PaymentMethod.CASH ? "" : PurchaseCodeGenerator.generateSecureHexToken()),
                    paymentAmount, saleDate);

            conn.commit();

            return _saleId;
        } catch (SQLException | IOException err) {
            try {
                conn.rollback();
            } catch (final SQLException err2) {
                err.addSuppressed(err2);
            }

            final Exception exception = new Exception("Failed to process transaction!");

            exception.addSuppressed(err);

            throw exception;
        }
    }

    public static SaleMetadataDto[] getAllTransactions() throws Exception {
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final SaleDao saleDao = factoryDao.getSaleDao();

        try (Connection conn = factoryDao.getConnection()) {
            return saleDao.getAllSales(conn);
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "", err);

            throw new Exception("Failed to get transactions");
        }
    }
}
