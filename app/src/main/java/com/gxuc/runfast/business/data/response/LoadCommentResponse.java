package com.gxuc.runfast.business.data.response;

import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.data.bean.Comment;
import com.gxuc.runfast.business.data.bean.CommentDTO;

public class LoadCommentResponse extends BaseResponse implements Mapper<Comment> {

    public CommentDTO businessComment;

    @Override
    public Comment map() {
        if (businessComment != null) {
            return businessComment.map();
        } else {
            return new CommentDTO().map();
        }
    }
}
