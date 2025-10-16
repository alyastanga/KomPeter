/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.constants;

import org.jetbrains.annotations.Range;

public final class StringLimits {
    public static final record StringLimit(
            @Range(from = 0, to = Integer.MAX_VALUE) int min,
            @Range(from = 0, to = Integer.MAX_VALUE) int max) {}
    ;

    public static final StringLimit DISPLAY_NAME = new StringLimit(2, 64);
    public static final StringLimit FIRST_NAME = new StringLimit(2, 64);
    public static final StringLimit LAST_NAME = new StringLimit(2, 64);
}
