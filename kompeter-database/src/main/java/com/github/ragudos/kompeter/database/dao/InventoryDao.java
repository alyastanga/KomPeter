/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.ragudos.kompeter.database.dao;

/**
 *
 * @author Peter M. Dela Cruz
 */
import com.github.ragudos.kompeter.database.dto.InventoryMetadataDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface InventoryDao {
    public enum Location{
       KOMPETER_DISPLAY_FLOOR,
       WAREHOUSE_A,
       WAREHOUSE_B,
       RECEIVING_DOCK;
       
       @Override
       public String toString(){
           return switch(this){
               case KOMPETER_DISPLAY_FLOOR  -> "Kompeter Display Floor";
               case WAREHOUSE_A -> "Warehouse A";
               case WAREHOUSE_B -> "Warehouse B";
               case RECEIVING_DOCK -> "Receiving Dock";
           };
       }
       
   }
    public enum OrderBy {
        ITEM_NAME,
        BRAND_NAME,
        CATEGORY_NAME,
        QUANTITY,
        PRICE;
        
        @Override
        public String toString(){
           return switch(this){
               case ITEM_NAME  -> "i.name";
               case BRAND_NAME -> "ib.name";
               case CATEGORY_NAME -> "ic.name";
               case QUANTITY -> "issl.quantity";
               case PRICE -> "ist.unit_price_php";
           };
       }
    }
    
    public enum Direction{
        ASC,
        DESC;     
    }
    
    // use this shit to not duplicate items because of location
    List<InventoryMetadataDto> getAllData(Location location, OrderBy orderBy, Direction direction) throws SQLException, IOException; 

}
