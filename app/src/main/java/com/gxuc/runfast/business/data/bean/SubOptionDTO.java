package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

public class SubOptionDTO implements Mapper<SubOption> {

    public long id;
    public String name;
    public String sort;

    @Override
    public SubOption map() {
        SubOption option = new SubOption();
        option.id = id;
        option.name = Utils.emptyToValue(name, "");
        option.order = Utils.emptyToValue(sort, "0");
        return option;
    }
}
