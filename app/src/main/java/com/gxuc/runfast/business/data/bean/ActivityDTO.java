package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

import java.util.Locale;

public class ActivityDTO implements Mapper<Activity> {

    public long id;
    public String name;
    public String startTime;
    public String endTime;
    public Integer valid;
    public Integer stops;
    public int ptype;
    public Double fulls;
    public Double lesss;
    public Double discount;
    public Double disprice;
    public String goods;
    public String goodids;
    public String stanids;

    @Override
    public Activity map() {
        Activity activity = new Activity();
        activity.id = id;
        activity.name = name;
        activity.startTime = Utils.emptyToValue(startTime, "现在").split(" ")[0].replace("-", ".");
        activity.endTime = Utils.emptyToValue(endTime, "永久").split(" ")[0].replace("-", ".");

        if (valid != null && valid == 1) {
            if (stops != null && stops == 1) {
                activity.status = 2;
                activity.statusName = "暂停中";
            } else {
                activity.status = 1;
                activity.statusName = "进行中";
            }
        } else {
            activity.status = 0;
            activity.statusName = "已结束";
        }

        activity.type = ptype;
        activity.typeName = // @formatter:off
                ptype == 1 ? "满减活动"
              : ptype == 2 ? "打折活动"
              : ptype == 3 ? "赠品活动"
              : ptype == 4 ? "特价活动"
                           : "满减免运费"; // @formatter:on

        activity.full = fulls == null ? "0" : Utils.formatFloorNumber(fulls, 2, true);
        activity.less = lesss == null ? "0" : Utils.formatFloorNumber(lesss, 2, true);
        activity.discount = discount == null ? "10" : Utils.formatFloorNumber(discount, 2, true);
        activity.disprice = disprice == null ? "0" : Utils.formatFloorNumber(disprice, 2, true);
        activity.freebie = Utils.emptyToValue(goods, "");
        activity.goodsIds = Utils.emptyToValue(goodids, "");
        activity.standardIds = Utils.emptyToValue(stanids, "");

        switch (ptype) {
            case 1:
                activity.describe = String.format(Locale.CHINA, "全场满%s元减%s元",
                        fulls == null ? "0" : Utils.formatFloorNumber(fulls, 2, true),
                        lesss == null ? "0" : Utils.formatFloorNumber(lesss, 2, true));
                break;
            case 2:
                activity.describe = String.format(Locale.CHINA, "部分商品%s折出售",
                        discount == null ? "10" : Utils.formatFloorNumber(discount, 2, true));
                break;
            case 3:
                activity.describe = Utils.emptyToValue(goods, "部分商品购买即送");
                break;
            case 4:
                activity.describe = String.format(Locale.CHINA, "部分商品特价%s元",
                        disprice == null ? "0" : Utils.formatFloorNumber(disprice, 2, true));
                break;
            case 5:
                activity.describe = String.format(Locale.CHINA, "全场满%s减免运费",
                        fulls == null ? "0" : Utils.formatFloorNumber(fulls, 2, true));
                break;
        }
        return activity;
    }
}
