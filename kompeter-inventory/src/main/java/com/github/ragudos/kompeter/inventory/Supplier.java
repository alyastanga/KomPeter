/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.SupplierDto;
import java.util.List;
import java.util.Optional;

/**
 * @author Peter M. Dela Cruz
 */
public interface Supplier {
    int addSupplier(
            String name,
            String email,
            String street,
            String city,
            String state,
            String postalCode,
            String country)
            throws InventoryException;

    List<SupplierDto> getAllSupplier() throws InventoryException;

    Optional<SupplierDto> getSupplierById(int id) throws InventoryException;
}
