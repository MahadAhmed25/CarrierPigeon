package com.group12.carrierpigeon.controller;

import com.group12.carrierpigeon.networking.DataObject;

import javax.crypto.KeyGenerator;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;


public class Encryption {

    //actual encryption algorithm
    private static String encrypt(String plaintext, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding"); //initialize algorithm
            aes.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] message = aes.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(message);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;

        }
    }

    private String decrypt(String ciphertext, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aes.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] encrypted= Base64.getDecoder().decode(ciphertext);
            byte[] decrypted = aes.doFinal(encrypted);
            return new String(decrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private SecretKey decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }

    private IvParameterSpec decodeIV(String encodedIV) {
        byte[] decodedIV = Base64.getDecoder().decode(encodedIV);
        return new IvParameterSpec(decodedIV);
    }

    public byte[] encryptionAES(String[] input) {

        String plainText = input[0];
        String encodedKey = input[1];
        String encodedIV = input[2];

        SecretKey key = decodeKey(encodedKey);
        IvParameterSpec iv = decodeIV(encodedIV);
        String message = encrypt(plainText, key, iv);

        byte[] ciphertext = message.getBytes();

        return ciphertext;
    }

    public byte[] decryptionAES(String[] input) {
        String ciphertext = input[0];
        String encodedKey = input[1];
        String encodedIV = input[2];

        SecretKey key = decodeKey(encodedKey);
        IvParameterSpec iv = decodeIV(encodedIV);
        String message = decryptMessage(ciphertext, key, iv);

        byte[] plaintext = message.getBytes();

        return plaintext;
    }
}