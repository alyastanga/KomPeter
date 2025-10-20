/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import java.util.List;

public interface Items {
    List<InventoryMetadataDto> showInventoryItems() throws InventoryException;

    List<InventoryMetadataDto> searchItem(String search) throws InventoryException;

    void deleteItem(int id) throws InventoryException;

    int updateItemName(String name, int id) throws InventoryException;

    int updateItemBrand(int brandId, int itemId) throws InventoryException;

    int updateItemCategory(int categoryId, int itemId) throws InventoryException;

    int addItem(String name) throws InventoryException;

    int addItem(String name, String description) throws InventoryException;

    int addBrand(String name) throws InventoryException;

    int addBrand(String name, String description) throws InventoryException;

    int addCategory(String name) throws InventoryException;

    int addCategory(String name, String description) throws InventoryException;

    void setItemCategory(int _itemId, int _categoryId) throws InventoryException;
}
