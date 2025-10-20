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
import java.util.Optional;

public interface SupplierDao {
    // CREATE
    int insertSupplier(
            String name,
            String email,
            String street,
            String city,
            String state,
            String postalCode,
            String country)
            throws SQLException, IOException;

    // READ
    List<SupplierDto> getAllSuppliers() throws SQLException, IOException;

    Optional<SupplierDto> getSupplierByID(int id) throws SQLException, IOException;
    // UPDATE
    // DELETE
}
