package com.github.ragudos.kompeter.pointofsale;

import java.util.ArrayList;

public class TransHistory {
    ArrayList<Transaction> transList = new ArrayList<>();

    void addTransaction(Transaction t) {
        transList.add(t);
    }

    void displayTrans() {
        for (int i = 0; i < transList.size(); i++) {
            System.out.println("Date and Time: " + transList.get(i).TimeStamp);
            System.out.println("Transaction ID: " + transList.get(i).TransID);
            System.out.println("Transaction Total: " + transList.get(i).Total);
            System.out.println("View More");
            System.out.println("-----------------");
        }
    }

    void getTransDetails(int TransID) {
        for (int i = 0; i < transList.size(); i++) {
            if (transList.get(i).TransID == TransID) {
                Transaction t =
                        transList.get(
                                i); /// once found, it puts it inside "t" object, then i can use everything that was
                // used in it.
                System.out.println("Date and Time: " + t.TimeStamp);
                System.out.println("Transaction ID: " + t.TransID);
                System.out.println("Transaction Total: " + t.Total);
                System.out.println("----Items Bought-----");

                for (int j = 0; j < t.cartItems.size(); j++) {
                    System.out.println("Product ID: " + t.cartItems.get(j).productID());
                    System.out.println("Product Name: " + t.cartItems.get(j).productName());
                    System.out.println("Quantity: " + t.cartItems.get(j).qty());
                    System.out.println("Price: " + t.cartItems.get(j).price());
                    System.out.println("-----------------");
                }
            }
            break;
        }
    }
}
