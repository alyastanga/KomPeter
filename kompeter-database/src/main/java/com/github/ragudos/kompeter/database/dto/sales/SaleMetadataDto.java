/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.sales;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class SaleMetadataDto {

    Timestamp createdAt;
    String customerName;
    String discountType; // Represents DiscountType enum as String
    BigDecimal discountValue;
    SaleMetadataPayments[] payments;
    String saleCode;
    Timestamp saleDate;
    int saleId;
    SaleItemStocks[] saleItemStocks;
    BigDecimal vatPercent;

    @JsonCreator
    @Builder
    public SaleMetadataDto(@JsonProperty("_saleId") final int saleId, @JsonProperty("saleCode") final String saleCode,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("saleDate") final Timestamp saleDate,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("_createdAt") final Timestamp createdAt,
            @JsonProperty("disountType") final String discountType,
            @JsonProperty("discountValue") final BigDecimal discountValue,
            @JsonProperty("vatPercent") final BigDecimal vatPercent,
            @JsonProperty("customerName") final String customerName,
            @JsonProperty("payments") final SaleMetadataPayments[] payments,
            @JsonProperty("saleItemStocks") final SaleItemStocks[] saleItemStocks) {

        // Use the Lombok-generated all-args constructor (which @Value provides)
        this.saleId = saleId;
        this.saleCode = saleCode;
        this.saleDate = saleDate;
        this.createdAt = createdAt;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.vatPercent = vatPercent;
        this.customerName = customerName;
        this.payments = payments;
        this.saleItemStocks = saleItemStocks;
    }

    // --- Inner Classes ---

    @Value
    public static class SaleItemStocks {
        Timestamp createdAt;
        int itemStockId;
        int quantity;
        BigDecimal unitPricePhp;

        @JsonCreator
        @Builder
        public SaleItemStocks(@JsonProperty("_itemStockId") final int itemStockId,
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("_createdAt") final Timestamp createdAt,
                @JsonProperty("quantity") final int quantity,
                @JsonProperty("unitPricePhp") final BigDecimal unitPricePhp) {
            this.itemStockId = itemStockId;
            this.createdAt = createdAt;
            this.quantity = quantity;
            this.unitPricePhp = unitPricePhp;
        }
    }

    @Value
    public static class SaleMetadataPayments {
        BigDecimal amountPhp;
        Timestamp createdAt;
        Timestamp paymentDate;
        int paymentId;
        String paymentMethod; // Represents PaymentMethod enum as String
        String referenceNumber;

        @JsonCreator
        @Builder
        public SaleMetadataPayments(@JsonProperty("_paymentId") final int paymentId,
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("_createdAt") final Timestamp createdAt,
                @JsonProperty("amountPhp") final BigDecimal amountPhp,
                @JsonProperty("paymentMethod") final String paymentMethod,
                @JsonProperty("referenceNumber") final String referenceNumber,
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") @JsonProperty("paymentDate") final Timestamp paymentDate) {
            this.paymentId = paymentId;
            this.createdAt = createdAt;
            this.amountPhp = amountPhp;
            this.paymentMethod = paymentMethod;
            this.referenceNumber = referenceNumber;
            this.paymentDate = paymentDate;
        }
    }
}
