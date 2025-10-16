/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.SupplierDto;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface SupplierDao {
    //CREATE
    void insertSupplier(String name, String email, String street, String city, String state, String postalCode, String country) throws SQLException, IOException;
    //READ
    List<SupplierDto> getAllSuppliers() throws SQLException, IOException;
    List<SupplierDto> getSupplierByID(int id) throws SQLException, IOException;
    //UPDATE
    //DELETE
}
