package com.gxuc.runfast.business.data.response;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Comment;
import com.gxuc.runfast.business.data.bean.CommentDTO;

import java.util.ArrayList;
import java.util.List;

public class LoadCommentsResponse extends BaseResponse implements Mapper<List<Comment>> {

    public List<CommentDTO> bus;

    @SerializedName("totalpage")
    public int totalPage;

    @Override
    public List<Comment> map() {
        ArrayList<Comment> comments = new ArrayList<>();
        if (bus != null && !bus.isEmpty()) {
            for (CommentDTO dto : bus) {
                comments.add(dto.map());
            }
        }
        return comments;
    }
}
