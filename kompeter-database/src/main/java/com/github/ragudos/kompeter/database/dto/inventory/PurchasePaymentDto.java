package com.github.ragudos.kompeter.database.dto.inventory;

import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record PurchasePaymentDto(
        int _purchasePaymentId,
        int _purchaseId,
        @NotNull Timestamp _createdAt,
        @NotNull Timestamp paymentDate,
        String referenceNumber,
        @NotNull PaymentMethod paymentMethod,
        @NotNull BigDecimal amountPhp) {}
