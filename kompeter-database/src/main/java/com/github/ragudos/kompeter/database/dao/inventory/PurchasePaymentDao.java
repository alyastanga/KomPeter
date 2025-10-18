/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface PurchasePaymentDao {
    public List<PurchaseItemStockDto> getExpenses();

    public List<PurchaseItemStockDto> getExpenses(Timestamp from);

    public List<PurchaseItemStockDto> getExpenses(Timestamp from, Timestamp to);

    int insertPurchasePayment(
            int _purchaseId,
            Timestamp paymentDate,
            String referenceNumber,
            @NotNull PaymentMethod paymentMethod,
            @NotNull BigDecimal amountPhp)
            throws SQLException, IOException;
}
