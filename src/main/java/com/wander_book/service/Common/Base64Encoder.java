package com.wander_book.service.Common;

import java.util.Base64;

public class Base64Encoder {
    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
