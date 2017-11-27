package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 收入记录
 * Created by Berial on 2017/8/26.
 */
public class IncomeRecordDTO implements Mapper<IncomeRecord> {

    public String orderCode;
    public String createTime;
    public double businesspay;
    public double businessget;

    @Override
    public IncomeRecord map() {
        IncomeRecord record = new IncomeRecord();
        record.date = createTime;
        record.orderId = orderCode;
        record.amount = Utils.formatFloorNumber(businesspay - businessget, 2, true);
        return record;
    }
}
