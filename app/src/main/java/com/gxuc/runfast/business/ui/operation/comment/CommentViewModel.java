package com.gxuc.runfast.business.ui.operation.comment;

import android.content.Context;

import com.gxuc.runfast.business.ItemCommentBindingModel_;
import com.gxuc.runfast.business.data.bean.Comment;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息中心
 * Created by Berial on 2017/8/26.
 */
public class CommentViewModel extends BaseViewModel {

    final Adapter mAdapter = new Adapter();

    private OperationRepo mRepo = OperationRepo.get();

    private CommentNavigator mNavigator;
    private LoadingCallback mCallback;

    private int page;
    private boolean isFirstLoad = true;

    CommentViewModel(Context context, CommentNavigator navigator, LoadingCallback callback) {
        super(context);
        mNavigator = navigator;
        mCallback = callback;
    }

    void start() {
        mRepo.resetCommentPages();
        loadComments(page = 1);
    }

    void loadMoreComments() {
        int maxPage = mRepo.getCommentPages();
        if (page < maxPage) {
            loadComments(++page);
        }
    }

    private void loadComments(final int page) {
        mRepo.loadComments(page)
                .compose(RxLifecycle.<List<Comment>>bindLifecycle(this))
                .doFinally(() -> {
                    if (page == 1) mCallback.onRefreshFinish();
                    if (isFirstLoad) {
                        isFirstLoad = false;
                        if (mAdapter.isEmpty()) {
                            mCallback.onLoadEmpty("暂时没有用户评价!");
                        } else {
                            mCallback.onFirstLoadFinish();
                        }
                    }
                    mCallback.onLoadMoreFinish(isLastPage(page));
                })
                .subscribe(new ResponseSubscriber<List<Comment>>(mContext) {
                    @Override
                    public void onNext(List<Comment> comments) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateCommentModels(comments));
                        } else {
                            mAdapter.addMore(generateCommentModels(comments));
                        }
                    }
                });
    }

    private List<ItemCommentBindingModel_> generateCommentModels(List<Comment> comments) {
        ArrayList<ItemCommentBindingModel_> models = new ArrayList<>();
        for (Comment comment : comments) {
            models.add(new ItemCommentBindingModel_()
                    .id(comment.id)
                    .avatarUrl(comment.avatarUrl)
                    .username(comment.username)
                    .time(comment.time)
                    .star(comment.star)
                    .content(comment.content)
                    .hasLabel(comment.hasLabel)
                    .hasFeedback(comment.hasFeedBack)
                    .feedback(comment.feedback)
                    .label(comment.label)
                    .viewModel(this));
        }
        return models;
    }

    public void toCommentDetail(long id) {
        mNavigator.toCommentDetail(id);
    }

    private boolean isLastPage(int currentPage) {
        return currentPage == mRepo.getCommentPages();
    }
}
