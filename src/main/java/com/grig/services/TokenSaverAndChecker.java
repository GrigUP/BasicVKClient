package com.grig.services;

import java.io.*;

public class TokenSaverAndChecker {

    public static String getTokenFromFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);

        byte[] buffer = new byte[fileInputStream.available()];
        while (fileInputStream.available() > 0) {
            fileInputStream.read(buffer);
        }
        fileInputStream.close();

        String bufferString = new String(buffer);
        return bufferString;
    }

    public static void writeTokenToFile(String filePath, Token token) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        String sendedString = String.format("access_token=%s&expires_id=%d&user_id=%d", token.getAccess_token(), token.getExpires_in(), token.getUser_id());
        fileOutputStream.write(sendedString.getBytes());
    }
}
