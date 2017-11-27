package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

public class GoodsSellDTO implements Mapper<GoodsSell> {

    public Double totprive;
    public Integer totnum;
    public String goodsSellName;

    @Override
    public GoodsSell map() {
        GoodsSell sell = new GoodsSell();
        sell.name = Utils.emptyToValue(goodsSellName, "--");
        sell.amount = Utils.formatFloorNumber(totprive == null ? 0 : totprive, 2);
        sell.count = String.valueOf(totnum == null ? 0 : totnum);
        sell.price = Utils.formatFloorNumber(
                (totprive == null ? 0 : totprive) / (totnum == null || totnum == 0 ? 1 : totnum), 2);

        sell.amountValue = totprive == null ? 0 : totprive;
        sell.countValue = totnum == null ? 0 : totnum;
        sell.priceValue = (totprive == null ? 0 : totprive) / (totnum == null || totnum == 0 ? 1 : totnum);
        return sell;
    }
}
