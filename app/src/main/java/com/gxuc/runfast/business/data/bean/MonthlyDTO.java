package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

public class MonthlyDTO implements Mapper<Monthly> {

    public Double totprive;
    public Integer totnum;
    public String goodsSellName;

    @Override
    public Monthly map() {
        Monthly monthly = new Monthly();
        monthly.name = Utils.emptyToValue(goodsSellName, "--");
        monthly.amount = Utils.formatFloorNumber(totprive == null ? 0 : totprive, 2);
        monthly.count = String.valueOf(totnum == null ? 0 : totnum);
        monthly.price = Utils.formatFloorNumber(
                (totprive == null ? 0 : totprive) / (totnum == null || totnum == 0 ? 1 : totnum), 2);

        monthly.amountValue = totprive == null ? 0 : totprive;
        monthly.countValue = totnum == null ? 0 : totnum;
        monthly.priceValue = (totprive == null ? 0 : totprive) / (totnum == null || totnum == 0 ? 1 : totnum);
        return monthly;
    }
}
