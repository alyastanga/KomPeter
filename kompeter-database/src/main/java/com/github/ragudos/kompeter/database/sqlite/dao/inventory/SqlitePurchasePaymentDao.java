/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.inventory.PurchasePaymentDao;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

public class SqlitePurchasePaymentDao implements PurchasePaymentDao {

    @Override
    public List<PurchaseItemStockDto> getExpenses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseItemStockDto> getExpenses(Timestamp from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseItemStockDto> getExpenses(Timestamp from, Timestamp to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int insertPurchasePayment(
            int _purchaseId,
            Timestamp paymentDate,
            String referenceNumber,
            PaymentMethod paymentMethod,
            BigDecimal amountPhp)
            throws SQLException, IOException {
        var query =
                SqliteQueryLoader.getInstance()
                        .get("insert_purchase_payment", "items", AbstractSqlQueryLoader.SqlQueryType.INSERT);
        try (var stmt =
                new NamedPreparedStatement(
                        SqliteFactoryDao.getInstance().getConnection(),
                        query,
                        Statement.RETURN_GENERATED_KEYS); ) {
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
