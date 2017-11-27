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
    public Double totalprice;
    public String showoption;
    public String goodsSellStandardName;
    public String goodsSellOptionName;

    @Override
    public OrderGoods map() {
        OrderGoods goods = new OrderGoods();
        goods.id = id;
        goods.name = goodsSellName;
        goods.count = num != null ? String.valueOf(num) : "0";
        goods.price = Utils.formatFloorNumber(totalprice != null ? totalprice : 0, 2, true);
        goods.remark = Utils.emptyToValue(showoption, "");

        goods.standard = TextUtils.isEmpty(goodsSellStandardName) ? "" :
                "(" + goodsSellStandardName + ")";

        goods.option = TextUtils.isEmpty(goodsSellOptionName) ? "" :
                "(" +
                        (goodsSellOptionName.endsWith(";") ? goodsSellOptionName.replace(";", "")
                                : goodsSellOptionName)
                        + ")";
        return goods;
    }
}
