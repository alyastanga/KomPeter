/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.inventory;

import com.github.ragudos.kompeter.database.dao.inventory.SupplierDao;
import com.github.ragudos.kompeter.database.dto.inventory.SupplierDto;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SqliteSupplierDao implements SupplierDao {
    private final Connection conn;
    
    public SqliteSupplierDao(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insertSupplier(String name, String email, String street, String city, String state, String postalCode, String country) throws SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SupplierDto> getAllSuppliers() throws SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<SupplierDto> getSupplierByID(int id) throws SQLException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
