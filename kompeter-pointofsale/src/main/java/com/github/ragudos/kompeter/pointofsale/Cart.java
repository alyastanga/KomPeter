package com.github.ragudos.kompeter.pointofsale;

import com.github.ragudos.kompeter.database.dao.SaleDao;
import java.sql.SQLException;
import java.util.ArrayList;

class Cart {
    SaleDao saleDao;
    static int transID = 0;

    ArrayList<CartItem> cI = new ArrayList<>();
    
    Cart (SaleDao saleDao) {
        this.saleDao = saleDao;
    }
    
    void addItem(CartItem item) {
        for (int i = 0; i < cI.size(); i++) {
            CartItem exist = cI.get(i);
            if(exist.productID() == item.productID()) {
                int newQty = exist.qty() + item.qty();
                cI.set(i, new CartItem(exist.productID(), exist.productName(), newQty, exist.price()));
                return;
            }
        }
        cI.add(item);
    } //// Connect to database
    void decItem(CartItem item) {
        for (int i = 0; i < cI.size(); i++) {
            CartItem exist = cI.get(i);
            if(exist.productID() == item.productID()) {
                int newQty = exist.qty() - item.qty();
                
                if(newQty > 0) {
                cI.set(i, new CartItem(exist.productID(), exist.productName(), newQty, exist.price()));
                } else { 
                    cI.remove(i);
                }  
                break;
            }
        } 
    }
    void removeItem(int productID) {
        for (int i = 0; i < cI.size(); i++) {
            if (cI.get(i).productID() == productID) {
                cI.remove(i);
                break;
            }
        }
    }

    float getCartTotal() {
        float sum = 0;
        for (int i = 0; i < cI.size(); i++) {
            sum += cI.get(i).getTotalPrice();
        }
        return sum;
    }

    void displayCart() { // /change this wrong logic
        for (int i = 0; i < cI.size(); i++) {
            System.out.println(cI.get(i).productID());
            System.out.println(cI.get(i).productName());
            System.out.println(cI.get(i).price());
            System.out.println(cI.get(i).getTotalPrice());
            System.out.println("\n");
        }
        System.out.println("Grand Total: " + getCartTotal());
    }

    float checkOut() throws SQLException {
        if (cI.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout");
        } else { /// wrong logic
            float total = getCartTotal();
            displayCart();
            
            Transaction trans = new Transaction(this, saleDao);
            trans.saveToDatabase();
            
            cI.clear();
            return total;
        }
        return 0;
    }
}
