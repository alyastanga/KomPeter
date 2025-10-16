/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestLRU {
    @Test
    @DisplayName("Test LRU Cache containsKey()")
    void testCacheContainsKey() {
        var cache = new LRU<String, String>(2);

        assertEquals(cache.containsKey("ha"), false);

        cache.update("ha", "hatdog");

        assertEquals(cache.containsKey("ha"), true);
    }

    @Test
    @DisplayName("Test LRU Cache get()")
    void testCacheGet() {
        var cache = new LRU<String, String>(2);

        assertEquals(cache.get("ha"), null);

        cache.update("ha", "hatdog");

        assertEquals(cache.get("ha"), "hatdog");
    }

    @Test
    @DisplayName("Test LRU Cache update()")
    void testCacheUpdate() {
        var cache = new LRU<String, String>(2);

        cache.update("foo", "bar");

        assertEquals(cache.get("foo"), "bar");

        cache.update("deez", "nuts");
        cache.update("say", "what");

        assertEquals(cache.get("foo"), null);
    }
}
