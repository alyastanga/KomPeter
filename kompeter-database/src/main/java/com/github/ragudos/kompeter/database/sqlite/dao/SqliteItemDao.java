package com.github.ragudos.kompeter.database.sqlite.dao;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.dto.ItemDto;
import com.github.ragudos.kompeter.database.dao.ItemDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteItemDao implements ItemDao {
    private final Connection conn;
    
    public SqliteItemDao(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<ItemDto> getAllItems() throws SQLException, IOException {
        List<ItemDto> items = new ArrayList<>();
        var query = SqliteQueryLoader.getInstance().get("select_all_items", "items", SqlQueryType.SELECT);
                
        try(
            var stmt = conn.prepareStatement(query);
            var rs = stmt.executeQuery();){
            
            while(rs.next()){
                ItemDto item = new ItemDto(
                        rs.getInt("_item_id"),
                        rs.getTimestamp("_created_at"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public List<ItemDto> getItemsById(int id) throws SQLException, IOException {
        List<ItemDto> items = new ArrayList<>();
        
          var query = SqliteQueryLoader.getInstance().get("select_item_by_id", "items", SqlQueryType.SELECT);
   
        try(
            var stmt = conn.prepareStatement(query);){
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
          
            while(rs.next()){
                ItemDto item = new ItemDto(
                        rs.getInt("_item_id"),
                        rs.getTimestamp("_created_at"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                
                items.add(item);
            }
        }
        return items;
    }

    
}
