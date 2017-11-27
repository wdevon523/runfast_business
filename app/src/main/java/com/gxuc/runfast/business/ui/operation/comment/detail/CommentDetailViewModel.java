package com.gxuc.runfast.business.ui.operation.comment.detail;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.gxuc.runfast.business.data.bean.Comment;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 意见反馈
 * Created by Berial on 2017/9/1.
 */
public class CommentDetailViewModel extends BaseViewModel {

    public final ObservableField<String> avatarUrl = new ObservableField<>();
    public final ObservableField<String> username = new ObservableField<>("匿名");
    public final ObservableField<String> time = new ObservableField<>("--");
    public final ObservableInt star = new ObservableInt(0);
    public final ObservableField<String> content = new ObservableField<>("该用户暂无评论");
    public final ObservableField<String> label = new ObservableField<>("");
    public final ObservableBoolean hasLabel = new ObservableBoolean(false);
    public final ObservableBoolean hasReply = new ObservableBoolean(true);
    public final ObservableField<String> reply = new ObservableField<>();

    private final ObservableField<Comment> mCommentObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private long mId;
    private LoadingCallback mLoadingCallback;
    private CommentDetailNavigator mNavigator;
    private ProgressHelper.Callback mCallback;

    CommentDetailViewModel(Context context,
                           long id,
                           CommentDetailNavigator navigator,
                           LoadingCallback loadingCallback,
                           ProgressHelper.Callback callback) {
        super(context);
        mId = id;
        mLoadingCallback = loadingCallback;
        mNavigator = navigator;
        mCallback = callback;
    }

    void start() {
        mCommentObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Comment comment = mCommentObservable.get();
                if (comment != null) {
                    avatarUrl.set(comment.avatarUrl);
                    username.set(comment.username);
                    time.set(comment.time);
                    star.set(comment.star);
                    content.set(comment.content);
                    label.set(comment.label);
                    hasLabel.set(comment.hasLabel);
                    hasReply.set(comment.hasFeedBack);
                    reply.set("商家回复：".equals(comment.feedback) ? "" : comment.feedback);

                    hasReply.notifyChange();
                }
            }
        });
        mRepo.loadCommentDetail(mId)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mLoadingCallback.onFirstLoadFinish())
                .subscribe(new ResponseSubscriber<Comment>(mContext) {
                    @Override
                    public void onNext(Comment comment) {
                        mCommentObservable.set(comment);
                    }
                });
    }

    public void replyComment() {
        if (TextUtils.isEmpty(reply.get())) {
            toast("请输入反馈内容");
            return;
        }
        mCallback.setLoading(true);
        mRepo.replyComment(mId, reply.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onReplyCommentSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }
}
