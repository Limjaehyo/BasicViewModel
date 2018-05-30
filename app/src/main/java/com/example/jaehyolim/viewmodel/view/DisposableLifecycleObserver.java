package com.example.jaehyolim.viewmodel.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.arch.lifecycle.Lifecycle.State.STARTED;

public class DisposableLifecycleObserver implements LifecycleObserver {

    private boolean enabled = false;
    private  Lifecycle lifecycle;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public DisposableLifecycleObserver(Lifecycle lifecycle ) {
        this.lifecycle = lifecycle;
        lifecycle.addObserver(this);
    }


    public void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        if (enabled) {
            // connect
        }
    }

    public void enable() {
        enabled = true;
        if (lifecycle.getCurrentState().isAtLeast(STARTED)) {
            // connect if not connected

        }
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void stop() {
        mDisposable.dispose();
        mDisposable.clear();
        lifecycle.removeObserver(this);
        Log.e("DisposableLifecycleObse", "OnLifecycleEvent");

        // disconnect if connected
    }

}
