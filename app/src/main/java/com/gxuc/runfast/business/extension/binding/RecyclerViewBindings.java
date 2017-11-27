package com.gxuc.runfast.business.extension.binding;


import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.epoxy.Adapter;

public class RecyclerViewBindings {

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, Adapter adapter) {
        view.setAdapter(adapter);
    }
}
