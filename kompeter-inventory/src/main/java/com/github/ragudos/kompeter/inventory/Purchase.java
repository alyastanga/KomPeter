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
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseDto;
import com.github.ragudos.kompeter.database.dto.inventory.PurchaseItemStockDto;
import com.github.ragudos.kompeter.database.dto.inventory.PurchasePaymentDto;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

/**
 * @author Peter M. Dela Cruz
 */
public interface Purchase {
    int addPurchaseItem(
            int supplierId,
            Timestamp purchaseDate,
            @Nullable Timestamp deliveryDate,
            BigDecimal vat,
            BigDecimal discVal,
            DiscountType discType)
            throws InventoryException;

    int addPurchasePayments(
            int _purchaseId, Timestamp paymentDate, PaymentMethod paymentMethod, BigDecimal amount)
            throws InventoryException;

    int addPurchaseItemStocks(
            int _purchaseId, int _itemStocksId, int qty_ordered, int qty_received, BigDecimal srp)
            throws InventoryException;

    BigDecimal getPurchaseLineItemCost(int purchaseId, int itemStockId) throws InventoryException;

    BigDecimal getPurchaseTotalCost(int purchaseId) throws InventoryException;

    List<PurchaseDto> getAllPurchases() throws InventoryException;

    List<PurchaseItemStockDto> getPurchaseItemStockById(int id) throws InventoryException;

    List<PurchasePaymentDto> getAllPurchasePayment() throws InventoryException;

    Optional<PurchasePaymentDto> getPurchasePaymentById(int id) throws InventoryException;
}
