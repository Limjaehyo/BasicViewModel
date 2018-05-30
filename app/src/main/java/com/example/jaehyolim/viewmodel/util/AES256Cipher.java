package com.example.jaehyolim.viewmodel.util;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by CHO on 2015-10-14.
 */
public class AES256Cipher {
    private static final String SECRET_KEY = "5#n(y^a8Ad2vuiPXS2PGzVm2Pscv&~(j";
    public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static String AES_Encode(String str, String key) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        if(str == null) return "";
        byte[] textBytes = str.getBytes("UTF-8");
        Cipher cipher = null;
        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        } catch (Exception e) {

        }
        return Base64.encodeToString(cipher.doFinal(textBytes), 0);
    }

    public static String AES_Decode(String str, String key) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] textBytes = Base64.decode(str, 0);
        //byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }

    /**
     * AES Encode
     **/
    public static String encodeAES(String text) {
        String encodeText = "";
        try {
            encodeText = AES256Cipher.AES_Encode(text, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeText;
    }

    /**
     * AES Decode
     **/
    public static String decodeAES(String text) {
        String decodeText = "";
        try {
            decodeText = AES256Cipher.AES_Decode(text, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeText;
    }
}