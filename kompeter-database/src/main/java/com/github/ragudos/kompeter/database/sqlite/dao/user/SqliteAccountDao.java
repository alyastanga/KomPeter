package com.github.ragudos.kompeter.database.sqlite.dao.user;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.user.AccountDao;
import com.github.ragudos.kompeter.database.dto.user.AccountDto;
import com.github.ragudos.kompeter.database.dto.user.AccountDto.AccountPassword;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class SqliteAccountDao implements AccountDao {
    @Override
    public int createAccount(
            @NotNull Connection conn,
            int _userId,
            @NotNull String email,
            @NotNull String passwordHash,
            @NotNull String passwordSalt)
            throws IOException, SQLException {
        try (NamedPreparedStatement stmnt =
                new NamedPreparedStatement(
                        conn,
                        SqliteQueryLoader.getInstance().get("create_account", "accounts", SqlQueryType.INSERT),
                        Statement.RETURN_GENERATED_KEYS); ) {
            stmnt.setInt("_user_id", _userId);
            stmnt.setString("email", email);
            stmnt.setString("password_hash", passwordHash);
            stmnt.setString("password_salt", passwordSalt);

            stmnt.executeUpdate();

            ResultSet rs = stmnt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public Optional<AccountDto> getAccountByEmail(@NotNull Connection conn, @NotNull String email)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_password_account_by_email", "accounts", SqlQueryType.SELECT))) {
            stmnt.setString(1, email);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new AccountDto(
                                    rs.getInt("_account_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getInt("_user_id"),
                                    rs.getString("password_hash"),
                                    rs.getString("password_salt"),
                                    rs.getString("email")))
                    : Optional.empty();
        }
    }

    @Override
    public Optional<AccountPassword> getAccountPassword(
            @NotNull Connection conn, @NotNull String email) throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_password_hash", "accounts", SqlQueryType.SELECT))) {
            stmnt.setString(1, email);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new AccountPassword(rs.getString("password_hash"), rs.getString("password_salt")))
                    : Optional.empty();
        }
    }

    @Override
    public boolean emailExists(@NotNull Connection conn, @NotNull String email)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_email_exists", "accounts", SqlQueryType.SELECT))) {
            stmnt.setString(1, email);

            ResultSet rs = stmnt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }
}
