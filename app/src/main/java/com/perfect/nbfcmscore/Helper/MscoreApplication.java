package com.perfect.nbfcmscore.Helper;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class MscoreApplication {
    private static MscoreApplication mInstance;
    public static void toastMessage(String message, int length) {
        //Toast.makeText(mInstance, message, length).show();
    }
    public static String encryptStart(String encypt) throws Exception {
        String te = encypt;

        String encrypted = encrypt(te);
        return encrypted;
    }
    private static String encrypt(String inputText) throws Exception {
        String s = "Agentscr";
        byte[] keyValue = s.getBytes("US-ASCII");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            KeySpec keySpec = new DESKeySpec(keyValue);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec(keyValue);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            bout.write(cipher.doFinal(inputText.getBytes("ASCII")));
        } catch (Exception e) {
            System.out.println("Exception .. " + e.getMessage());
        }

        return new String(Base64.encode(bout.toByteArray(), Base64.DEFAULT), "ASCII");

    }
    public static MscoreApplication getAppContext() {
        return mInstance;
    }

    
}
