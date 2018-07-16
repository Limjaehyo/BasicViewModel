package com.example.jaehyolim.viewmodel.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.arch.lifecycle.Lifecycle.State.STARTED;

public class DisposableLifecycleObserver implements LifecycleObserver {

    private boolean enabled = false;
    private  Lifecycle lifecycle;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private HashMap<String, Disposable> saveDisposableMap = new HashMap<>();

    public DisposableLifecycleObserver(Lifecycle lifecycle ) {
        this.lifecycle = lifecycle;
        lifecycle.addObserver(this);
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


    public void putDisposableMap(String tag, Disposable disposable) {
        if (saveDisposableMap.get(tag) == null) {
            mDisposable.add(disposable);
            saveDisposableMap.put(tag, disposable);
        }else{
            mDisposable.remove(saveDisposableMap.get(tag));
            saveDisposableMap.remove(tag);
            saveDisposableMap.put(tag, disposable);
            mDisposable.add(disposable);
        }

    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void stop() {
        saveDisposableMap.clear();
        mDisposable.dispose();
        mDisposable.clear();
        lifecycle.removeObserver(this);
        Log.e("DisposableLifecycleObse", "OnLifecycleEvent");

        // disconnect if connected
    }

}
