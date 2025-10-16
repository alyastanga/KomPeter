/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.ragudos.kompeter.cryptography;

/**
 *
 * @author Peter M. Dela Cruz
 */
import java.security.SecureRandom;
import java.util.Base64;

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
