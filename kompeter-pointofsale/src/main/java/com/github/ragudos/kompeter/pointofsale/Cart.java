/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.pointofsale;

import java.sql.SQLException;
import java.util.ArrayList;

import com.github.ragudos.kompeter.database.dao.sales.SaleDao;
import com.github.ragudos.kompeter.pointofsale.observers.CartObserver;
import com.github.ragudos.kompeter.pointofsale.observers.Observable;

public class Cart {
    ArrayList<CartItem> items = new ArrayList<>();
    Observable<Cart> observable = new Observable<>();
    SaleDao saleDao;

    Cart(SaleDao saleDao) {
        this.saleDao = saleDao;
    }

    public void addItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);
            if (exist.productID() == item.productID()) {
                int newQty = exist.qty() + item.qty();
                items.set(i, new CartItem(exist.productID(), exist.productName(), newQty, exist.price()));
                notifyChange("ITEM_ADDED");
                return;
            }
        }
        items.add(item);
        notifyChange("ITEM_ADDED");
    }

    //// Connect to database//// Connect to database

    public void addObserver(CartObserver<Cart> ob) {
        observable.addObserver(ob);
    }

    public double checkOut() throws SQLException {
        if (items.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout");
        } else {
            Transaction trans = new Transaction(this, saleDao);
            System.out.println("Transaction saving to database....");
            trans.saveToDatabase();
            System.out.println("Transaction has Succesfully been saved!");

            items.clear();
            notifyChange("CART_CLEARED");
            System.out.println("Cart has been cleared");
        }
        return 0;
    }

    public void decItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem exist = items.get(i);
            if (exist.productID() == item.productID()) {
                int newQty = exist.qty() - item.qty();

                if (newQty > 0) {
                    items.set(i, new CartItem(exist.productID(), exist.productName(), newQty, exist.price()));
                    notifyChange("ITEM_REMOVED");
                } else {
                    items.remove(i);
                    notifyChange("ITEM_REMOVED");
                }
                break;
            }
        }
    }

    public ArrayList<CartItem> getAllItems() {
        return items;
    }

    public double getCartTotal() {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum += items.get(i).getTotalPrice();
        }
        return sum;
    }

    public void notifyChange(String eventType) {
        observable.notifyObserver(eventType, this);
    }

    public void removeItem(int productID) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).productID() == productID) {
                items.remove(i);
                notifyChange("ITEM_REMOVED");
                break;
            }
        }
    }

    public void removeObserver(CartObserver<Cart> ob) {
        observable.removeObserver(ob);
    }
}
