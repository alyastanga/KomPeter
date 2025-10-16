/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestPasswordValidator {
    @Test
    @DisplayName("Test strong password pattern")
    void testStrongPasswordMathes() {
        var weak = "12345".toCharArray();
        var weak2 = "abcdef".toCharArray();
        var weak3 = "ABCDEF".toCharArray();
        var weak4 = "@$#$#%".toCharArray();
        var med = "1adadasd24234".toCharArray();
        var med2 = "SDFDFDR344523FD".toCharArray();
        var med3 = "@$#43423343$#@$".toCharArray();
        var med4 = "423432FfsdfsfsdfsFfsd".toCharArray();
        var med5 = "fdfds454$%%$#".toCharArray();
        var med6 = "FDSFSD42342$@#$#@".toCharArray();

        var strong = "123@heLoL".toCharArray();
        var strongweak = "1@Ll".toCharArray();

        assertFalse(PasswordValidator.isPasswordValid(weak, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(weak2, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(weak3, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(weak4, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(med, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(med2, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(med3, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(med4, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(med5, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(med6, PasswordValidator.STRONG_PASSWORD));
        assertFalse(PasswordValidator.isPasswordValid(strongweak, PasswordValidator.STRONG_PASSWORD));

        assertTrue(PasswordValidator.isPasswordValid(strong, PasswordValidator.STRONG_PASSWORD));
    }
}
