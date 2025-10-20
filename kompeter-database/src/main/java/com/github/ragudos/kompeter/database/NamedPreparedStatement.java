/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A wrapper for {@link PreparedStatement} that allows named parameters in SQL.
 *
 * <p>This is useful to have a clearer and more readable SQL query file instead of using '?'.
 *
 * <p>It has the DELIMITER {@link NamedPreparedStatement.DELIMITER}
 */
public final class NamedPreparedStatement implements AutoCloseable {
    private final PreparedStatement prepStmt;
    private final Map<String, List<Integer>> fields = new HashMap<>();
    private final String parsedSql;

    private static final Pattern PARAM_PATTERN = Pattern.compile("[A-Za-z0-9_]+");

    public static final char DELIMITER = ':';

    public NamedPreparedStatement(final Connection conn, final String sql) throws SQLException {
        this(conn, sql, Statement.NO_GENERATED_KEYS);
    }

    public NamedPreparedStatement(final Connection conn, String sql, int autoGenerateKeysFlag)
            throws SQLException {
        final int SQL_QUERY_LENGTH = sql.length();
        StringBuilder parsedSqlBuilder = new StringBuilder();
        int jdbcStartingIndex = 1; // JDBC parameter index starts at 1
        int currentLoopIndex = 0;

        while (currentLoopIndex < SQL_QUERY_LENGTH) {
            final int DELIMITER_INDEX = sql.indexOf(DELIMITER, currentLoopIndex);

            if (DELIMITER_INDEX == -1) {
                parsedSqlBuilder.append(sql, currentLoopIndex, SQL_QUERY_LENGTH);

                break;
            }

            Matcher matcher = PARAM_PATTERN.matcher(sql);
            matcher.region(DELIMITER_INDEX + 1, SQL_QUERY_LENGTH);

            if (!matcher.find()) {
                throw new SQLException(
                        "Delimiter "
                                + DELIMITER
                                + " has no following name. Should be "
                                + DELIMITER
                                + "name_something");
            }

            final int PARAM_NAME_LAST_INDEX = matcher.end();

            parsedSqlBuilder.append(sql, currentLoopIndex, DELIMITER_INDEX);
            parsedSqlBuilder.append('?');

            fields
                    .computeIfAbsent(
                            sql.substring(DELIMITER_INDEX + 1, PARAM_NAME_LAST_INDEX), v -> new ArrayList<>())
                    .add(jdbcStartingIndex++);

            // Move to after the parameter
            currentLoopIndex = PARAM_NAME_LAST_INDEX;
        }

        parsedSql = parsedSqlBuilder.toString();
        prepStmt = conn.prepareStatement(parsedSql, autoGenerateKeysFlag);
    }

    public ResultSet executeQuery() throws SQLException {
        return prepStmt.executeQuery();
    }

    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }

    public void setInt(final String name, final int value) throws SQLException {
        List<Integer> positions = fields.get(name);
        if (positions == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        for (int pos : positions) {
            prepStmt.setInt(pos, value);
        }
    }

    public void setDouble(final String name, final double value) throws SQLException {
        List<Integer> positions = fields.get(name);
        if (positions == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        for (int pos : positions) {
            prepStmt.setDouble(pos, value);
        }
    }

    public void setBigDecimal(final String name, final BigDecimal value) throws SQLException {
        List<Integer> positions = fields.get(name);
        if (positions == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        for (int pos : positions) {
            prepStmt.setBigDecimal(pos, value);
        }
    }

    public void setString(final String name, final String value) throws SQLException {
        List<Integer> positions = fields.get(name);
        if (positions == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        for (int pos : positions) {
            prepStmt.setString(pos, value);
        }
    }

    public void setTimestamp(final String name, final Timestamp value) throws SQLException {
        List<Integer> positions = fields.get(name);
        if (positions == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        for (int pos : positions) {
            prepStmt.setTimestamp(pos, value);
        }
    }

    public int executeUpdate() throws SQLException {
        return prepStmt.executeUpdate();
    }

    public String getSql() {
        return parsedSql;
    }

    @Override
    public void close() throws SQLException {
        prepStmt.close();

        fields.clear();
    }
}
