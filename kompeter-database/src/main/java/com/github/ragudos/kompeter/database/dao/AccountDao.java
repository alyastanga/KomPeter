package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.AccountDto;
import com.github.ragudos.kompeter.database.dto.AccountDto.AccountPassword;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface AccountDao {
    /**
     * Create a new {@link AccountDto} and return an int.
     *
     * @return -1 if no account was created, otherwise return the _account_id.
     */
    int createAccount(
            @NotNull Connection conn,
            final int _userId,
            final @NotNull String email,
            final @NotNull String passwordHash,
            final @NotNull String passwordSalt)
            throws IOException, SQLException;

    /** Get the {@link AccountPassword}. This has the encoded hashed password and password salt. */
    Optional<AccountPassword> getAccountPassword(
            @NotNull Connection conn, final @NotNull String email) throws IOException, SQLException;

    boolean emailExists(@NotNull Connection conn, final @NotNull String email)
            throws IOException, SQLException;
}
