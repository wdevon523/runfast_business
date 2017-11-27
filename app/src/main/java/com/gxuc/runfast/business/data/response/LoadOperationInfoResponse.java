package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Operation;
import com.gxuc.runfast.business.util.Utils;

/**
 * 获取运营数据
 * Created by Berial on 2017/8/25.
 */
public class LoadOperationInfoResponse extends BaseResponse implements Mapper<Operation> {

    public String count;
    public String total_amount;

    @Override
    public Operation map() {
        Operation operation = new Operation();
        operation.todayIncome = Utils.emptyToValue(total_amount, "0");
        operation.todayOrderCount = Utils.emptyToValue(count, "0");
        return operation;
    }
}
