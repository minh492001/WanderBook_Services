package com.wander_book.service.Common;

import java.util.Base64;

public class Base64Decoder {
    public static byte[] decodeBase64(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
}
