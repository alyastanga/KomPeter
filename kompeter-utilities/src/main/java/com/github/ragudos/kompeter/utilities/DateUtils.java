/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.NotNull;

public final class DateUtils {
    public static enum DateFormat {
        ISO("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        ISO_DATE("yyyy/MM/dd"),
        ISO_TIME("HH:mm:ss"),
        DATE_TIME("yyyy-MM-dd HH:mm:ss");

        private final String format;

        DateFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    public static final String formatDate(
            @NotNull final String date, @NotNull final DateFormat format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.format);

        return LocalDateTime.parse(date, formatter).format(formatter);
    }

    public static final String getDateWithFormat() {
        return getDateWithFormat(DateFormat.ISO);
    }

    public static final String getDateWithFormat(@NotNull DateFormat dateFormat) {
        var now = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern(dateFormat.format);
        var formattedDate = now.format(formatter);

        return formattedDate;
    }
}
