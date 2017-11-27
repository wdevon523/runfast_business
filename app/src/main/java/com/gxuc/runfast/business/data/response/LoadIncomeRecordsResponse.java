package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.IncomeRecord;
import com.gxuc.runfast.business.data.bean.IncomeRecordDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入记录
 * Created by Berial on 2017/8/26.
 */
public class LoadIncomeRecordsResponse extends BaseResponse implements Mapper<List<IncomeRecord>> {

    public List<IncomeRecordDTO> goodsell;
    public int totalPage;

    @Override
    public List<IncomeRecord> map() {
        ArrayList<IncomeRecord> records = new ArrayList<>();
        if (goodsell != null && !goodsell.isEmpty()) {
            for (IncomeRecordDTO dto : goodsell) {
                records.add(dto.map());
            }
        }
        return records;
    }
}
