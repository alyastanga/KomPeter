/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public final class SqlScriptParser {
    public static record SqlStatement(
            @NotNull String type, @NotNull String name, @NotNull String sql) {}

    public static SqlStatement parseSqlStatement(String script) {
        String sql = script.trim();

        if (sql.isEmpty()) {
            return null;
        }

        Pattern pattern =
                Pattern.compile(
                        "(?i)\\b(CREATE|ALTER|DROP|INSERT|UPDATE|DELETE|TRUNCATE)\\s+(TABLE|DATABASE|INDEX|TRIGGER|VIEW)?\\s*`?(\\w+)`?",
                        Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String action = matcher.group(1).toUpperCase(); // e.g. CREATE
            String objectType = matcher.group(2); // e.g. TRIGGER
            String name = matcher.group(3); // e.g. update_trigger_status

            String type =
                    (objectType != null)
                            ? (action + " " + objectType.toUpperCase()) // e.g. CREATE TRIGGER
                            : action;

            return new SqlStatement(type, name, sql);
        }

        return new SqlStatement("UNKNOWN", "?", sql);
    }

    public static List<SqlStatement> parseSqlStatements(String script) {
        String[] parts = script.split("(?<=;)");
        List<SqlStatement> statements = new ArrayList<>();

        for (String raw : parts) {
            statements.add(parseSqlStatement(raw));
        }

        return statements;
    }
}
