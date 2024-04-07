package com.group12.carrierpigeon.components.chats;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class Chat {

    // everything in here right now is just for chat encryption
    // we all still need to work out the actual messaging stuff


    //each chat has its own key and IV for E2E encryption
    //incorporate this with encyrption controller
    private static final int KEY_SIZE = 128;
    private SecretKey key;
    private IvParameterSpec iv;

    public Chat() {
        // Add other properties here

        // Generate the encryption key and initialization vector
        key = generateKey(KEY_SIZE);
        iv = generateIV();
    }

    //n can be 128, 192, or 256 bits
    public static SecretKey generateKey(int n) {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    //generates the Initialization Vector (IV) that will be used in AES algorithm
    public static IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public SecretKey getKey() {
        return key;
    }

    public IvParameterSpec getIV() {
        return iv;
    }

}