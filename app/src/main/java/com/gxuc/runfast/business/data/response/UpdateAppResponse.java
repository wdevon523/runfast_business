package com.gxuc.runfast.business.data.response;

/**
 * Created by Devon on 2017/12/14.
 */

public class UpdateAppResponse {


    /**
     * update : true
     * apkName : runfastbiz.apk
     * downloadUrl : http://image.gxptkc.com/apk/runfastbiz.apk
     * msg : 您的版本过低，请更新版本后再登录
     * 新版本更新内容如下：
     * 优化功能
     * <p>
     * success : true
     */

    public boolean update;
    public String apkName;
    public String downloadUrl;
    public String msg;
    public boolean success;

}
