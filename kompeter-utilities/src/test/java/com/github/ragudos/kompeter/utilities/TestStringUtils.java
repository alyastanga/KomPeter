/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestStringUtils {
    @Test
    void testSplitTrim() {
        String[] split = StringUtils.splitTrim("hi ,  hello  , what  , where", ",");

        for (String s : split) {
            assertTrue(!s.contains(" "));
        }
    }
}
