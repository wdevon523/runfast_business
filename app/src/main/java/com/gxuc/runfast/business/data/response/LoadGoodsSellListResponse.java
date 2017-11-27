package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.GoodsSell;
import com.gxuc.runfast.business.data.bean.GoodsSellDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品销售统计
 * Created by Berial on 2017/8/26.
 */
public class LoadGoodsSellListResponse extends BaseResponse implements Mapper<List<GoodsSell>> {

    public List<GoodsSellDTO> sell;
    public int totalPage;

    @Override
    public List<GoodsSell> map() {
        ArrayList<GoodsSell> orders = new ArrayList<>();
        if (sell != null && !sell.isEmpty()) {
            for (GoodsSellDTO dto : sell) {
                orders.add(dto.map());
            }
        }
        return orders;
    }
}
