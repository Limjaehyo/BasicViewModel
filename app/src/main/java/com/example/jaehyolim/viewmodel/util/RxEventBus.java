package com.example.jaehyolim.viewmodel.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Author : jaehyolim
 * Created on Date : 2018. 7. 31.
 * RxEventBus
 */

public class RxEventBus {
    private Subject<Object> mSubject;

    private static volatile RxEventBus INSTANCE = null;

    private Map<OnEventListener<?>, Disposable> map = new HashMap<>();

    private RxEventBus() {
        mSubject = BehaviorSubject.create().toSerialized();
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
    public <E> void registerListener(OnEventListener<E> listener, Class<E> type) {
        final Disposable subscription = mSubject.ofType(type) // filter

                .subscribe(listener::onEvent, throwable -> {
                    Log.e("EventBus", throwable.getMessage(), throwable);
                });
        map.put(listener, subscription);
    }

    public <E> void unregisterListener(OnEventListener<E> listener) {
        Disposable disposable = map.get(listener);
        disposable.dispose();
        if (map.containsKey(listener)) {
            map.remove(listener);
        }
    }

    /**
     * push an event to the queue(Subject)
     *
     * @param event
     */
    public void postEvent(Object event) { // push event to queue
        mSubject.onNext(event);
    }

    public Observable<Object> getObservable() {
        return mSubject;
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
        return mSubject
                .ofType(clazz)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler);

    }

    public static interface OnEventListener<E> {
        void onEvent(E e);
    }

}
