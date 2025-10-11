/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.ragudos.kompeter.inventory;
/**
 *
 * @author Peter M. Dela Cruz
 */
import com.github.ragudos.kompeter.database.dto.ItemDto;
import java.util.List;

public class InventoryController {
    private final InventoryService is;
    private final InventoryView iv;
    
    public InventoryController(InventoryService is, InventoryView iv){
        this.is = is;
        this.iv = iv;
    }
    
    public void loadItems(){
        try{
            List<ItemDto> items = is.getAllItem();
            
                    
        }catch(RuntimeException e){
            
        }
    }

}
