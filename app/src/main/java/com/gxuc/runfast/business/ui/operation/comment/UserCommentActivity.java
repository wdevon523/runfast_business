package com.gxuc.runfast.business.ui.operation.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.LayoutRefreshWithToolbarBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.recyclerview.SpaceDecoration;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.comment.detail.CommentDetailActivity;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

/**
 * 用户评价
 * Created by Berial on 2017/8/21.
 */
public class UserCommentActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback, CommentNavigator,
        NeedDataBinding<LayoutRefreshWithToolbarBinding> {

    private CommentViewModel mVM;

    private LayoutRefreshWithToolbarBinding mBinding;

    @Override
    public String thisTitle() {
        return "用户评价";
    }

    @Override
    public int thisLayoutId() {
        return R.layout.layout_refresh_with_toolbar;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected CommentViewModel thisViewModel() {
        return new CommentViewModel(this, this, this);
    }

    @Override
    protected void onLoadData() {
        mBinding.refreshRoot.progress.showLoading();
        mVM.start();
    }

    @Override
    public void onBoundDataBinding(LayoutRefreshWithToolbarBinding binding) {
        mBinding = binding;
        mVM = findOrCreateViewModel();
        mBinding.setAdapter(mVM.mAdapter);
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refreshRoot.refresh;
        refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mVM.loadMoreComments();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mVM.start();
            }
        });

        final RecyclerView recyclerView = mBinding.refreshRoot.recyclerView;
        recyclerView.setAnimation(null);
        recyclerView.addItemDecoration(new SpaceDecoration(
                (int) Utils.dp2px(this, 10)
        ));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void toCommentDetail(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        startActForResult(CommentDetailActivity.class, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mVM.start();
        }
    }

    @Override
    public void onFirstLoadFinish() {
        mBinding.refreshRoot.progress.showContent();
    }

    @Override
    public void onRefreshFinish() {
        mBinding.refreshRoot.refresh.finishRefresh();
        mBinding.refreshRoot.refresh.setEnableLoadmore(true);
    }

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {
        mBinding.refreshRoot.refresh.finishLoadmore();
        mBinding.refreshRoot.refresh.setEnableLoadmore(!isLastPage);
    }

    @Override
    public void onLoadEmpty(String text) {
        mBinding.refreshRoot.progress.showEmpty(R.drawable.empty_order, text, "");
    }
}
