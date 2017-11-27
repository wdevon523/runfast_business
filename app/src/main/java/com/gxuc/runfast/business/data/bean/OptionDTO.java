package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class OptionDTO implements Mapper<Option> {

    public long id;
    public String name;
    public List<SubOptionDTO> subOption;

    @Override
    public Option map() {
        Option option = new Option();
        option.id = id;
        option.name = Utils.emptyToValue(name, "");

        ArrayList<SubOption> options = new ArrayList<>();
        if (subOption != null && !subOption.isEmpty()) {
            for (SubOptionDTO dto : subOption) {
                options.add(dto.map());
            }
        }
        option.options = options;

        return option;
    }
}
