package com.github.ragudos.kompeter.database.dto.user;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record UserRoleDto(
        int _userRoleId, int _userId, int _roleId, @NotNull Timestamp _createdAt) {}
