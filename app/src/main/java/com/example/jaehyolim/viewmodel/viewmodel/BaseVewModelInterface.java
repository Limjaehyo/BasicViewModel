package com.example.jaehyolim.viewmodel.viewmodel;

import io.reactivex.disposables.Disposable;

public interface BaseVewModelInterface {
    void putDisposableMap(String tag, Disposable disposable);

    void showMessageDialog(String msg);


}
