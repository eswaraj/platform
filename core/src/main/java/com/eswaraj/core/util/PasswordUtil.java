package com.eswaraj.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;

@Component
public class PasswordUtil {

    public String encryptPassword(String password) throws ApplicationException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            byte[] digested = messageDigest.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationException(e);
        }
    }

    public boolean checkPassword(String textPassword, String encryptedPassword) throws ApplicationException {
        String convertredEncryptPassword = encryptPassword(textPassword);
        return convertredEncryptPassword.equals(encryptedPassword);
    }
}
