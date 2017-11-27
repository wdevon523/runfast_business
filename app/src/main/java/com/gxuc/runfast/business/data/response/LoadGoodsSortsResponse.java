package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.data.bean.GoodsSortDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表
 * Created by Berial on 2017/8/28.
 */
public class LoadGoodsSortsResponse extends BaseResponse implements Mapper<List<GoodsSort>> {

    @SerializedName("rows")
    public List<GoodsSortDTO> sorts;

    @Override
    public List<GoodsSort> map() {
        ArrayList<GoodsSort> list = new ArrayList<>();
        if (sorts != null && !sorts.isEmpty()) {
            for (GoodsSortDTO dto : sorts) {
                list.add(dto.map());
            }
        }
        return list;
    }
}
