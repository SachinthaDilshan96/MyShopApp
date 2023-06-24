package com.devstack.pos.util;

import org.mindrot.BCrypt;

public class PasswordManager {
    public static String encryptPassword(String plainText ){
       return BCrypt.hashpw(plainText,BCrypt.gensalt(10));
    }
    public static boolean checkPassword(String plainText,String hashedPassword){
        return BCrypt.checkpw(plainText,hashedPassword);
    }
}
