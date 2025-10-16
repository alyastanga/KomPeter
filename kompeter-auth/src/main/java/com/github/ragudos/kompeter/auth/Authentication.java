/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.auth;

import com.github.ragudos.kompeter.configurations.ApplicationConfig;
import com.github.ragudos.kompeter.cryptography.HashedStringWithSalt;
import com.github.ragudos.kompeter.cryptography.Hasher;
import com.github.ragudos.kompeter.cryptography.Salt;
import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.database.dao.user.AccountDao;
import com.github.ragudos.kompeter.database.dao.user.SessionDao;
import com.github.ragudos.kompeter.database.dao.user.UserDao;
import com.github.ragudos.kompeter.database.dao.user.UserRoleDao;
import com.github.ragudos.kompeter.database.dto.user.AccountDto.AccountPassword;
import com.github.ragudos.kompeter.database.dto.user.SessionDto;
import com.github.ragudos.kompeter.database.dto.user.UserDto;
import com.github.ragudos.kompeter.database.dto.user.UserMetadataDto;
import com.github.ragudos.kompeter.utilities.CharUtils;
import com.github.ragudos.kompeter.utilities.constants.PropertyKey;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public final class Authentication {
    private static final Logger LOGGER = KompeterLogger.getLogger(Authentication.class);

    public static enum AuthenticationErrors {
        SIGN_UP_MISMATCH_PASSWORD("Passwords do not match"),
        INVALID_CREDENTIALS("Invalid credentials"),
        ACCOUNT_EXISTS("Account already exists"),
        SOMETHING_WENT_WRONG("Something went wrong");

        public String msg;

        private AuthenticationErrors(String msg) {
            this.msg = msg;
        }
    }

    public static final class AuthenticationException extends Exception {
        public AuthenticationErrors errorType;

        public AuthenticationException() {
            super(AuthenticationErrors.SOMETHING_WENT_WRONG.msg);
            errorType = AuthenticationErrors.SOMETHING_WENT_WRONG;
        }

        public AuthenticationException(AuthenticationErrors error) {
            super(error.msg);
        }
    }

    /** Signs the user in if there's an existing session token and it isn't expired */
    public static void signInFromStoredSessionToken() throws AuthenticationException {
        String sessionToken = ApplicationConfig.getInstance().getProperty(PropertyKey.Session.UID);

        if (sessionToken == null) {
            LOGGER.info("No stored session token");
            return;
        }

        AbstractSqlFactoryDao factory =
                AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        try (Connection conn = factory.getConnection(); ) {
            SessionDao sessionDao = factory.getSessionDao();
            SessionDto sessionDto =
                    sessionDao
                            .getSessionByToken(conn, sessionToken)
                            .orElseThrow(AuthenticationException::new);

            if (Session.isExpired(sessionDto.expiresAt())) {
                sessionDao.removeSessionByToken(conn, sessionToken);
                ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);
            }
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Cannot sign in from stored session token", err);
            throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
        }
    }

    public static void signOut() throws AuthenticationException {
        String sessionToken = SessionManager.getInstance().session().sessionToken();
        AbstractSqlFactoryDao factory =
                AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        try (Connection conn = factory.getConnection(); ) {
            conn.setAutoCommit(false);

            SessionDao sessionDao = factory.getSessionDao();

            try {
                sessionDao.removeSessionByToken(conn, sessionToken);
                ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);
                SessionManager.getInstance().removeSession();

                conn.commit();
            } catch (SQLException | IOException err) {
                try {
                    conn.rollback();
                } catch (SQLException err2) {
                    err.addSuppressed(err2);
                }

                throw err;
            }
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Cannot sign out", err);
            throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
        }
    }

    public static void signIn(final @NotNull String email, final @NotNull char[] password)
            throws AuthenticationException {
        AbstractSqlFactoryDao factory =
                AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        try (Connection conn = factory.getConnection(); ) {
            UserDao userDao = factory.getUserDao();
            AccountDao accountDao = factory.getAccountDao();
            SessionDao sessionDao = factory.getSessionDao();
            UserRoleDao userRoleDao = factory.getUserRoleDao();

            if (!accountDao.emailExists(conn, email)) {
                throw new AuthenticationException(AuthenticationErrors.INVALID_CREDENTIALS);
            }

            /* ===== Check if account password's correct ===== */

            AccountPassword accountPassword =
                    accountDao
                            .getAccountPassword(conn, email)
                            .orElseThrow(
                                    () -> new AuthenticationException(AuthenticationErrors.INVALID_CREDENTIALS));
            HashedStringWithSalt hashedPassword =
                    Hasher.hash(password, Salt.fromBase64(accountPassword.passwordSalt()))
                            .orElseThrow(AuthenticationException::new);
            byte[] accountPasswordBytes = Base64.getDecoder().decode(accountPassword.passwordHash());

            // Not so sescure using the stringified Base64 but it's whatever
            if (!CharUtils.constantTimeEquals(hashedPassword.hashedString(), accountPasswordBytes)) {
                throw new AuthenticationException(AuthenticationErrors.INVALID_CREDENTIALS);
            }

            hashedPassword.clearHashedStringBytes();
            Arrays.fill(password, '\0');
            Arrays.fill(accountPasswordBytes, (byte) 0);

            /* ===== Sign in ===== */

            UserDto userDto =
                    userDao.getUserByEmail(conn, email).orElseThrow(AuthenticationException::new);

            conn.setAutoCommit(false);

            try {
                int _sessionId = sessionDao.createSession(conn, userDto._userId());

                if (_sessionId == -1) {
                    throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
                }

                SessionDto sessionDto =
                        sessionDao.getSessionById(conn, _sessionId).orElseThrow(AuthenticationException::new);

                UserMetadataDto userMetadataDto =
                        new UserMetadataDto(
                                userDto._userId(),
                                userDto._createdAt(),
                                userDto.displayName(),
                                userDto.firstName(),
                                userDto.lastName(),
                                userRoleDao
                                        .getRolesOfUserById(conn, userDto._userId())
                                        .orElseThrow(AuthenticationException::new),
                                email);

                ApplicationConfig.getInstance()
                        .getConfig()
                        .setProperty(PropertyKey.Session.UID, sessionDto.sessionToken());
                SessionManager.getInstance()
                        .setSession(
                                new Session(
                                        userMetadataDto,
                                        sessionDto.sessionToken(),
                                        sessionDto.expiresAt(),
                                        sessionDto.ipAddress()));

                conn.commit();
            } catch (SQLException | IOException | AuthenticationException err) {
                try {
                    conn.rollback();
                } catch (SQLException err2) {
                    err.addSuppressed(err2);
                }

                throw err;
            }
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to sign in ", err);
            throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
        }
    }

    public static void signUp(
            final @NotNull String displayName,
            final @NotNull String firstName,
            final @NotNull String lastName,
            final @NotNull String email,
            final @NotNull char[] password,
            final @NotNull char[] confirmPassword)
            throws AuthenticationException {
        if (!CharUtils.constantTimeEquals(password, confirmPassword)) {
            throw new AuthenticationException(AuthenticationErrors.SIGN_UP_MISMATCH_PASSWORD);
        }

        AbstractSqlFactoryDao factory =
                AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.SQLITE);

        try (Connection conn = factory.getConnection(); ) {
            UserDao userDao = factory.getUserDao();
            AccountDao accountDao = factory.getAccountDao();

            if (userDao.displayNameTaken(conn, displayName) || accountDao.emailExists(conn, email)) {
                throw new AuthenticationException(AuthenticationErrors.ACCOUNT_EXISTS);
            }

            HashedStringWithSalt hashedPassword =
                    Hasher.hash(password).orElseThrow(AuthenticationException::new);

            Arrays.fill(password, '\0');
            Arrays.fill(password, '\0');

            conn.setAutoCommit(false);

            try {
                int _userId = userDao.createUser(conn, displayName, firstName, lastName);

                if (_userId == -1) {
                    throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
                }

                int _accountId =
                        accountDao.createAccount(
                                conn,
                                _userId,
                                email,
                                hashedPassword.hashedStringToBase64(),
                                hashedPassword.salt().toBase64());

                hashedPassword.clearHashedStringBytes();

                if (_accountId == -1) {
                    throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
                }

                conn.commit();
            } catch (SQLException | IOException | AuthenticationException err) {
                try {
                    conn.rollback();
                } catch (SQLException err2) {
                    err.addSuppressed(err2);
                }

                throw err;
            }
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to sign up", err);

            throw new AuthenticationException(AuthenticationErrors.SOMETHING_WENT_WRONG);
        }
    }
}
