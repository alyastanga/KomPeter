package com.github.ragudos.kompeter.database.dto.inventory;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record ItemDto(
        int _itemId, @NotNull Timestamp _createdAt, @NotNull String name, String description) {}
