package com.gxuc.runfast.business.data.bean;

import android.text.TextUtils;

import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.Mapper;
import com.gxuc.runfast.business.util.Utils;

public class CommentDTO implements Mapper<Comment> {

    public long id;
    public String userName;
    public String createTime;
    public Integer score;
    public String content;
    public String feedback;
    public String shangstr;
    public String pic;

    @Override
    public Comment map() {
        Comment comment = new Comment();
        comment.id = id;
        comment.avatarUrl = TextUtils.isEmpty(pic) ? "" : ApiServiceFactory.HOST + pic;
        comment.username = Utils.emptyToValue(userName, "匿名用户");
        comment.time = Utils.emptyToValue(createTime, "");
        comment.star = score == null ? 0 : score;
        comment.content = Utils.emptyToValue(content, "该用户暂无评论");

        comment.hasFeedBack = !TextUtils.isEmpty(feedback);
        comment.feedback = "商家回复：" + Utils.emptyToValue(feedback, "");

        comment.hasLabel = !TextUtils.isEmpty(shangstr);
        if (!TextUtils.isEmpty(shangstr)) {
            if (shangstr.charAt(shangstr.length() - 1) == ',') {
                comment.label = shangstr.substring(0, shangstr.length() - 1);
            } else {
                comment.label = shangstr.replace(",", "，");
            }
        } else {
            comment.label = "";
        }
        return comment;
    }
}
