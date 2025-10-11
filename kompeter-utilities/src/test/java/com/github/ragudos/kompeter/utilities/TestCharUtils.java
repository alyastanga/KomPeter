package com.github.ragudos.kompeter.utilities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestCharUtils {
    @Test
    @DisplayName("Test byte[] to char[]")
    void testByteArrToCharArr() {
        byte[] asciiBytes =
                new byte[] {
                    65, 66, 67, 68, 69, 70, 71, 72, 73, 74 // ASCII for 'A' to 'J'
                };
        char[] charBytes = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        assertTrue(Arrays.equals(CharUtils.byteArrayToCharArray(asciiBytes), charBytes));
    }

    @Test
    @DisplayName("Test char[] to byte[]")
    void testCharArrToByteArr() {
        byte[] asciiBytes =
                new byte[] {
                    65, 66, 67, 68, 69, 70, 71, 72, 73, 74 // ASCII for 'A' to 'J'
                };
        char[] charBytes = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        assertTrue(Arrays.equals(CharUtils.charArrayToByteArray(charBytes), asciiBytes));
    }

    @Test
    @DisplayName("Test [] comparison")
    void testComparison() {
        byte[] asciiBytes =
                new byte[] {
                    65, 66, 67, 68, 69, 70, 71, 72, 73, 74 // ASCII for 'A' to 'J'
                };
        char[] charBytes = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        char[] charBytes2 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        assertTrue(CharUtils.constantTimeEquals(asciiBytes, CharUtils.charArrayToByteArray(charBytes)));
        assertTrue(CharUtils.constantTimeEquals(charBytes, charBytes2));
    }
}
