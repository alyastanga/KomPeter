/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.PurchasePaymentDao;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.PurchasePaymentDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqlitePurchasePaymentDao implements PurchasePaymentDao {

    @Override
    public List<PurchasePaymentDto> getAllPurchasePayment() throws SQLException, IOException {
        List<PurchasePaymentDto> purchasePaymentList = new ArrayList<>();
        var query = SqliteQueryLoader.getInstance().get("select_all_purchase_payments", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection();
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery();) {

            while (rs.next()) {
                PurchasePaymentDto purchasePayment = new PurchasePaymentDto(rs.getInt("_purchase_payment_id"),
                        rs.getInt("_purchase_id"), rs.getTimestamp("_created_at"), rs.getTimestamp("payment_date"),
                        rs.getString("reference_number"), PaymentMethod.fromString(rs.getString("payment_method")),
                        rs.getBigDecimal("amount_php"));
                purchasePaymentList.add(purchasePayment);
            }
        }
        return purchasePaymentList;
    }

    @Override
    public Optional<PurchasePaymentDto> getPurchasePaymentById(int id) throws SQLException, IOException {
        Optional<PurchasePaymentDto> purchasePaymentOptional = Optional.empty();
        var query = SqliteQueryLoader.getInstance().get("select_purchase_payment_by_id", "items",
                AbstractSqlQueryLoader.SqlQueryType.SELECT);
        try (var conn = SqliteFactoryDao.getInstance().getConnection(); var stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                PurchasePaymentDto purchasePayment;
                purchasePayment = new PurchasePaymentDto(rs.getInt("_purchase_payment_id"), rs.getInt("_purchase_id"),
                        rs.getTimestamp("_created_at"), rs.getTimestamp("payment_date"),
                        rs.getString("reference_number"), PaymentMethod.fromString(rs.getString("payment_method")),
                        rs.getBigDecimal("amount_php"));
                purchasePaymentOptional = Optional.of(purchasePayment);
            }
        }
        return purchasePaymentOptional;
    }

    @Override
    public int insertPurchasePayment(int _purchaseId, Timestamp paymentDate, String referenceNumber,
            PaymentMethod paymentMethod, BigDecimal amountPhp) throws SQLException, IOException {
        var query = SqliteQueryLoader.getInstance().get("insert_purchase_payment", "items",
                AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt = new NamedPreparedStatement(SqliteFactoryDao.getInstance().getConnection(), query,
                Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt("_purchase_id", _purchaseId);
            stmt.setTimestamp("payment_date", paymentDate);
            stmt.setString("reference_number", referenceNumber);
            stmt.setString("payment_method", paymentMethod.toString());
            stmt.setBigDecimal("amount_php", amountPhp);
            stmt.executeUpdate();
            var rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}
