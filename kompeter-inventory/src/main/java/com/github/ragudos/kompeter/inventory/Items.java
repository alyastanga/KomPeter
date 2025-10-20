/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.InventoryMetadataDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemBrandDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemCategoryDto;
import com.github.ragudos.kompeter.database.dto.inventory.ItemDto;
import java.util.List;
import java.util.Optional;

public interface Items {
    List<InventoryMetadataDto> showInventoryItems() throws InventoryException;

    List<InventoryMetadataDto> searchItem(String search) throws InventoryException;

    List<ItemDto> showAllItems() throws InventoryException;

    Optional<ItemDto> showItemById(int id) throws InventoryException;

    List<ItemBrandDto> showAllBrands() throws InventoryException;

    Optional<ItemBrandDto> showBrandsById(int id) throws InventoryException;

    List<ItemCategoryDto> showAllCategories() throws InventoryException;

    Optional<ItemCategoryDto> showCategoryById(int id) throws InventoryException;

    int deleteItem(int id) throws InventoryException;

    int updateItemName(String name, int id) throws InventoryException;

    int updateItemCategory(int categoryId, int itemId) throws InventoryException;

    int addItem(String name) throws InventoryException;

    int addItem(String name, String description) throws InventoryException;

    int addBrand(String name) throws InventoryException;

    int addBrand(String name, String description) throws InventoryException;

    int addCategory(String name) throws InventoryException;

    int addCategory(String name, String description) throws InventoryException;

    void setItemCategory(int _itemId, int _categoryId) throws InventoryException;
}
