package com.github.ragudos.kompeter.database.dao.user;

import com.github.ragudos.kompeter.database.dto.user.AccountDto;
import com.github.ragudos.kompeter.database.dto.user.AccountDto.AccountPassword;
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
            int _userId,
            @NotNull String email,
            @NotNull String passwordHash,
            @NotNull String passwordSalt)
            throws IOException, SQLException;

    Optional<AccountDto> getAccountByEmail(@NotNull Connection conn, @NotNull String email)
            throws IOException, SQLException;

    /** Get the {@link AccountPassword}. This has the encoded hashed password and password salt. */
    Optional<AccountPassword> getAccountPassword(@NotNull Connection conn, @NotNull String email)
            throws IOException, SQLException;

    boolean emailExists(@NotNull Connection conn, @NotNull String email)
            throws IOException, SQLException;
}
