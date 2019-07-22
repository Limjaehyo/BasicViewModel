package com.example.jaehyolim.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.example.jaehyolim.viewmodel.view.BaseViewModelActivity;
import com.example.jaehyolim.viewmodel.viewmodel.TempViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseViewModelActivity<TempViewModel> implements TempViewModel.TempInterface {

    @Override
    public TempViewModel viewModel() {
        final TempViewModel.TempViewModelFactory tempViewModelFactory = new TempViewModel.TempViewModelFactory(getApplication(), this);
        return ViewModelProviders.of(this, tempViewModelFactory).get(TempViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel.getArrayListObservable();
      /*  getDisposable().add(RxView.clicks(findViewById(R.id.button)).throttleFirst(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Log.e("MainActivity", "clikc");
        },throwable -> {}));*/
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25);

        getDisposable().add(Observable.fromIterable(integers).take(20).subscribe(new Consumer<Integer>() {
            ArrayList<Integer> data = new ArrayList<>();
            @Override
            public void accept(Integer integer) throws Exception {
                data.add(integer);
            }

    }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        },() -> {

        }));
    }





    @Override
    public void getComplete() {

    }



    @Override
    public void putDisposableMap(String tag, Disposable disposable) {

    }

    @Override
    public void showMessageDialog(String msg) {
        showCustomDialog(this, msg).show();
    }
}
