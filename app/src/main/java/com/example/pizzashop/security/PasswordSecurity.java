package com.example.pizzashop.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * Creates and verifies local password hashes for the portfolio app.
 * The format is: pbkdf2$iterations$base64Salt$base64Hash
 */
public final class PasswordSecurity {

    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;

    private PasswordSecurity() {
    }

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        byte[] salt = new byte[SALT_LENGTH_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(plainPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);

        return PREFIX
                + "$" + ITERATIONS
                + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String plainPassword, String storedCredential) {
        if (plainPassword == null || storedCredential == null || storedCredential.isEmpty()) {
            return false;
        }

        if (!isHashed(storedCredential)) {
            return MessageDigest.isEqual(
                    plainPassword.getBytes(StandardCharsets.UTF_8),
                    storedCredential.getBytes(StandardCharsets.UTF_8)
            );
        }

        String[] parts = storedCredential.split("\\$");
        if (parts.length != 4) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
            byte[] actualHash = pbkdf2(plainPassword.toCharArray(), salt, iterations, expectedHash.length * 8);
            return MessageDigest.isEqual(actualHash, expectedHash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isHashed(String storedCredential) {
        return storedCredential != null && storedCredential.startsWith(PREFIX + "$");
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to hash password.", ex);
        }
    }
}