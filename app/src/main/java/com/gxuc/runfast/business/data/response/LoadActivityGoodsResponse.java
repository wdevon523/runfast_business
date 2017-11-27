package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Goods;
import com.gxuc.runfast.business.data.bean.GoodsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动商品选单
 * Created by Berial on 2017/9/1.
 */
public class LoadActivityGoodsResponse extends BaseResponse implements Mapper<List<Goods>> {

    @SerializedName(value = "totalpage", alternate = { "gvtotalPage", "totalPage" })
    public int totalPage;

    @SerializedName(value = "goodsell", alternate = "goodsellview")
    public List<GoodsDTO> goodsell;

    @Override
    public List<Goods> map() {
        ArrayList<Goods> goods = new ArrayList<>();
        if (goodsell != null && !goodsell.isEmpty()) {
            for (GoodsDTO dto : goodsell) {
                goods.add(dto.map());
            }
        }
        return goods;
    }
}
