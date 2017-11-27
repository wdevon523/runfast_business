package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.Mapper;

public class ArchiveDTO implements Mapper<Archive> {

    public long id;
    public String imgUrl;

    @Override
    public Archive map() {
        Archive archive = new Archive();
        archive.id = id;
        archive.imageUrl = TextUtils.isEmpty(imgUrl) ? "" : ApiServiceFactory.HOST + imgUrl;
        return archive;
    }
}
