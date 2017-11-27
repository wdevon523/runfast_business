package com.gxuc.runfast.business.ui.operation.comment.detail;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityCommentDetailBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;

/**
 * 意见反馈
 * Created by Berial on 2017/8/20.
 */
public class CommentDetailActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, CommentDetailNavigator, ProgressHelper.Callback,
        LoadingCallback, NeedDataBinding<ActivityCommentDetailBinding> {

    private CommentDetailViewModel mVM;

    private ActivityCommentDetailBinding mBinding;

    private ProgressHelper helper;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_comment_detail;
    }

    @Override
    public String thisTitle() {
        return "意见反馈";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected CommentDetailViewModel thisViewModel() {
        long id = getIntent().getLongExtra("id", 0L);
        return new CommentDetailViewModel(this, id, this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityCommentDetailBinding binding) {
        mBinding = binding;
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
    }

    @Override
    protected void onLoadData() {
        mBinding.progress.showLoading();
        mVM.start();
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Override
    public void onReplyCommentSuccess() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onFirstLoadFinish() {
        mBinding.progress.showContent();
    }

    @Override
    public void onRefreshFinish() {}

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {}

    @Override
    public void onLoadEmpty(String text) {}
}
