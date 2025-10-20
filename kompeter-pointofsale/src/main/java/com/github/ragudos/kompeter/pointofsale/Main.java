/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.database.sqlite.dao.sales.SqliteSaleDao;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        SaleDao saleDao = null;

        try {
            // Create a fake cart with sample items
            Cart cart = new Cart(saleDao);
            cart.addItem(new CartItem(1, "Apple", 3, 10.0));
            cart.addItem(new CartItem(2, "Banana", 2, 15.0));

            // Initialize a Transaction
            Transaction transaction = new Transaction(cart, new SqliteSaleDao());

            // Save sale to database
            transaction.saveToDatabase();
            System.out.println("✅ Transaction saved successfully. Sale ID: " + transaction.saleId);

            // Add a payment to the same sale
            transaction.addPayment(BigDecimal.valueOf(75.00), "REF-001", "CASH");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
