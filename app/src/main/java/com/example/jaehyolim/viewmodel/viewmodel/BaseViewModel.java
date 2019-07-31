package com.example.jaehyolim.viewmodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.jaehyolim.viewmodel.model.BaseModel;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel 최상위 추상화 클래스
 *
 * @param <T>
 * @param <A>
 */
abstract public class BaseViewModel<I extends BaseVewModelInterface, A> extends AndroidViewModel {

    public final ObservableField<Boolean> isProgress = new ObservableField<>();
    protected final I viewModelInterface;
    protected final String _FAILMSG = "서버와 통신이 원활하지 않습니다.\n잠시 후 다시 이용해 주세요.";
    private final int MAX_RETRY = 3;
    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    public BaseViewModel(@NonNull Application application, I viewModelInterface) {
        super(application);
        this.viewModelInterface = viewModelInterface;
    }

    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, Consumer<O> consumer) {
        executeObservable(tag, observable, null, consumer, null);
    }

    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, Consumer<O> consumer, ArrayList<View> enbleList) {

        for (View view : enbleList) {
            view.setEnabled(false);
        }
        executeObservable(tag, observable, null, consumer, null);
    }


    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, ObservableField<Boolean> isField, Consumer<O> consumer) {
        executeObservable(tag, observable, null, consumer, null);

    }


    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, Consumer<O> consumer, Consumer<? super Throwable> errorConsumer) {
        executeObservable(tag, observable, null, consumer, errorConsumer);
    }

    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, ObservableField<Boolean> isField, Consumer<O> consumer, Consumer<? super Throwable> errorConsumer) {
        if (isNetWork()) {
            viewModelInterface.putDisposableMap(tag,
                    observable.subscribeOn(Schedulers.io())
                            .doOnSubscribe(disposable -> {
                                isProgress.set(true);
                                if (isField != null) {
                                    isField.set(false);
                                }
                            })
                            .retry((count, throwable) -> {
                      /*      Log.e("retry", "throwable " + throwable.getMessage());
                            if (throwable instanceof IOException) {
                                Toast.makeText(application, "IOExeeption", Toast.LENGTH_SHORT).show();
                            }*/
                                return throwable instanceof IOException && count <= MAX_RETRY;
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(consumer, throwable -> {
                                if (errorConsumer != null) {
                                    try {
                                        errorConsumer.accept(throwable);
                                    } catch (Exception ignored) {
                                    }
                                }
                                viewModelInterface.showMessageDialog(throwable instanceof IOException ? _FAILMSG : throwable.getMessage());
                                isProgress.set(false);
                            }, () -> isProgress.set(false))
            );
        }else{
            viewModelInterface.showMessageDialog("인터넷 연결이 원활하지 않습니다.");
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.dispose();
        mDisposable.clear();
    }

    private boolean isNetWork() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
