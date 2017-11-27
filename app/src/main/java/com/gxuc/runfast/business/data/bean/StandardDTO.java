package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 商品规格
 * Created by Berial on 2017/9/8.
 */
public class StandardDTO implements Mapper<Standard> {

    public long id;
    public String name;
    public Double price;

    @Override
    public Standard map() {
        Standard standard = new Standard();
        standard.id = id;
        standard.name = Utils.emptyToValue(name, "");
        standard.price = Utils.formatFloorNumber(price == null ? 0 : price, 2, true);
        return standard;
    }
}
