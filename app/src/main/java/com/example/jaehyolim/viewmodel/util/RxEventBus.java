package com.example.jaehyolim.viewmodel.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Author : jaehyolim
 * Created on Date : 2018. 7. 31.
 * RxEventBus
 */

public class RxEventBus {
    private Subject<Object> mPublishSubject;
    private Subject<Object> mBehaviorSubject;

    private static volatile RxEventBus INSTANCE = null;

    private Map<String, Disposable> map = new HashMap<>();

    private RxEventBus() {
        mPublishSubject = PublishSubject.create().toSerialized();
        mBehaviorSubject = BehaviorSubject.create().toSerialized();
    }

    public static synchronized RxEventBus getInstance() {
        if (INSTANCE == null) {
            synchronized (RxEventBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxEventBus();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * class type -> register subscriber(listener)
     *
     * @param listener
     * @param type
     * @param <E>
     */
    public <E> void registerListener(String key, OnEventListener<E> listener, Class<E> type) {
        final Disposable subscription = mPublishSubject.ofType(type) // filter

                .subscribe(listener::onEvent, throwable -> {
                    Log.e("EventBus", throwable.getMessage(), throwable);
                });
        map.put(key, subscription);
    }

    public <E> void oneEventRegisterListener(String key, OnEventListener<E> listener, Class<E> type) {
        if (map.get(key) != null) {
            map.get(key).dispose();
            map.remove(key);
        }
        final Disposable subscription = mBehaviorSubject.ofType(type) // filter
                .subscribe(event -> {
                    listener.onEvent(event);
                    unregisterListener(key);
                }, throwable -> {
                    Log.e("EventBus", throwable.getMessage(), throwable);

                });
        map.put(key, subscription);
    }

    public void unregisterListener(String key) {
        final Disposable disposable = map.get(key);
        disposable.dispose();
        if (map.containsKey(key)) {
            map.remove(key);
        }
    }

    /**
     * push an event to the queue(Subject)
     *
     * @param event
     */
    public void postEvent(Object event) { // push event to queue
        mPublishSubject.onNext(event);
    }

    public void postOneEvent(Object event) { // push event to queue
        mBehaviorSubject.onNext(event);
    }

    public Observable<Object> getPublishObservable() {
        return mPublishSubject;
    }
    public Observable<Object> getBegaviorObservable() {
        return mBehaviorSubject;
    }

    /**
     * peek an event from the queue(Subject)
     *
     * @param clazz
     * @param handler
     * @param <T>
     * @return
     */
    public <T> Disposable onEventMainThread(Class<T> clazz, Consumer<T> handler) {
        return mPublishSubject
                .ofType(clazz)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler);

    }

    public static interface OnEventListener<E> {
        void onEvent(E e);
    }

}
