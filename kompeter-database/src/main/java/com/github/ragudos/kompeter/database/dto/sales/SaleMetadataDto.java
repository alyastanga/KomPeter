/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.sales;

import java.math.BigDecimal;
import java.security.Timestamp;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleMetadataDto {
    Timestamp _createdAt;
    int _saleId;
    String customerName;
    BigDecimal discountValue;
    DiscountType disountType;
    SaleItemStocks[] items;
    SaleMetadataPayments[] payments;
    String saleCode;
    Timestamp saleDate;
    BigDecimal vatPercent;

    @Data
    @Builder
    public static class SaleItemStocks {
        Timestamp _createdAt;
        int _itemStockId;
        int quantity;
        BigDecimal unitPricePhp;
    }

    @Data
    @Builder
    public static class SaleMetadataPayments {
        Timestamp _createdAt;
        int _paymentId;
        BigDecimal amountPhp;
        Timestamp paymentDate;
        PaymentMethod paymentMethod;
        String referenceNumber;
    }
}
