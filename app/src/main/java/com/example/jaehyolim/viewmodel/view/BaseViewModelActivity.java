package com.example.jaehyolim.viewmodel.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
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
        try {
            /*어플리케이션을 강제 종료하거나 메모리에서 삭제될경우 메소드 를 찾지못하는 어레발생 하여 앱이 죽는경우가있음  어플리케이션을 찾지못할경우 catch 문으로 이동하고 앱을 재시작한다.*/
            if (getApplication() != null) {
                mViewModel = viewModel();
            }
        } catch (Exception e) {
            /*리스타트할 처음 화면으로 로딩*/
            reStart();
        }

    }
    /*리스타트할 처음 화면으로 로딩*/

    private void reStart() {
//        final Intent intent = new Intent(this, LoadingActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        observer.enable();
    }


    protected void putDisposableMap(String tag, Disposable disposable) {
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

    public CompositeDisposable getDisposable() {
        return observer.getDisposable();
    }

    public MaterialDialog showCustomDialog(Context context, String msg) {
        this.materialDialog = new MaterialDialog.Builder(context).build();
        materialDialog.setTitle("알림");
        materialDialog.setContent(msg);
        if (materialDialog.getWindow() != null) {
            materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
            materialDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        materialDialog.setCanceledOnTouchOutside(false);

        return materialDialog;
    }

    public void removeDisposable(String tag) {
        observer.removeDisposable(tag);
    }


   /* @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }*/
}
