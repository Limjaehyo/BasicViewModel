package com.example.jaehyolim.viewmodel.repository;


import io.reactivex.Observable;

public interface getResponseInterFace<T> {
    Observable<T> getResponse();
}
