/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.inventory.StorageLocationDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuantityMetadata {
    int qty;
    StorageLocationDto storageLocation;
}
