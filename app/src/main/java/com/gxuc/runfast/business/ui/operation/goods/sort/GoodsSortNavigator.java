package com.gxuc.runfast.business.ui.operation.goods.sort;

import com.gxuc.runfast.business.data.bean.GoodsSort;

interface GoodsSortNavigator {

    void toEdit(GoodsSort sort);

    void toAddGoods(GoodsSort sort);

    void onSuccess();
}
