/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.cryptography;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSalt {
    @Test
    @DisplayName("Test encoded salt is decoded the same")
    void testSaltEncoding() {
        Salt s = SaltFactory.generateSalt();
        Salt decodedEncoded = Salt.fromBase64(s.toBase64());

        assertTrue(Arrays.equals(s.value(), decodedEncoded.value()));
    }
}
