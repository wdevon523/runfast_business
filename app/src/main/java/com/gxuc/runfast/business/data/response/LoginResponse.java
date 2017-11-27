package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.bean.User;

/**
 * 登录
 * Created by Berial on 2017/8/24.
 */
public class LoginResponse extends BaseResponse {

    @SerializedName("appuser")
    public User user;
}
