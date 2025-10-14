package com.github.ragudos.kompeter.database.dto.sales;

import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record SalePaymentDto(
        int _salePaymentId,
        int _saleId,
        @NotNull Timestamp _createdAt,
        @NotNull Timestamp paymentDate,
        String referenceNumber,
        @NotNull PaymentMethod paymentMethod,
        @NotNull BigDecimal amountPhp) {}
