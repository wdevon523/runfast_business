package com.gxuc.runfast.business.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 商品
 * Created by Berial on 2017/8/28.
 */
public class Goods implements Serializable {

    public long id;

    public String name;
    public String price;
    public String sales;
    public int status;
    public String imageUrl;
    public String imagePath;
    public String thumbnailPath;

    public long sortId;
    public String sortName;
    public int count;
    public String limitCount;
    public boolean isLimited;
    public boolean needPackingCharge;
    public boolean needFullPrice;
    public String startTime;
    public String endTime;
    public String describe;

    public List<Standard> standards;
    public List<Option> options;

    public boolean selected;

    public long standardId;
    public String standardName;
}
