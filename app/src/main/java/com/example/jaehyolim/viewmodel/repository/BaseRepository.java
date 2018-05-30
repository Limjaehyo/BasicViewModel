package com.example.jaehyolim.viewmodel.repository;

import com.example.jaehyolim.viewmodel.BuildConfig;
import com.example.jaehyolim.viewmodel.util.AES256Cipher;

/**
 * ViewModel -> Repository 동신을위한 최상위 Repository
 * @param <T>
 */
public abstract class BaseRepository<T extends BaseModel > implements getResponseInterFace<T> {


    protected String apiEncryption(String value) {
        String apiURL = value;
        if (!BuildConfig.DEBUG) {
            try {
                apiURL = AES256Cipher.encodeAES(value).trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return apiURL == null ? "" : apiURL.trim();
    }
    protected String isTest() {
        return  BuildConfig.DEBUG ? "Y" : "N";
    }


}
