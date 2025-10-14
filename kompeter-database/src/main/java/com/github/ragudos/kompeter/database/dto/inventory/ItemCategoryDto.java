package com.github.ragudos.kompeter.database.dto.inventory;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record ItemCategoryDto(
        int _itemCategoryId, @NotNull Timestamp _createdAt, @NotNull String name, String description) {}
