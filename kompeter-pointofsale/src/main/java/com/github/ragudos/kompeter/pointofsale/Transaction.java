/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.cryptography.PurchaseCodeGenerator;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dao.sales.SalePaymentDao;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class Transaction {
    public static final double VAT_RATE = 0.12;

    public static void createTransaction(@NotNull ArrayList<CartItem> items, @NotNull PaymentMethod paymentMethod,
            double cashTendered) throws Exception {
        createTransaction(items, paymentMethod, cashTendered, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)),
                PurchaseCodeGenerator.generateSecureHexToken());
    }

    public static void createTransaction(@NotNull ArrayList<CartItem> items, @NotNull PaymentMethod paymentMethod,
            double cashTendered,
            @NotNull Timestamp saleDate, @NotNull String saleCode) throws Exception {
        AbstractSqlFactoryDao factoryDao = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        SaleDao saleDao = factoryDao.getSaleDao();
        SalePaymentDao salePaymentDao = factoryDao.getSalePaymentDao();
        SaleItemStockDao saleItemStockDao = factoryDao.getSaleItemStockDao();

        Connection conn = factoryDao.getConnection();

        try {
            conn.setAutoCommit(false);

            int _saleId = saleDao.createSale(conn, saleDate, saleCode, new BigDecimal(VAT_RATE));

            for (CartItem item : items) {
                saleItemStockDao.createSaleItemStock(conn, _saleId, item._itemStockId(), item.qty(),
                        new BigDecimal(item.price()));
            }

            // TODO: accept reference number of other payment methods
            salePaymentDao.createPayment(conn, _saleId, paymentMethod,
                    (paymentMethod == PaymentMethod.CASH ? "" : PurchaseCodeGenerator.generateSecureHexToken()),
                    new BigDecimal(cashTendered), saleDate);

            conn.commit();
        } catch (SQLException | IOException err) {
            try {
                conn.rollback();
            } catch (SQLException err2) {
                err.addSuppressed(err2);
            }

            Exception exception = new Exception("Failed to process transaction!");

            exception.addSuppressed(err);

            throw exception;
        }
    }
}
