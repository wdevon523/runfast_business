package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.CashRecord;
import com.gxuc.runfast.business.data.bean.CashRecordDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现记录
 * Created by Berial on 2017/8/26.
 */
public class LoadCashRecordsResponse extends BaseResponse implements Mapper<List<CashRecord>> {

    public List<CashRecordDTO> withdraw;
    public int totalPage;

    @Override
    public List<CashRecord> map() {
        ArrayList<CashRecord> records = new ArrayList<>();
        if (withdraw != null && !withdraw.isEmpty()) {
            for (CashRecordDTO dto : withdraw) {
                records.add(dto.map());
            }
        }
        return records;
    }
}
