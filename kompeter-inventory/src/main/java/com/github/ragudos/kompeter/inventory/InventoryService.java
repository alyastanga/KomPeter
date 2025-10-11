/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.inventory;
/**
 *
 * @author Peter M. Dela Cruz
 * 
 */
import com.github.ragudos.kompeter.database.sqlite.dao.SqliteItemDao;
import com.github.ragudos.kompeter.database.dto.ItemDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InventoryService {
    private final SqliteItemDao sqlItemDao;
    
    public InventoryService(SqliteItemDao sqlItemDao){
        this.sqlItemDao = sqlItemDao;
    }
    
    public List<ItemDto> getAllItem(){
        try{
            return sqlItemDao.getAllItems();
        }catch(SQLException e){
            throw new RuntimeException("Database error occurred while retrieving all items.", e);
        }catch(IOException e){
            throw new RuntimeException("Configuration/File error while retrieving query for all items.", e);
        }
    }
}
