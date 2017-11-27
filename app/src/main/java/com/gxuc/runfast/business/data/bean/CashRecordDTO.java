package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 提现记录
 * Created by Berial on 2017/8/26.
 */
public class CashRecordDTO implements Mapper<CashRecord> {

    public String createTime;
    public String wdate;
    public Integer status;
    public Double monetary;

    @Override
    public CashRecord map() {
        CashRecord record = new CashRecord();
        record.amount = Utils.formatFloorNumber(monetary != null ? monetary : 0, 2, true);
        record.date = Utils.nullToValue(createTime, "");
        record.cashDate = Utils.nullToValue(wdate, "");
        record.state = status == null ? 2 : status;
        record.stateName = status == null || status == 2 ? "未通过" : status == 1 ? "已通过" : "待审核";
        return record;
    }
}
