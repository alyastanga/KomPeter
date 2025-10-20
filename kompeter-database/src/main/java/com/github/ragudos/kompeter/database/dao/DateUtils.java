/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Hanz Mapua
 */
public class DateUtils {

    public static Timestamp safeGetTimestamp(ResultSet rs, String column) throws SQLException {
        try {
            return rs.getTimestamp(column);
        } catch (SQLException e) {
            String s = rs.getString(column);
            if (s != null && s.matches("\\d{4}-\\d{2}-\\d{2}")) return Timestamp.valueOf(s + " 00:00:00");
            return null;
        }
    }
}
