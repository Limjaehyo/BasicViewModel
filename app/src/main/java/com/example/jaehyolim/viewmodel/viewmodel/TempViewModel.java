package com.example.jaehyolim.viewmodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.jaehyolim.viewmodel.model.BaseModel;
import com.example.jaehyolim.viewmodel.model.dataSource.TempDataFactory;
import com.example.jaehyolim.viewmodel.repository.TempRepository;
import com.example.jaehyolim.viewmodel.util.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;


public class TempViewModel extends BaseViewModel< TempViewModel.TempInterface,Object> {
    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<BaseModel>> articleLiveData;


    public TempViewModel(@NonNull Application application, TempInterface tempInterface) {
        super(application , tempInterface);
        executor = Executors.newFixedThreadPool(5);
    }





    public void getPaging(){
        TempDataFactory feedDataFactory = new TempDataFactory();
        networkState = Transformations.switchMap(feedDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        articleLiveData = (new LivePagedListBuilder(feedDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
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
            if (modelClass.isAssignableFrom(TempViewModel.class)) {
                return (T) new TempViewModel(mApplication,anInterface);
            }
            throw  new IllegalArgumentException("Unknown ViewModel class");

        }
    }
    public interface TempInterface extends BaseVewModelInterface {

        void getComplete();
    }

}
