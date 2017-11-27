package com.gxuc.runfast.business.data.bean;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

/**
 * 消息
 * Created by Berial on 2017/8/26.
 */
public class MessageDTO implements Mapper<Message> {

    public String createTime;
    public String title;
    public String content;

    @Override
    public Message map() {
        Message message = new Message();
        message.date = Utils.nullToValue(createTime, "");
        message.title = Utils.nullToValue(title, "");
        message.content = Utils.nullToValue(content, "");
        return message;
    }
}
