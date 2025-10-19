/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.enums.PaymentMethod;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jetbrains.annotations.Nullable;

/**
 * @author Peter M. Dela Cruz
 */
public interface Purchase {
    void addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            @Nullable Timestamp deliveryDate,
            BigDecimal vat,
            BigDecimal discVal,
            DiscountType discType)
            throws InventoryException;

    void addPurchasePayments(
            int _purchaseId,
            Timestamp paymentDate,
            String refNumber,
            PaymentMethod paymentMethod,
            BigDecimal amount)
            throws InventoryException;

    void addPurchaseItemStocks(
            int _purchaseId, int _itemStocksId, int qty_ordered, int qty_received, BigDecimal srp)
            throws InventoryException;

    BigDecimal getPurchaseLineItemCost(int purchaseId, int itemStockId) throws InventoryException;

    BigDecimal getPurchaseTotalCost(int purchaseId) throws InventoryException;
}
