package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类
 * Created by Berial on 2017/8/28.
 */
public class GoodsSortDTO implements Mapper<GoodsSort> {

    public long id;
    public String content;
    public String name;
    public List<GoodsDTO> glist;

    @Override
    public GoodsSort map() {
        GoodsSort sort = new GoodsSort();
        sort.name = name;
        sort.describe = content;
        sort.id = id;

        ArrayList<Goods> list = new ArrayList<>();
        if (glist != null && !glist.isEmpty()) {
            for (GoodsDTO dto : glist) {
                list.add(dto.map());
            }
        }
        sort.goods = list;
        return sort;
    }
}
