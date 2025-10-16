/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.cryptography;

import java.util.Base64;
import org.jetbrains.annotations.NotNull;

public record Salt(byte[] value) {
    public static Salt fromBase64(@NotNull final String encodedSalt) {
        return new Salt(Base64.getDecoder().decode(encodedSalt));
    }

    public static String toBase64(Salt salt) {
        return Base64.getEncoder().encodeToString(salt.value());
    }

    public String toBase64() {
        return Salt.toBase64(this);
    }
}
