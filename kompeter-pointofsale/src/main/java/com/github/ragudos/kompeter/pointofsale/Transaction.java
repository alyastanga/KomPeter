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

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.cryptography.PurchaseCodeGenerator;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;

// TODO: REDUCE INVENTORY STOCK AFTER A SALE
public class Transaction {
    public static final BigDecimal VAT_RATE = new BigDecimal("0.12");

    public static int createTransaction(@NotNull final Cart cart, final String customerName,
            @NotNull final BigDecimal paymentAmount, @NotNull final PaymentMethod paymentMethod,
            @NotNull final DiscountType discountType, @NotNull final BigDecimal discountAmount) throws Exception {
        final Timestamp saleDate = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
        final String saleCode = PurchaseCodeGenerator.generateSecureHexToken();
        final AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);
        final SaleDao saleDao = factoryDao.getSaleDao();
        final SalePaymentDao salePaymentDao = factoryDao.getSalePaymentDao();
        final SaleItemStockDao saleItemStockDao = factoryDao.getSaleItemStockDao();
        final Connection conn = factoryDao.getConnection();

        try {
            conn.setAutoCommit(false);

            final int _saleId = saleDao.createSale(conn, saleDate, saleCode, VAT_RATE, discountType, discountAmount);

            for (final CartItem item : cart.getAllItems()) {
                saleItemStockDao.createSaleItemStock(conn, _saleId, item._itemStockId(), item.qty(), item.price());
            }

            // TODO: accept reference number of other payment methods
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
}
