/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.ragudos.kompeter.database.sqlite;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.inventory.InventoryDao;
import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteInventoryDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Peter M. Dela Cruz
 */
public class TestSqliteInventoryDao {
    @Test
    @DisplayName("Test Sqlite Inventory Dao")
    void testInventoryDao(){
        var dao = new SqliteInventoryDao(AbstractSqlFactoryDao.getSqlFactoryDao
                                    (AbstractSqlFactoryDao.SQLITE)
                                                .getConnection());
        
       List<InventoryMetadataDto> inventory = null;
       try{
           inventory = dao.getAllData(null, null , null);
           for(InventoryMetadataDto item : inventory){
               System.out.println("Item Name: " + item._itemId() + ", Quantity: " + item.quantity());
           }
       }catch(SQLException e){
           e.printStackTrace();
       }catch(IOException e){
           e.printStackTrace();
       }
    }
    
}
