/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import java.sql.SQLException;
import java.util.ArrayList;

public class Cart {
    SaleDao saleDao;
    static int transID = 0;

    ArrayList<CartItem> items = new ArrayList<>();

    Cart(SaleDao saleDao) {
        this.saleDao = saleDao;
    }

    void addItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);
            if (exist.productID() == item.productID()) {
                int newQty = exist.qty() + item.qty();
                items.set(i, new CartItem(exist.productID(), exist.productName(), newQty, exist.price()));
                return;
            }
        }
        items.add(item);
    } //// Connect to database//// Connect to database

    void decItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);
            if (exist.productID() == item.productID()) {
                int newQty = exist.qty() - item.qty();

                if (newQty > 0) {
                    items.set(i, new CartItem(exist.productID(), exist.productName(), newQty, exist.price()));
                } else {
                    items.remove(i);
                }
                break;
            }
        }
    }

    void removeItem(int productID) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).productID() == productID) {
                items.remove(i);
                break;
            }
        }
    }

    double getCartTotal() {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum += items.get(i).getTotalPrice();
        }
        return sum;
    }

    ArrayList<CartItem> getAllItems() {
        return items;
    }

    double checkOut() throws SQLException {
        if (items.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout");
        } else {
            Transaction trans = new Transaction(this, saleDao);
            System.out.println("Transaction saving to database....");
            trans.saveToDatabase();
            System.out.println("Transaction has Succesfully been saved!");

            items.clear();
            System.out.println("Cart has been cleared");
        }
        return 0;
    }
}
