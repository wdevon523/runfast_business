package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO implements Mapper<Order> {

    public long id;

    public Integer orderNumber;

    public String statStr;
    public int status;
    public Integer isCancel;

    public String businessId;
    public String businessName;
    public String businessAddr;
    public String businessMobile;
    public String userName; // 顾客
    public String userAddress;
    public String address;
    public String userPhone;
    public String content;

    public String shopper; // 快递员
    public String shopperMobile;
    public String readyTime;

    public Integer goodsTotal;

    public String payTime;
    public String createTime; // 下单时间
    public String orderCode; // 订单号

    public Double businesspay;
    public Double businessget;
    public Double activityprice;
    public Double agentget;
    public Double showps; // 配送费
    public Double packing; // 打包费
    public Double yhprice; // 优惠券
    public Double totalpay; // 总价格
    public Double disprice; // 小计

    public Integer isDeliver;
    public Double shopperMoney; // 商家配送费

    public Double refund; // 退款
    public Integer isRefund;

    public List<OrderGoodsDTO> goodsSellRecordChildren;

    @Override
    public Order map() {
        Order order = new Order();
        order.id = id;
        order.status = status;
        order.expand = true;
        order.businessId = Utils.emptyToValue(businessId, "0");
        order.businessName = Utils.emptyToValue(businessName, "未填写");
        order.businessAddr = Utils.emptyToValue(businessAddr, "未填写");
        order.businessMobile = Utils.emptyToValue(businessMobile, "未填写");

        order.shopperName = Utils.emptyToValue(userName, "匿名");
        order.shopperAddress = Utils.emptyToValue(userAddress + address, "未填写");
        order.shopperPhone = Utils.emptyToValue(userPhone, "未填写");
        order.remark = Utils.emptyToValue(content, "无");

        order.courier = Utils.emptyToValue(shopper, "匿名");
        order.courierPhone = Utils.emptyToValue(shopperMobile, "未填写");
        if (TextUtils.isEmpty(readyTime)) {
            order.deliveryTime = "--:--";
        } else {
            order.deliveryTime = readyTime.substring(11, 16);
        }

        order.goodsCount = goodsTotal != null ? String.valueOf(goodsTotal) : "0";
        order.payTime = Utils.emptyToValue(payTime, "--");
        order.orderTime = Utils.emptyToValue(createTime, "--");
        order.orderNo = Utils.emptyToValue(orderCode, "--");
        order.orderNumber = String.valueOf(orderNumber == null ? 0 : orderNumber);

        order.hasPackingCharge = packing != null && packing > 0;

        order.hasRefund = isRefund != null && isRefund == 2;
        order.refund = Utils.formatFloorNumber(refund == null ? 0 : refund, 2, true);

        order.isDeliver = isDeliver != null && isDeliver == 1;
        order.shopDeliveryCost = Utils.formatFloorNumber(shopperMoney == null ? 0 : shopperMoney, 2, true);

        order.deliveryCost = Utils.formatFloorNumber(showps == null ? 0 : showps, 2, true);
        order.packingCharge = Utils.formatFloorNumber(packing == null ? 0 : packing, 2, true);
        order.discounts = Utils.formatFloorNumber(yhprice == null ? 0 : yhprice, 2, true);
        order.payAmount = Utils.formatFloorNumber(totalpay != null ? totalpay : 0, 2, true);
        order.cost = Utils.formatFloorNumber(activityprice != null ? activityprice : 0, 2, true);
        order.serviceCharge = Utils.formatFloorNumber(businessget != null ? businessget : 0, 2, true);
        order.realIncome = Utils.formatFloorNumber((businesspay != null ? businesspay : 0)
                - (businessget != null ? businessget : 0), 2, true);
        order.subtotal = Utils.formatFloorNumber(disprice != null ? disprice : 0, 2, true);

        ArrayList<OrderGoods> goods = new ArrayList<>();
        if (goodsSellRecordChildren != null && !goodsSellRecordChildren.isEmpty()) {
            for (OrderGoodsDTO dto : goodsSellRecordChildren) {
                goods.add(dto.map());
            }
        }
        order.goods = goods;

        if (isCancel != null && isCancel == 1) {
            order.status = -2;
        }
        switch (order.status) {
            case -2:
                order.statusName = "取消订单";
                order.showLeftButton = true;
                order.showRightButton = true;
                order.leftButtonText = "同意取消";
                order.rightButtonText = "不同意取消";
                break;
            case -1:
                order.statusName = "订单已取消";
                order.showLeftButton = false;
                order.showRightButton = true;
                order.rightButtonText = "打印";
                order.isWhiteButton = true;
                break;
            case 1:
                order.statusName = "待商家接单";
                order.showLeftButton = true;
                order.showRightButton = true;
                order.leftButtonText = "拒单";
                order.rightButtonText = "接单";
                break;
            case 8:
                order.statusName = "订单已完成";
                order.showLeftButton = true;
                order.showRightButton = true;
                order.leftButtonText = "打印";
                order.rightButtonText = "退款";
                order.isWhiteButton = true;
                break;
            case 2:
            case 3:
                order.statusName = "待商家打包";
                order.showLeftButton = true;
                order.showRightButton = true;
                order.leftButtonText = "打印";
                order.rightButtonText = "商品打包";
                break;
            case 5:
                order.statusName = "待配送员送达";
                order.showLeftButton = false;
                order.showRightButton = true;
                order.rightButtonText = "打印";
                order.isWhiteButton = true;
                break;
            default:
                order.statusName = Utils.emptyToValue(statStr, "");
                order.showLeftButton = false;
                order.showRightButton = false;
        }
        return order;
    }
}
