package com.github.ragudos.kompeter.pointofsale;
import java.time.LocalDateTime;
import java.util.ArrayList;

class Transaction {
    
    static int transCounter = 0;
    ArrayList<CartItem> cartItems = new ArrayList<>();
    int TransID;
    LocalDateTime TimeStamp;
    float Total;
    
    
    Transaction(Cart cart){
        this.TransID = ++transCounter;
        this.TimeStamp = LocalDateTime.now();
        for (CartItem item : cart.cI) {
            CartItem copy = new CartItem(item.productID, item.productName, item.qty, item.price);
            cartItems.add(copy);
        } ///Copies all the transaction and stores in a differnt array
        
        
        
        this.Total = cart.getCartTotal();
    }
    
}
