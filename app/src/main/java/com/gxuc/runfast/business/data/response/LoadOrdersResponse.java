package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Order;
import com.gxuc.runfast.business.data.bean.OrderDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * Created by Berial on 2017/8/31.
 */
public class LoadOrdersResponse extends BaseResponse implements Mapper<List<Order>> {

    public List<OrderDTO> rows;
    public int totalPage;
    public int total;
    public double totalAmount;

    @Override
    public List<Order> map() {
        ArrayList<Order> orders = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            for (OrderDTO dto : rows) {
                orders.add(dto.map());
            }
        }
        return orders;
    }
}
