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

    //need to incorporate this with messages

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
}