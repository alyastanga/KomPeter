/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import com.github.ragudos.kompeter.database.dto.inventory.PurchasePaymentDto;

public interface PurchasePaymentDao {
    List<PurchasePaymentDto> getAllPurchasePayment() throws SQLException, IOException;

    Optional<PurchasePaymentDto> getPurchasePaymentById(int id) throws SQLException, IOException;

    int insertPurchasePayment(int _purchaseId, Timestamp paymentDate, String referenceNumber,
            @NotNull PaymentMethod paymentMethod, @NotNull BigDecimal amountPhp) throws SQLException, IOException;
}
