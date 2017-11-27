package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.DayOrder;
import com.gxuc.runfast.business.data.bean.DayOrderDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 当日订单清单
 * Created by Berial on 2017/8/26.
 */
public class LoadDayOrdersResponse extends BaseResponse implements Mapper<List<DayOrder>> {

    public List<DayOrderDTO> sell;
    public int totalPage;

    @Override
    public List<DayOrder> map() {
        ArrayList<DayOrder> orders = new ArrayList<>();
        if (sell != null && !sell.isEmpty()) {
            for (DayOrderDTO dto : sell) {
                orders.add(dto.map());
            }
        }
        return orders;
    }
}
