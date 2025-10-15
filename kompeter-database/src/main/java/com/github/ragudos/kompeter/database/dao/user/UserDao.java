/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.user;

import com.github.ragudos.kompeter.database.dto.user.UserDto;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface UserDao {
    /**
     * Creates a new {@link UserDto} and return an int
     *
     * @return -1 if no user was created, otherwise return the _user_id
     */
    int createUser(
            @NotNull Connection conn,
            @NotNull String displayName,
            @NotNull String firstName,
            @NotNull String lastName)
            throws IOException, SQLException;

    Optional<UserDto> getUserById(
            @NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _userId)
            throws IOException, SQLException;

    Optional<UserDto> getUserByDisplayName(@NotNull Connection conn, @NotNull String displayName)
            throws IOException, SQLException;

    Optional<UserDto> getUserByEmail(@NotNull Connection conn, @NotNull String email)
            throws IOException, SQLException;

    boolean displayNameTaken(@NotNull Connection conn, @NotNull String displayName)
            throws IOException, SQLException;
}
