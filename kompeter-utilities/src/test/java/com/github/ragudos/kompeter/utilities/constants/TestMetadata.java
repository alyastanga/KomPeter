/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestMetadata {
    @Test
    @DisplayName("Test Metadata Class")
    public void testMetadata() {
        assertEquals(System.getProperty("app.title"), Metadata.APP_TITLE);
        assertEquals(System.getProperty("app.version"), Metadata.APP_VERSION);
        assertEquals(System.getProperty("app.env"), Metadata.APP_ENV);
    }
}
