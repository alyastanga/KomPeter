package com.github.ragudos.kompeter.pointofsale;
import java.util.ArrayList;
 
class Cart{
    
    static int transID = 0;
    
    ArrayList<CartItem> cI = new ArrayList<>();
    
    void addItem(CartItem item){
        cI.add(item);
    } ////Connect to database
    
    void removeItem(String productID){
        for ( int i=0; i < cI.size() ; i++){
            if(cI.get(i).productID.equals(productID)){
                cI.remove(i); break;
            }
        }
    }
    
    float getCartTotal(){
        float sum=0;
        for ( int i=0; i < cI.size() ; i++){
                sum += cI.get(i).getTotalPrice();
            }
        return sum; 
    }
    
    void displayCart(){ ///change this wrong logic
        for ( int i=0; i < cI.size() ; i++){
               System.out.println(cI.get(i).productID);
               System.out.println(cI.get(i).productName);
               System.out.println(cI.get(i).price);
               System.out.println(cI.get(i).getTotalPrice());
               System.out.println("\n");
            }
        System.out.println("Grand Total: " + getCartTotal());
    }
    
    float checkOut(Cart currentCart){
        if(cI.isEmpty()){
            System.out.println("Cart is empty. Cannot checkout");
        }
        else{ ///wrong logic 
            float total = getCartTotal();
            displayCart();
            Transaction trans = new Transaction(currentCart);
            cI.clear();
            return total;
        }
        return 0;
    }
}

//// Learn Database handling