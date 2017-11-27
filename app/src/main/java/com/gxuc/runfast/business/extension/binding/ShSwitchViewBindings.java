package com.gxuc.runfast.business.extension.binding;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;

import com.sevenheaven.iosswitch.ShSwitchView;

public class ShSwitchViewBindings {

    @InverseBindingAdapter(attribute = "on", event = "onSwitchStateChange")
    public static boolean isOn(ShSwitchView view) {
        return view.isOn();
    }

    @BindingAdapter("onSwitchStateChange")
    public static void setOnSwitchStateChangeListener(ShSwitchView view, final InverseBindingListener listener) {
        view.setOnSwitchStateChangeListener((isOn) -> listener.onChange());
    }
}
