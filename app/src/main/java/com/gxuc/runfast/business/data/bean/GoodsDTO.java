package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class GoodsDTO implements Mapper<Goods> {

    public long id;
    public long gid;
    public String name;
    public String imgPath;
    public String mini_imgPath;
    public int salesnum;
    public Double price;
    public int status;

    public Long sellTypeId;
    public String sellTypeName;
    public String content;
    public Integer islimited;
    public Integer limittype;
    public Integer limitNum;
    public String limiStartTime;
    public String limiEndTime;
    public Integer ptype;
    public Integer num;

    public String sname;

    public List<StandardDTO> standardList;
    public List<OptionDTO> optionList;

    @Override
    public Goods map() {
        Goods goods = new Goods();
        goods.id = id;
        goods.imageUrl = TextUtils.isEmpty(imgPath) ? "" : ApiServiceFactory.HOST + imgPath;
        goods.imagePath = imgPath;
        goods.thumbnailPath = mini_imgPath;
        goods.name = name;
        goods.sales = String.valueOf(salesnum);
        goods.price = price != null ? Utils.formatFloorNumber(price, 2, true) : "0";
        goods.status = status;
        goods.sortId = sellTypeId == null ? 0 : sellTypeId;
        goods.sortName = Utils.emptyToValue(sellTypeName, "");
        goods.describe = Utils.emptyToValue(content, "");
        goods.isLimited = islimited != null && islimited == 1;
        goods.needFullPrice = limittype != null && limittype == 1;
        goods.needPackingCharge = ptype != null && ptype == 0;
        goods.limitCount = String.valueOf(limitNum == null ? 0 : limitNum);
        goods.count = String.valueOf(num == null ? 0 : num);

        goods.standardId = gid;
        goods.standardName = sname;

        if (TextUtils.isEmpty(limiStartTime)) {
            goods.startTime = "";
        } else {
            goods.startTime = limiStartTime.substring(0, 10);
        }

        if (TextUtils.isEmpty(limiEndTime)) {
            goods.endTime = "";
        } else {
            goods.endTime = limiEndTime.substring(0, 10);
        }

        ArrayList<Standard> standards = new ArrayList<>();
        if (standardList != null && !standardList.isEmpty()) {
            for (StandardDTO dto : standardList) {
                standards.add(dto.map());
            }
        }
        goods.standards = standards;

        ArrayList<Option> options = new ArrayList<>();
        if (optionList != null && !optionList.isEmpty()) {
            for (OptionDTO dto : optionList) {
                options.add(dto.map());
            }
        }
        goods.options = options;

        return goods;
    }
}
