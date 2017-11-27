package com.gxuc.runfast.business.event;

/**
 * 设置营业状态
 * Created by Berial on 2017/8/20.
 */
public class ChangeBusinessStatusEvent {

    public String statusName;
    public int status;

    public ChangeBusinessStatusEvent() {}

    public ChangeBusinessStatusEvent(String statusName) {
        this.statusName = statusName;
    }
}
