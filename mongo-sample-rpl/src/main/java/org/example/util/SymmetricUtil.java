package org.example.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

public class SymmetricUtil {

    private static String key = "1762169882481234";

    public static String encode(String data) {
        byte[] bytesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded();
        AES aes = SecureUtil.aes(bytesKey);
        return aes.encryptBase64(data);
    }

    public static String decode(String data) {
        byte[] bytesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded();
        AES aes = SecureUtil.aes(bytesKey);
        return aes.decryptStr(data);
    }
}
