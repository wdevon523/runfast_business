package com.gxuc.runfast.business.extension;

import android.support.annotation.MenuRes;
import android.support.v7.widget.Toolbar;

public interface WithMenu {

    @MenuRes
    int thisMenu();

    Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener();
}
