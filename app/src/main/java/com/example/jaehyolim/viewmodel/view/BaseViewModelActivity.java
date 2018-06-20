package com.example.jaehyolim.viewmodel.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

abstract public class BaseViewModelActivity<T extends ViewModel> extends AppCompatActivity {


    private DisposableLifecycleObserver observer;

    protected abstract T viewModel();
    protected T mViewModel;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Lifecycle lifecycle = this.getLifecycle();
        observer = new DisposableLifecycleObserver(lifecycle);
        lifecycle.addObserver(observer);
        mViewModel = viewModel();

    }


    @Override
    protected void onStart() {
        super.onStart();
        observer.enable();
    }


    public void putDisposableMap(String tag, Disposable disposable) {
        observer.putDisposableMap(tag, disposable);

    }



    @Override
    protected void onStop() {
        super.onStop();
        if (materialDialog != null) {
            if (materialDialog.isShowing()) {
                materialDialog.dismiss();
            }
        }

    }

    public MaterialDialog showCustomDialog(Context context, String msg) {
        this.materialDialog =  new MaterialDialog.Builder(context).build();
        return materialDialog;
    }



   /* @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }*/
}
