package com.gxuc.runfast.business.data.bean;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.DataLayer;

import java.util.ArrayList;
import java.util.List;

public class Option {

    public transient long id;
    public String name;

    @SerializedName("subOption")
    public List<SubOption> options;

    public Option() {
        name = "";
        options = new ArrayList<>();
    }

    @Override
    public String toString() {
        return DataLayer.getGson().toJson(this);
    }
}
