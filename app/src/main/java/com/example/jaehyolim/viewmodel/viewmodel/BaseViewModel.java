package com.example.jaehyolim.viewmodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jaehyolim.viewmodel.model.BaseModel;

import java.io.IOException;

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
abstract public class BaseViewModel<T extends BaseModel,I extends BaseVewModelInterface, A > extends AndroidViewModel {
    abstract protected Observable<T> getObservable();

    abstract protected Observable<T> getObservable(A args);
    public final ObservableField<Boolean> isProgress = new ObservableField<>();
    protected final I viewModelInterface;
    protected final String _FAILMSG = "서버와 통신이 원활하지 않습니다.\n잠시 후 다시 이용해 주세요.";
    private final int MAX_RETRY = 3;
    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    public BaseViewModel(@NonNull Application application , I viewModelInterface) {
        super(application);
        this.viewModelInterface = viewModelInterface;
    }
    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, Consumer<O> consumer) {
        viewModelInterface.putDisposableMap(tag,
                observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> isProgress.set(true))
                        .retry((count, throwable) -> {
                            Log.e("retry", "throwable " +throwable.getMessage());
                            if (throwable instanceof IOException) {
                            }
                            return throwable instanceof IOException && count <= MAX_RETRY;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer, throwable -> {
                            viewModelInterface.showMessageDialog(throwable instanceof IOException ? _FAILMSG : throwable.getMessage());
                            isProgress.set(false);
                        }, () -> isProgress.set(false))
        );

    }

    protected synchronized <O extends BaseModel> void executeObservable(String tag, Observable<O> observable, Consumer<O> consumer, Consumer<? super Throwable> errorConsumer) {
        viewModelInterface.putDisposableMap(tag,
                observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> isProgress.set(true))
                        .retry((count, throwable) -> {
                            Log.e("retry", "throwable " + throwable.getMessage());
                            if (throwable instanceof IOException) {
                            }
                            return throwable instanceof IOException && count <= MAX_RETRY;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer, throwable -> {
                            errorConsumer.accept(throwable);
                            viewModelInterface.showMessageDialog(throwable instanceof IOException ? _FAILMSG : throwable.getMessage());
                            isProgress.set(false);
                        }, () -> isProgress.set(false))
        );

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.dispose();
        mDisposable.clear();
    }

}
