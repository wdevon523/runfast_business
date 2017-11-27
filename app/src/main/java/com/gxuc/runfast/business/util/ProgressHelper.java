package com.gxuc.runfast.business.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.gxuc.runfast.business.R;

import java.lang.ref.WeakReference;

public class ProgressHelper {

    private WeakReference<Activity> activity;
    private WeakReference<Fragment> parentFragment;

    private AlertDialog mDialog;

    public ProgressHelper(Activity activity) {
        this.activity = new WeakReference<>(activity);
        createDialog();
    }

    public ProgressHelper(Fragment fragment) {
        parentFragment = new WeakReference<>(fragment);
        createDialog();
    }

    @SuppressWarnings("InflateParams")
    private void createDialog() {
        Context context = null;
        if (activity != null && activity.get() != null) {
            context = activity.get();
        } else if (parentFragment != null && parentFragment.get() != null) {
            context = parentFragment.get().getActivity();
        }

        if (context == null) return;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

        mDialog = new AlertDialog.Builder(context, R.style.LoadingTheme)
                .setCancelable(false)
                .setView(view)
                .create();

        mDialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        if (mDialog != null) mDialog.show();
    }

    public boolean isShow() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    public void dismiss() {
        if (mDialog != null) mDialog.dismiss();
    }

    public interface Callback {
        void setLoading(boolean loading);
    }
}
