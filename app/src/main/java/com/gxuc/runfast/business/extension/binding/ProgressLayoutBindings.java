package com.gxuc.runfast.business.extension.binding;

import android.databinding.BindingAdapter;

import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.vlonjatg.progressactivity.ProgressLinearLayout;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

public class ProgressLayoutBindings {

    @BindingAdapter({ "loading" })
    public static void setLoading(ProgressLinearLayout layout, boolean loading) {
        if (loading) {
            layout.showLoading();
        } else {
            layout.showContent();
        }
    }

    @BindingAdapter({ "loading" })
    public static void setLoading(ProgressRelativeLayout layout, boolean loading) {
        if (loading) {
            layout.showLoading();
        } else {
            layout.showContent();
        }
    }

    @BindingAdapter({ "loading" })
    public static void setLoading(ProgressFrameLayout layout, boolean loading) {
        if (loading) {
            layout.showLoading();
        } else {
            layout.showContent();
        }
    }
}
