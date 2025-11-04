/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.inventory;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemBrandDto {
    @NotNull
    Timestamp _createdAt;
    int _itemBrandId;
    String description;
    @NotNull
    String name;
}
