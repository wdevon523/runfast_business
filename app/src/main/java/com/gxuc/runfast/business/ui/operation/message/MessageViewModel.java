package com.gxuc.runfast.business.ui.operation.message;

import android.content.Context;

import com.gxuc.runfast.business.ItemMessageBindingModel_;
import com.gxuc.runfast.business.data.bean.Message;
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
class MessageViewModel extends BaseViewModel {

    final Adapter mAdapter = new Adapter();

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    private boolean isFirstLoad = true;
    private int page;

    MessageViewModel(Context context, LoadingCallback callback) {
        super(context);
        mCallback = callback;
    }

    void start() {
        mRepo.resetMessagePages();
        loadMessages(page = 1);
    }

    void loadMoreMessages() {
        int maxPage = mRepo.getMessagePages();
        if (page < maxPage) {
            loadMessages(++page);
        }
    }

    private void loadMessages(final int page) {
        mRepo.loadMessages(page)
                .compose(RxLifecycle.bindLifecycle(this))
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
                .subscribe(new ResponseSubscriber<List<Message>>(mContext) {
                    @Override
                    public void onSuccess(List<Message> messages) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateMessageModels(messages));
                        } else {
                            mAdapter.addMore(generateMessageModels(messages));
                        }
                    }
                });
    }

    private boolean isLastPage(int currentPage) {
        return currentPage == mRepo.getMessagePages();
    }

    private List<ItemMessageBindingModel_> generateMessageModels(List<Message> messages) {
        ArrayList<ItemMessageBindingModel_> models = new ArrayList<>();
        for (Message message : messages) {
            models.add(new ItemMessageBindingModel_()
                    .date(message.date)
                    .content(message.content)
                    .title(message.title));
        }
        return models;
    }
}
