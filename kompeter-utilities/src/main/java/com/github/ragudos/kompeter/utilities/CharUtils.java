package com.github.ragudos.kompeter.utilities;

import java.util.Base64;
import org.jetbrains.annotations.NotNull;

public final class CharUtils {
    public static char[] byteArrayToCharArray(byte[] bArray) {
        char[] cArray = new char[bArray.length];
        for (int i = 0; i < bArray.length; ++i) {
            cArray[i] = (char) (bArray[i] & 0xFF);
        }

        return cArray;
    }

    public static byte[] charArrayToByteArray(char[] cArray) {
        byte[] bArray = new byte[cArray.length];

        for (int i = 0; i < cArray.length; ++i) {
            bArray[i] = (byte) (0xFF & cArray[i]);
        }

        return bArray;
    }

    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }

        int res = 0;

        for (int i = 0; i < a.length; ++i) {
            res |= a[i] ^ b[i];
        }

        return res == 0;
    }

    public static byte[] fromBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String toBase64(byte[] str) {
        return Base64.getEncoder().encodeToString(str);
    }

    public static boolean constantTimeEquals(@NotNull char[] a, @NotNull char[] b) {
        if (a.length != b.length) {
            return false;
        }

        int res = 0;

        for (int i = 0; i < a.length; ++i) {
            res |= a[i] ^ b[i];
        }

        return res == 0;
    }
}
