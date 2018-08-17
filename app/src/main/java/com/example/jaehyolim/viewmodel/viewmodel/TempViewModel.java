package com.example.jaehyolim.viewmodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.jaehyolim.viewmodel.model.BaseModel;
import com.example.jaehyolim.viewmodel.repository.TempRepository;

import io.reactivex.Observable;


public class TempViewModel extends BaseViewModel<BaseModel, TempViewModel.TempInterface,Object> {


    public TempViewModel(@NonNull Application application, TempInterface tempInterface) {
        super(application , tempInterface);
    }

    @Override
    protected Observable<BaseModel> getObservable() {

        return TempRepository.getInstance().getResponse();
    }

    @Override
    protected Observable<BaseModel> getObservable(Object args) {
        return null;
    }



    public void getArrayListObservable() {
       /* viewModelInterface.getDisposable().add(getObservable().flatMap(
                gajagoAreaVO -> Observable.create(emitter ->{
                    emitter.onNext(emitter);
                    emitter.onComplete();
                }))
                 .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                        isProgress.set(true);
                })
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(area -> viewModelInterface.getComplete(), throwable -> {isProgress.set(false);
                         viewModelInterface.showMessageDialog(throwable.getMessage());}
                         , () -> isProgress.set(false)));*/

    }
    public static class TempViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private TempInterface anInterface;

        public TempViewModelFactory(@NonNull Application application, TempInterface anInterface) {
            mApplication = application;
            this.anInterface = anInterface;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TempViewModel(mApplication,anInterface);
        }
    }
    public interface TempInterface extends BaseVewModelInterface {

        void getComplete();
    }

}
