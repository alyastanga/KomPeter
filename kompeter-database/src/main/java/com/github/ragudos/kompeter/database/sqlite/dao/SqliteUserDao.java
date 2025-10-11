package com.github.ragudos.kompeter.database.sqlite.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.UserDao;
import com.github.ragudos.kompeter.database.dto.UserDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;

public class SqliteUserDao implements UserDao {
	@Override
	public int createUser(@NotNull Connection conn, @NotNull String displayName) throws IOException, SQLException {
		try (NamedPreparedStatement stmnt = new NamedPreparedStatement(conn,
				SqliteQueryLoader.getInstance().get("create_user", "users", SqlQueryType.INSERT),
				Statement.RETURN_GENERATED_KEYS)) {
			stmnt.setString("display_name", displayName);

			ResultSet rs = stmnt.getPreparedStatement().getGeneratedKeys();

			return rs.next() ? rs.getInt(1) : -1;
		}
	}

	@Override
	public Optional<UserDto> getUserById(@NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId)
			throws IOException, SQLException {
		try (PreparedStatement stmnt = conn.prepareStatement(
				SqliteQueryLoader.getInstance().get("get_user_by_id", "users", SqlQueryType.SELECT))) {
			stmnt.setInt(1, _userId);

			ResultSet rs = stmnt.getResultSet();

			return rs.next() ? Optional.of(new UserDto(_userId, rs.getTimestamp("_created_at"),
					rs.getString("display_name"), rs.getString("first_name"), rs.getString("last_name")))
					: Optional.empty();
		}
	}

	@Override
	public boolean displayNameTaken(@NotNull Connection conn, @NotNull String displayName)
			throws IOException, SQLException {
		try (PreparedStatement stmnt = conn.prepareStatement(
				SqliteQueryLoader.getInstance().get("select_display_name_taken", "users", SqlQueryType.SELECT))) {
			stmnt.setString(1, displayName);

			ResultSet rs = stmnt.getResultSet();

			return rs.next() && rs.getInt(1) != 0;
		}
	}
}
