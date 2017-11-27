package com.gxuc.runfast.business.data.bean;

/**
 * 商户信息
 * Created by Berial on 2017/8/25.
 */
public class Business {

    public long id;
    public String name;
    public String phone;
    public String logoUrl;

    public int status;
    public String statusName;
    public String account;
    public String accountName;

    public String range;

    public String workTime;
    public String startTime;
    public String endTime;
    public String nextStartTime;
    public String nextEndTime;

    public String startAmount; // 起送金额

    public boolean hasSubsidy;
    public String subsidy;

    public String deliveryCost; // 配送费
    public String packingCharge;
    public boolean isAutomatic;

    public String describe;
    public String activities;

    public boolean isDeliver; // 自配送
}
