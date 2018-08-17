package com.example.jaehyolim.viewmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.example.jaehyolim.viewmodel.view.BaseViewModelActivity;
import com.example.jaehyolim.viewmodel.viewmodel.TempViewModel;

import io.reactivex.disposables.Disposable;

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
