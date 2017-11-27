package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.data.bean.GoodsSortDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表
 * Created by Berial on 2017/8/28.
 */
public class LoadGoodsResponse extends BaseResponse implements Mapper<List<GoodsSort>> {

    public List<GoodsSortDTO> rows;

    @Override
    public List<GoodsSort> map() {
        ArrayList<GoodsSort> list = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            final int size = rows.size();
            for (int i = 0; i < size; i++) {
                GoodsSortDTO dto = rows.get(i);
                GoodsSort sort = dto.map();
                sort.selected = i == 0;
                list.add(sort);
            }
        }
        return list;
    }
}
