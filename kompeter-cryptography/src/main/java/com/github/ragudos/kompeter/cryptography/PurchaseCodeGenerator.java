/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.cryptography;

/**
 * @author Peter M. Dela Cruz
 */
import java.security.SecureRandom;

public class PurchaseCodeGenerator {

    public static String generateSecureHexToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        // Convert the random bytes to a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {

            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
