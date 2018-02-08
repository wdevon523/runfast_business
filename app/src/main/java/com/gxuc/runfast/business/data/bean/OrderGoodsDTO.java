package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 订单商品
 * Created by Berial on 2017/9/2.
 */
public class OrderGoodsDTO implements Mapper<OrderGoods> {

    public long id;
    public String goodsSellName;
    public Integer num;
    public Integer activityType;
    public String goods;
    public String activityName;
    public Double totalprice;
    public String showoption;
    public String goodsSellStandardName;
    public String goodsSellOptionName;

    @Override
    public OrderGoods map() {
        OrderGoods good = new OrderGoods();
        good.id = id;
        good.name = goodsSellName;
        good.count = num != null ? String.valueOf(num) : "0";
        good.price = Utils.formatFloorNumber(totalprice != null ? totalprice : 0, 2, true);
        good.remark = Utils.emptyToValue(showoption, "");

        good.standard = TextUtils.isEmpty(goodsSellStandardName) ? "" :
                "(" + goodsSellStandardName + ")";

        good.option = TextUtils.isEmpty(goodsSellOptionName) ? "" :
                "(" +
                        (goodsSellOptionName.endsWith(";") ? goodsSellOptionName.replace(";", "")
                                : goodsSellOptionName)
                        + ")";
        good.activityType = activityType == null ? 0 : activityType;
        good.goodsAct = goods;
//        good.activityName = activityName;
        return good;
    }
}
