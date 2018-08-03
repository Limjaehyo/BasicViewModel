package com.example.jaehyolim.viewmodel.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

abstract public class BaseViewModelFragment<T extends ViewModel> extends Fragment {

    protected abstract T viewModel();


    protected T mViewModel;
    private MaterialDialog materialDialog;
    private DisposableLifecycleObserver observer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lifecycle lifecycle = this.getLifecycle();
        observer = new DisposableLifecycleObserver(getLifecycle());
        lifecycle.addObserver(observer);

        try {
            /*어플리케이션을 강제 종료하거나 메모리에서 삭제될경우 메소드 를 찾지못하는 어레발생 하여 앱이 죽는경우가있음  어플리케이션을 찾지못할경우 catch 문으로 이동하고 앱을 재시작한다.*/
            if (getActivity().getApplication() != null) {
                mViewModel = viewModel();
            }
        } catch (Exception e) {
            /*리스타트할 처음 화면으로 로딩*/
            reStart();
        }

    }
    private void reStart() {
    /*    final Intent intent = new Intent(getActivity(), LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }
    @Override
    public void onStart() {
        super.onStart();
        observer.enable();
    }

    protected void putDisposableMap(String tag, Disposable disposable) {
        observer.putDisposableMap(tag, disposable);

    }

    public CompositeDisposable getDisposable() {
        return observer.getDisposable();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }





}
