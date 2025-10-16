/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.cryptography;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestHasher {
    @Test
    @DisplayName(
            "Tests hashing algorithm produces different hashes for the same input with different salts")
    void testPasswordHashing() {
        char[] p = "123456".toCharArray();
        Salt s = SaltFactory.generateSalt();
        Salt s2 = SaltFactory.generateSalt();

        assertFalse(Hasher.hash(p, s).get().equalsHashedString(Hasher.hash(p, s2).get()));
        assertFalse(Hasher.hash(p).get().equalsHashedString(Hasher.hash(p).get()));
    }

    @Test
    @DisplayName("Tests hasher to produce different hashes for different inputs with same salt.")
    void testPasswordHashingDifferences() {
        Salt s = SaltFactory.generateSalt();
        char[] p = "123456".toCharArray();
        char[] p2 = "654321".toCharArray();

        assertFalse(Hasher.hash(p, s).get().equalsHashedString(Hasher.hash(p2, s).get()));
    }

    @Test
    @DisplayName("Tests hasher to produce same hash for same input and salt")
    void testPasswordHashingSimilarity() {
        Salt s = SaltFactory.generateSalt();
        char[] p = "123456".toCharArray();

        assertTrue(Hasher.hash(p, s).get().equalsHashedString(Hasher.hash(p, s).get()));
    }
}
