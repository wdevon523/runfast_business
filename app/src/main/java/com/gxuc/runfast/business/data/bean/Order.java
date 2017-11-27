package com.gxuc.runfast.business.data.bean;

import java.util.List;

public class Order {

    public long id;
    public String orderNumber;

    public String statusName;
    public int status;

    public String businessId;
    public String businessName;
    public String businessAddr;
    public String businessMobile;
    public String shopperName; // 顾客
    public String shopperAddress;
    public String shopperPhone;
    public String remark;

    public String courier; // 快递员
    public String courierPhone;
    public String deliveryTime; // 送达时间

    public String goodsCount;

    public String payAmount;
    public String subtotal; // 小计
    public String cost; // 活动支出
    public String serviceCharge; // 服务费
    public String realIncome; // 实际收入
    public String deliveryCost; // 配送费
    public String packingCharge;
    public String discounts;
    public boolean hasPackingCharge;

    public boolean isDeliver;
    public String shopDeliveryCost;

    public boolean hasRefund;
    public String refund;

    public String payTime;
    public String orderTime; // 下单时间
    public String orderNo; // 订单号

    public List<OrderGoods> goods;

    public boolean expand;
    public boolean showLeftButton;
    public boolean showRightButton;
    public String leftButtonText;
    public String rightButtonText;
    public boolean isWhiteButton;
}
