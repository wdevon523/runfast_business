package com.gxuc.runfast.business.extension.binding;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshLayoutBindings {

    @BindingAdapter("progressColor")
    public static void setProgressColor(SwipeRefreshLayout layout, int color) {
        layout.setColorSchemeColors(color);
    }
}
