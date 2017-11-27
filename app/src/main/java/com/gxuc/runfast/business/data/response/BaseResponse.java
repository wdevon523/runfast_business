package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.DataLayer;

public class BaseResponse {

    @SerializedName(value = "success")
    public boolean success;

    @SerializedName(value = "msg", alternate = { "succ", "gettime" })
    public String msg;

    @Override
    public String toString() {
        return DataLayer.getGson().toJson(this);
    }
}
