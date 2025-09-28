package com.mis7ake7411.tddprojectdemo.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.jasypt.util.text.BasicTextEncryptor;

public class AesEncryptor {
  private static final String KEY = "DocManager123456";

  public static void main(String[] args) throws Exception {
//    String plaintext = "771414"; // 明文密碼
//    String encrypted = encrypt(plaintext, KEY);
//    System.out.println("Encrypted password: " + encrypted);

    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    encryptor.setPassword("DocManager123456"); // 加密密鑰
    String encrypted = encryptor.encrypt("771414"); // 明文密碼
    System.out.println("ENC(" + encrypted + ")");
  }

  public static String encrypt(String plainText, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encrypted);
  }
}

