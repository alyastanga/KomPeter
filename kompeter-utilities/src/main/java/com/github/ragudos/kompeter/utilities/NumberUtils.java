/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public final class NumberUtils {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.##");
    public static final DecimalFormat shortFormatter = new DecimalFormat("0.##");
    public static final NumberFormat intFormatter = NumberFormat.getIntegerInstance(Locale.US);

    public static @NotNull String formatWithSuffix(double n) {
        var absolutevalue = Math.abs(n);
        char suffix;
        double shortenedValue;

        if (absolutevalue >= 1_000_000_000) {
            suffix = 'B';
            shortenedValue = n / 1_000_000_000.0;
        } else if (absolutevalue >= 1_000_000) {
            suffix = 'M';
            shortenedValue = n / 1_000_000.0;
        } else {
            return formatter.format(n);
        }

        return shortFormatter.format(shortenedValue) + suffix;
    }

    public static BigDecimal valueToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP);
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue()).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
