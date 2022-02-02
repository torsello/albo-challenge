package com.albo.comics.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class HashGenerator {

    public static String generateHash(String timestamp, String publicKey, String privateKey) {
        byte[] toHash = timestamp.concat(privateKey).concat(publicKey).getBytes(StandardCharsets.UTF_8);
        return DigestUtils.md5DigestAsHex(toHash);
    }
}
