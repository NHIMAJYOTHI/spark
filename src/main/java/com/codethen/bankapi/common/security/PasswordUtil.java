package com.codethen.bankapi.common.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkHash(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
