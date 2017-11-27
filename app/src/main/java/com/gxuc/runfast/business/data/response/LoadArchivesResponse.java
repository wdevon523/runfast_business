package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Archive;
import com.gxuc.runfast.business.data.bean.ArchiveDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 食品安全档案
 * Created by Berial on 2017/9/8.
 */
public class LoadArchivesResponse extends BaseResponse implements Mapper<List<Archive>> {

    public List<ArchiveDTO> rows;

    @Override
    public List<Archive> map() {
        ArrayList<Archive> archives = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            for (ArchiveDTO dto : rows) {
                archives.add(dto.map());
            }
        }
        return archives;
    }
}
