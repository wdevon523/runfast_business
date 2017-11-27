package com.gxuc.runfast.business.ui.base;

import android.content.Context;
import android.databinding.BaseObservable;
import android.widget.Toast;

import com.gxuc.runfast.business.util.RxLifecycle;

public class BaseViewModel extends BaseObservable implements RxLifecycle.Impl {

    protected Context mContext;

    private RxLifecycle mLifecycle = new RxLifecycle();

    public BaseViewModel(Context context) {
        mContext = context.getApplicationContext();
    }

    protected void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        mLifecycle.onDestroy();
    }

    @Override
    public RxLifecycle bindLifecycle() {
        return mLifecycle;
    }
}
