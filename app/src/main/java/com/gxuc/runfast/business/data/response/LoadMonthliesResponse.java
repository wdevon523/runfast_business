package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Monthly;
import com.gxuc.runfast.business.data.bean.MonthlyDTO;

import java.util.ArrayList;
import java.util.List;

public class LoadMonthliesResponse extends BaseResponse implements Mapper<List<Monthly>> {

    public List<MonthlyDTO> sell;
    public int totalPage;

    @Override
    public List<Monthly> map() {
        ArrayList<Monthly> monthlies = new ArrayList<>();
        if (sell != null && !sell.isEmpty()) {
            for (MonthlyDTO dto : sell) {
                monthlies.add(dto.map());
            }
        }
        return monthlies;
    }
}
