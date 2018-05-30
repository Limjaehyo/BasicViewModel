package com.example.jaehyolim.viewmodel.viewmodel;

import io.reactivex.disposables.CompositeDisposable;

public interface BaseVewModelInterface {
    CompositeDisposable getDisposable();

    void showMessageDialog(String msg);


}
