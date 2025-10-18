/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.dto.enums.DiscountType;
import com.github.ragudos.kompeter.database.dto.sales.SaleDto;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

class Transaction {

    static int transCounter = 0;
    ArrayList<CartItem> cartItems = new ArrayList<>();
    int TransID;
    LocalDateTime TimeStamp;
    double Total;
    SaleDao saleDao;

    final double vatRate = 0.12;
    double discountValue = 0.0;
    DiscountType discountType = DiscountType.FIXED;

    Transaction(Cart cart, SaleDao saleDao) {
        this.saleDao = saleDao;
        this.TransID = ++transCounter;
        this.TimeStamp = LocalDateTime.now();
        for (CartItem item : cart.items) {
            CartItem copy = new CartItem(item.productID(), item.productName(), item.qty(), item.price());
            cartItems.add(copy);
        } /// Copies all the transaction and stores in a differnt array
        this.Total = cart.getCartTotal();
    }

    void saveToDatabase() throws SQLException {
        saleDao.save(toSaleDto());
    }

    SaleDto toSaleDto() {
        return new SaleDto(
                TransID,
                Timestamp.valueOf(TimeStamp),
                Timestamp.valueOf(TimeStamp),
                "SALE-" + TransID,
                "Walk-in Customer",
                BigDecimal.valueOf(0.12),
                BigDecimal.ZERO,
                DiscountType.FIXED);
    }
/*
    void printReciept() {
        double vatAmount = Total * vatRate;
        double discountAmount =
                (discountType == DiscountType.PERCENTAGE) ? Total * (discountValue / 100) : discountValue;
        double grandTotal = Total + vatAmount - discountAmount;

        System.out.println(" ============================= ");
        System.out.println("    KOMPETER COMPUTER PARTS    ");
        System.out.println("     & ACCESSORIES RECEIPT     ");
        System.out.println(" ============================= ");

        System.out.println(" Transaction ID: " + TransID);
        System.out.println(" Date & Time: " + TimeStamp);
        System.out.println(" ----------------------------- ");
        System.out.printf("%-15s %5s %10s%n", "Item", "Qty", "Subtotal");
        System.out.println(" ----------------------------- ");

        for (CartItem items : cartItems) {
            double subTotal = items.qty() * items.price();
            System.out.printf("%-15s %5d %10.2f%n", items.productName(), items.qty(), subTotal);
        }

        System.out.println(" ----------------------------- ");
        System.out.printf("%-20s %10.2f%n", "Subtotal:", Total);
        System.out.printf("%-20s %10.2f%n", "VAT (12%):", vatAmount);
        if (discountAmount > 0) {
            System.out.printf("%-20s %10.2f%n", "Discount: ", -discountAmount);
        }
        System.out.println(" ----------------------------- ");
        System.out.printf("%-20s %10.2f%n", " Grand Total: ", +grandTotal);
        System.out.println(" ============================= ");
        System.out.println("     THANK YOU FOR SHOPPING    ");
        System.out.println(" ============================= ");
    } */
}
