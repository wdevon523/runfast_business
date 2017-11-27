package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Goods;
import com.gxuc.runfast.business.data.bean.GoodsDTO;

/**
 * 获取商品详情
 * Created by Berial on 2017/9/8.
 */
public class LoadGoodsDetailResponse extends BaseResponse implements Mapper<Goods> {

    public GoodsDTO good;

    @Override
    public Goods map() {
        if (good == null) return new GoodsDTO().map();
        else return good.map();
    }
}
