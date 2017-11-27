package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;

public class UploadFileResponse extends BaseResponse {

    public String fileName;

    public long fileSize;

    public String filePath;

    @SerializedName("mini_fileName")
    public String thumbnailPath;
}
