package com.example.jaehyolim.viewmodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.jaehyolim.viewmodel.model.BaseModel;

import io.reactivex.Observable;

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
    public final I viewModelInterface;


    public BaseViewModel(@NonNull Application application , I viewModelInterface) {
        super(application);
        this.viewModelInterface = viewModelInterface;
    }

}
