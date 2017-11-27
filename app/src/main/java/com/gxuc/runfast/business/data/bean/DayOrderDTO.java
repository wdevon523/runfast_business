package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

public class DayOrderDTO implements Mapper<DayOrder> {

    public String orderCode;
    public String createTime;
    public Double subsidy;
    public double businesspay;
    public double businessget;

    @Override
    public DayOrder map() {
        DayOrder order = new DayOrder();
        order.orderId = Utils.emptyToValue(orderCode, "");
        order.amount = Utils.formatFloorNumber(businesspay - businessget, 2);
        order.subsidy = Utils.formatFloorNumber(subsidy == null ? 0 : subsidy, 2);

        order.amountValue = businesspay - businessget;
        order.subsidyValue = subsidy == null ? 0 : subsidy;

        if (TextUtils.isEmpty(createTime)) {
            order.time = "--";
        } else {
            order.time = createTime.substring(0, createTime.length() - 3);
        }
        return order;
    }
}
