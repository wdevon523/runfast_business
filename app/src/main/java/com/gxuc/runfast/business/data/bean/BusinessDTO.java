package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 商户信息
 * Created by Berial on 2017/8/25.
 */
public class BusinessDTO implements Mapper<Business> {

    public long id;
    public String name;
    public String mobile;
    public Object sort;
    public String content;
    public String imgPath;
    public String mini_imgPath;
    public Double startPay;
    public Double packing;

    //    @SerializedName("isopen")
//    public int status;
    public String account;
    public String establishname;
    public String code;
    public Integer issubsidy;
    public Double subsidy;
    public Object typeName;
    public String state;

    public Double busshowps;

    @SerializedName("maxdistance")
    public String range;

    @SerializedName("wordTime")
    public String workTime;

    public String startTime1;
    public String endTime1;
    public String startTime2;
    public String endTime2;

    public String showActity;

    public Integer isDeliver;

    @Override
    public Business map() {
        Business business = new Business();
        business.name = Utils.emptyToValue(name, "");
        business.phone = Utils.emptyToValue(mobile, "");
        business.logoUrl = TextUtils.isEmpty(imgPath) ? "" : ApiServiceFactory.HOST + imgPath;
        business.id = id;

        business.statusName = Utils.emptyToValue(state, "--");
        business.status = "营业".equals(state) ? 0 : 1;

        business.workTime = Utils.emptyToValue(workTime, "未设置")
                .replace(";", workTime.endsWith(";") ? "" : "\n")
                .replace("null", "");
        business.startAmount = Utils.formatFloorNumber(startPay == null ? 0 : startPay, 2, true);
        business.range = Utils.emptyToValue(range, "");
        business.account = Utils.emptyToValue(account, "--");
        business.accountName = Utils.emptyToValue(establishname, "--");
        business.hasSubsidy = issubsidy != null && issubsidy == 1;
        business.subsidy = Utils.formatFloorNumber(subsidy == null ? 0 : subsidy, 2, true);
        business.packingCharge = Utils.formatFloorNumber(packing == null ? 0 : packing, 2, true);
        business.startTime = Utils.emptyToValue(startTime1, "");
        business.endTime = Utils.emptyToValue(endTime1, "");
        business.nextStartTime = Utils.emptyToValue(startTime2, "");
        business.nextEndTime = Utils.emptyToValue(endTime2, "");
        business.deliveryCost = Utils.formatFloorNumber(busshowps != null ? busshowps : 0, 2, true);
        business.describe = Utils.emptyToValue(content, "");
        business.activities = Utils.emptyToValue(showActity, "");

        business.isDeliver = isDeliver != null && isDeliver == 1;

        return business;
    }
}
