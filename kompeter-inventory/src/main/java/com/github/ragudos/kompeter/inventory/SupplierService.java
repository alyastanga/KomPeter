/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.github.ragudos.kompeter.database.dto.inventory.SupplierDto;
import com.github.ragudos.kompeter.database.sqlite.dao.inventory.SqliteSupplierDao;

/**
 * @author Peter M. Dela Cruz
 */
public class SupplierService implements Supplier {
    private final SqliteSupplierDao sqliteSupplierDao;

    public SupplierService(SqliteSupplierDao sqliteSupplierDao) {
        this.sqliteSupplierDao = sqliteSupplierDao;
    }

    @Override
    public int addSupplier(String name, String email, String street, String city, String state, String postalCode,
            String country) throws InventoryException {
        try {
            return sqliteSupplierDao.insertSupplier(name, email, street, city, state, postalCode, country);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to add supplier in the database", e);
        }
    }

    @Override
    public List<SupplierDto> getAllSupplier() throws InventoryException {
        try {
            return sqliteSupplierDao.getAllSuppliers();
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to get all supplier in the database", e);
        }
    }

    @Override
    public Optional<SupplierDto> getSupplierById(int id) throws InventoryException {
        try {
            return sqliteSupplierDao.getSupplierByID(id);
        } catch (SQLException | IOException e) {
            throw new InventoryException("Failed to get supplier in the database for supplier id: " + id, e);
        }
    }
}
