package com.gxuc.runfast.business.ui.mine.feedback;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityFeedbackBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;

/**
 * 意见反馈
 * Created by Berial on 2017/8/20.
 */
public class FeedbackActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, FeedbackNavigator, ProgressHelper.Callback,
        NeedDataBinding<ActivityFeedbackBinding> {

    private ProgressHelper helper;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public String thisTitle() {
        return "意见反馈";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected FeedbackViewModel thisViewModel() {
        return new FeedbackViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityFeedbackBinding binding) {
        binding.setViewModel(findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Override
    public void onSubmitFeedbackSuccess() {
        onBackPressed();
    }
}
