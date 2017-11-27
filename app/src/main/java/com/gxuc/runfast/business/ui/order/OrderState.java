package com.gxuc.runfast.business.ui.order;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.gxuc.runfast.business.ui.order.OrderState.CANCELED;
import static com.gxuc.runfast.business.ui.order.OrderState.COMPLETED;
import static com.gxuc.runfast.business.ui.order.OrderState.GOING;
import static com.gxuc.runfast.business.ui.order.OrderState.PENDING;

/**
 * 订单状态
 * Created by Berial on 2017/8/21.
 */
@IntDef({
        PENDING, CANCELED, GOING, COMPLETED
})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderState {

    /**
     * 待处理
     */
    int PENDING = 1;

    /**
     * 进行中
     */
    int GOING = 2;

    /**
     * 已完成
     */
    int COMPLETED = 3;

    /**
     * 已取消
     */
    int CANCELED = 4;
}
