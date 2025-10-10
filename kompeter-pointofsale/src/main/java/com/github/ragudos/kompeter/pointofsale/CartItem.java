package com.github.ragudos.kompeter.pointofsale;

public class CartItem {
    String productID;
    String productName;
    int qty;
    float price;
    
    CartItem(String productID, String productName, int qty, float price){
        this.productID = productID;
        this.productName = productName;
        this.qty = qty;
        this.price = price;
    }
    void setQuantity(int qty){
        this.qty = qty;
    }
    
    void setPrice(float price){
        this.price = price;  
    }

    public float getTotalPrice(){
        return qty * price;
    }
}