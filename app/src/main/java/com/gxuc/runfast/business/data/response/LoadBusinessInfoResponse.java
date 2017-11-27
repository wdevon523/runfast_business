package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Business;
import com.gxuc.runfast.business.data.bean.BusinessDTO;

/**
 * 获取商铺信息
 * Created by Berial on 2017/8/25.
 */
public class LoadBusinessInfoResponse extends BaseResponse implements Mapper<Business> {

    @SerializedName("bus")
    public BusinessDTO business;

    @Override
    public Business map() {
        if (business == null) return new BusinessDTO().map();
        return business.map();
    }
}
