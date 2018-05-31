package com.example.jaehyolim.viewmodel.repository;

import com.example.jaehyolim.viewmodel.BuildConfig;

/**
 * ViewModel -> Repository 동신을위한 최상위 Repository
 * @param <T>
 */
public abstract class BaseRepository<T extends BaseModel > implements getResponseInterFace<T> {


    protected String isTest() {
        return  BuildConfig.DEBUG ? "Y" : "N";
    }


}
