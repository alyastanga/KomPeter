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

/**
 * @author Peter M. Dela Cruz
 */
public record StorageLocationDto(
        int _storageLocationId, @NotNull Timestamp created_at, String name, String description) {}
