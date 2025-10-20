/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.sales;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader;
import com.github.ragudos.kompeter.database.dao.sales.SaleItemStockDao;
import com.github.ragudos.kompeter.database.dto.sales.SaleItemStockDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SqliteSaleItemStockDao implements SaleItemStockDao {

    
/* Nakagawa na si Hanz Dito
    @Override
    public List<SaleItemStockDto> getSalesUnitById(int _saleItemStockId, int _saleId) throws SQLException {
        List<SaleItemStockDto> list = new ArrayList<>();
        
        String sqlFileName ="select_by_sale_item_stock";
        String query;
        
        try {
            query = SqliteQueryLoader.getInstance().get(
                    sqlFileName, 
                    "sale_item_stock", 
                    AbstractSqlQueryLoader.SqlQueryType.SELECT
            );
            
        } catch (IOException e) {
            throw new SQLException("Error loading SQL file", e);
        }
        
        try (Connection con = SqliteFactoryDao.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
                
            ps.setInt(1, _saleItemStockId);
            ps.setInt(2, _saleId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SaleItemStockDto(
                         _saleItemStockId,
                         _saleId,
                         0,
                         new Timestamp(System.currentTimeMillis()),
                         rs.getInt("quantity"),
                         rs.getBigDecimal("unit_price_php")));
                    }
                }
            }
        return list;
    }

    @Override
    public List<SaleItemStockDto> getSalesUnitFrom(Timestamp from) throws SQLException {
       List<SaleItemStockDto> list = new ArrayList<>();
       
       String sqlFileName = "select_sale_item_from";
       String query;
       
       try { 
           query = SqliteQueryLoader.getInstance().get(
                   sqlFileName, 
                   "sale_item_stock", 
                   AbstractSqlQueryLoader.SqlQueryType.SELECT
           );  
       } catch (IOException e) {
           throw new SQLException ("Error Loading SQL File", e);
       }
       
       try (Connection con = SqliteFactoryDao.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
       
           ps.setTimestamp(1, from);
           
           try (ResultSet rs = ps.executeQuery()){
               while(rs.next()){
                   list.add(new SaleItemStockDto(
                   rs.getInt("_sale_item_stock_id"),
                   rs.getInt("_sale_id"),
                   0,
                   rs.getTimestamp("_created_at"),
                   rs.getInt("quantity"),
                   rs.getBigDecimal("unit_price_php")));
               }
           }
       }
       return list;
    }

    @Override
    public List<SaleItemStockDto> getSalesUnitByRange(Timestamp from, Timestamp to) throws SQLException {
        List<SaleItemStockDto> list = new ArrayList<>();
       
       String sqlFileName = "select_sale_item_by_range";
       String query;
       
       try { 
           query = SqliteQueryLoader.getInstance().get(
                   sqlFileName, 
                   "sale_item_stock", 
                   AbstractSqlQueryLoader.SqlQueryType.SELECT
           );  
       } catch (IOException e) {
           throw new SQLException ("Error Loading SQL File", e);
       }
       
       try (Connection con = SqliteFactoryDao.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
       
           ps.setTimestamp(1, from);
           ps.setTimestamp(2, to);
           
           try (ResultSet rs = ps.executeQuery()){
               while(rs.next()){
                   list.add(new SaleItemStockDto(
                   rs.getInt("_sale_item_stock_id"),
                   rs.getInt("_sale_id"),
                   0,
                   rs.getTimestamp("_created_at"),
                   rs.getInt("quantity"),
                   rs.getBigDecimal("unit_price_php")));
               }
           }
       }
       return list;
    }
*/
    @Override
    public List<SaleItemStockDto> getTopSellingItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SaleItemStockDto> getTopSellingItemsFrom(Timestamp from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SaleItemStockDto> getTopSellingItemsByRange(Timestamp from, Timestamp to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SaleItemStockDto> getSalesUnitById(int _saleItemStockId, int _saleId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getSalesUnitFrom(Timestamp from) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SaleItemStockDto> getSalesUnitByRange(Timestamp from, Timestamp to) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
