package com.group12.carrierpigeon.controller;

import android.provider.ContactsContract;

import com.group12.carrierpigeon.components.accounts.Account;
import com.group12.carrierpigeon.networking.DataObject;
import com.group12.carrierpigeon.threading.Publisher;
import com.group12.carrierpigeon.threading.Subscriber;
import com.group12.carrierpigeon.threading.Worker;

import javax.crypto.KeyGenerator;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;


public class Encryption extends Publisher<List<Object>> implements Subscriber<DataObject> {

    private Authentication authentication;

    public Encryption(Authentication authentication) {
        this.authentication = authentication;
        this.authentication.getAccount().subscribe(this);
    }

    public void getEncryptionDetails(String username, String recipient) {
        // First check ticket
        Account account = this.authentication.getAccount();
        account.handleDataResponseCommand(account.checkTicket, "CHECKTICKET");
        account.subscribe(this);
        // Contact server for encryption details
        account.handleDataResponseCommand(() -> {
            try {
                account.getOut().writeObject(new DataObject(DataObject.Status.VALID,("GETKEY:"+username+":"+recipient+":"+ new String(account.getTicket())).getBytes(StandardCharsets.UTF_8)));
                return (DataObject) account.getIn().readObject();
            } catch (Exception e) {
                return new DataObject(DataObject.Status.FAIL,null);
            }
            },"KEY");
    }

    public static List<Object> extractEncryptionInfo(DataObject object) {
        // Extract encryption details from DataObject
        byte[] enc = object.getData();
        // The first 16 bytes is the initialization vector
        // Technically it's 0 to 15 but last index in function is exclusive
        byte[] initVector = Arrays.copyOfRange(enc,0,16);
        byte[] encodedKey = Arrays.copyOfRange(enc,16,enc.length);

        SecretKeySpec key = new SecretKeySpec(encodedKey,"AES");
        IvParameterSpec iv = new IvParameterSpec(initVector);
        // Add key and iv to list
        List<Object> encInfo = new ArrayList<Object>() {{
            add(key);
            add(iv);
        }};

        return encInfo;
    }

    //actual encryption algorithm
    public static String encrypt(String plaintext, SecretKey key, IvParameterSpec iv) {
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

    public static String decrypt(String ciphertext, SecretKey key, IvParameterSpec iv) {
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

    @Override
    public void update(DataObject context, String whoIs) {
        if (whoIs != null && whoIs.contains("KEY") && context.getStatus().equals(DataObject.Status.VALID)) {
            // Extract encryption details from DataObject
            // Notify subscribers of class
            this.notifySubscribersInSameThread(extractEncryptionInfo(context),"ENCINFO");
        } else {
            this.notifySubscribersInSameThread(null,"ENCINFO-FAIL");
        }
    }

}