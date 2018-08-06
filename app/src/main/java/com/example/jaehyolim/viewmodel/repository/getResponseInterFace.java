package com.example.jaehyolim.viewmodel.repository;


import java.util.Map;

import io.reactivex.Observable;

public interface getResponseInterFace<T> {
    Observable<T> getResponse();
    Observable<T> getResponse(String... age);
    Map<String, String> queryValuesMap();
}
