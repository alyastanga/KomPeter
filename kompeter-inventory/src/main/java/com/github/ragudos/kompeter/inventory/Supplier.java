/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.SupplierDto;
import java.util.List;
import java.util.Optional;



/**
 *
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
