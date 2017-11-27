package com.gxuc.runfast.business.data.bean;

import android.databinding.BaseObservable;

import com.gxuc.runfast.business.data.DataLayer;

public class Standard extends BaseObservable {

    public transient long id;
    public String name;
    public String price;

    public Standard() {
        name = "";
        price = "";
    }

    @Override
    public String toString() {
        return DataLayer.getGson().toJson(this);
    }
}
