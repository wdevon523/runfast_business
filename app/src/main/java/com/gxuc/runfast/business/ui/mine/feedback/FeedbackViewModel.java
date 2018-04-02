package com.gxuc.runfast.business.ui.mine.feedback;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 意见反馈
 * Created by Berial on 2017/9/1.
 */
public class FeedbackViewModel extends BaseViewModel {

    public ObservableField<String> feedback = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();

    private BusinessRepo mRepo = BusinessRepo.get();

    private FeedbackNavigator mNavigator;
    private ProgressHelper.Callback mCallback;

    FeedbackViewModel(Context context, FeedbackNavigator navigator, ProgressHelper.Callback callback) {
        super(context);
        mNavigator = navigator;
        mCallback = callback;
    }

    public void submitFeedback() {
        if (TextUtils.isEmpty(feedback.get())) {
            toast("请输入反馈内容");
            return;
        }
        mCallback.setLoading(true);
        mRepo.submitFeedback(feedback.get(), email.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onSubmitFeedbackSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }
}
