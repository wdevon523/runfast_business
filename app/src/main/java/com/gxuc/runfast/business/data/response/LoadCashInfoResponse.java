package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.CashInfo;
import com.gxuc.runfast.business.util.Utils;

/**
 * 提现申请界面数据
 * Created by Berial on 2017/8/26.
 */
public class LoadCashInfoResponse extends BaseResponse implements Mapper<CashInfo> {

    public String wdate;
    public String price;

    @Override
    public CashInfo map() {
        CashInfo info = new CashInfo();
        info.amount = Utils.nullToValue(price, "0");
        info.date = Utils.nullToValue(wdate, "当前金额统计日期为：未统计");
        return info;
    }
}
