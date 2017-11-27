package com.gxuc.runfast.business.ui.operation.message;

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
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

/**
 * 消息中心
 * Created by Berial on 2017/8/20.
 */
public class MessageCenterActivity extends BaseActivity
        implements WithToolbar, LayoutProvider,LoadingCallback,
        NeedDataBinding<LayoutRefreshWithToolbarBinding> {

    private MessageViewModel mVM;

    private LayoutRefreshWithToolbarBinding mBinding;

    @Override
    public int thisLayoutId() {
        return R.layout.layout_refresh_with_toolbar;
    }

    @Override
    public String thisTitle() {
        return "消息中心";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MessageViewModel thisViewModel() {
        return new MessageViewModel(this, this);
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
                mVM.loadMoreMessages();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mVM.start();
            }
        });

        final RecyclerView recyclerView = mBinding.refreshRoot.recyclerView;
        recyclerView.addItemDecoration(new SpaceDecoration(
                (int) Utils.dp2px(this, 10)
        ));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
