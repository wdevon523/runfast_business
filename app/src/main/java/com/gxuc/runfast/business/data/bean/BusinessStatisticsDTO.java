package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 营业统计
 * Created by Berial on 2017/8/25.
 */
public class BusinessStatisticsDTO implements Mapper<BusinessStatistics> {

    public String tday;
    public String day;
    public String month;
    public String total;

    @Override
    public BusinessStatistics map() {
        BusinessStatistics statistics = new BusinessStatistics();
        statistics.todaySale = Utils.nullToValue(tday, "0");
        statistics.todayIncome = Utils.nullToValue(day, "0");
        statistics.monthIncome = Utils.nullToValue(month, "0");
        statistics.totalIncome = Utils.nullToValue(total, "0");
        return statistics;
    }
}
