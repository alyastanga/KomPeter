package com.github.ragudos.kompeter.auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.cryptography.HashedStringWithSalt;
import com.github.ragudos.kompeter.cryptography.Hasher;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.AccountDao;
import com.github.ragudos.kompeter.database.dao.UserDao;
import com.github.ragudos.kompeter.utilities.CharUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public final class Authentication {
	private static final Logger LOGGER = KompeterLogger.getLogger(Authentication.class);

	public static enum AuthenticationErrors {
		SIGN_UP_MISMATCH_PASSWORD("Passwords do not match"), INVALID_CREDENTIALS("Invalid credentials"),
		ACCOUNT_EXISTS("Account already exists"), SOMETHING_WENT_WRONG("Something went wrong");

		public String msg;

		private AuthenticationErrors(String msg) {
			this.msg = msg;
		}
	}

	public static final class AuthenticationException extends Exception {
		public AuthenticationErrors errorType;

		public AuthenticationException(AuthenticationErrors error) {
			super(error.msg);
		}
	}

	public static void signUp(final @NotNull String email, final @NotNull String displayName,
			final @NotNull char[] password, final @NotNull char[] confirmPassword) throws AuthenticationException {
		if (!CharUtils.constantTimeEquals(password, confirmPassword)) {
			throw new AuthenticationException(AuthenticationErrors.SIGN_UP_MISMATCH_PASSWORD);
		}

		AbstractSqlFactoryDao factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

		try {
			Connection conn = factory.getConnection();
			UserDao userDao = factory.getUserDao();
			AccountDao accountDao = factory.getAccountDao();

			if (userDao.displayNameTaken(conn, displayName) || accountDao.emailExists(conn, email)) {
				throw new AuthenticationException(AuthenticationErrors.ACCOUNT_EXISTS);
			}

			Optional<HashedStringWithSalt> maybeHashedPassword = Hasher.hash(password);

			if (maybeHashedPassword.isEmpty()) {
				throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
			}

			HashedStringWithSalt hashedPassword = maybeHashedPassword.get();

			Arrays.fill(password, '\0');
			Arrays.fill(password, '\0');

			conn.setAutoCommit(false);

			try {
				int _userId = userDao.createUser(conn, displayName);
				int _accountId = accountDao.createAccount(conn, _userId, email, hashedPassword.hashedStringToBase64(),
						hashedPassword.salt().toBase64());

				if (_accountId == -1) {
					throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
				}

				conn.commit();
			} catch (SQLException | IOException | AuthenticationException err) {
				conn.rollback();
				throw err;
			}
		} catch (SQLException err) {
			LOGGER.log(Level.SEVERE, "Failed to sign up", err);
		} catch (IOException err) {
			LOGGER.log(Level.SEVERE, "Failed to sign up", err);
		}
	}
}
