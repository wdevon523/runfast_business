package com.gxuc.runfast.business.data.bean;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.DataLayer;

public class SubOption {

    public transient long id;
    public String name;

    @SerializedName("sort")
    public String order;

    @Override
    public String toString() {
        return DataLayer.getGson().toJson(this);
    }
}
