package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Message;
import com.gxuc.runfast.business.data.bean.MessageDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 * Created by Berial on 2017/8/26.
 */
public class LoadMessagesResponse extends BaseResponse implements Mapper<List<Message>> {

    public List<MessageDTO> rows;

    @SerializedName("totalpage")
    public int totalPage;

    @Override
    public List<Message> map() {
        ArrayList<Message> messages = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            for (MessageDTO dto : rows) {
                messages.add(dto.map());
            }
        }
        return messages;
    }
}
