package com.gxuc.runfast.business.ui.operation.goods.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.bean.Activity;
import com.gxuc.runfast.business.databinding.LayoutRefreshWithToolbarBinding;
import com.gxuc.runfast.business.event.UpdateActivitySuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithMenu;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.recyclerview.SpaceDecoration;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.goods.activity.add.AddActivityActivity;
import com.gxuc.runfast.business.ui.operation.goods.activity.detail.ActivityDetailActivity;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 活动列表
 * Created by Berial on 2017/8/21.
 */
public class ActivityListActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, WithMenu, LoadingCallback, ActivityListNavigator,
        NeedDataBinding<LayoutRefreshWithToolbarBinding> {

    private ActivityListViewModel mVM;

    private LayoutRefreshWithToolbarBinding mBinding;

    private Toolbar.OnMenuItemClickListener mListener = item -> {
        startAct(AddActivityActivity.class);
        return true;
    };

    @Override
    public String thisTitle() {
        return "活动列表";
    }

    @Override
    public int thisLayoutId() {
        return R.layout.layout_refresh_with_toolbar;
    }

    @Override
    public int thisMenu() {
        return R.menu.menu_add_activity;
    }

    @Override
    public Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener() {
        return mListener;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ActivityListViewModel thisViewModel() {
        return new ActivityListViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(LayoutRefreshWithToolbarBinding binding) {
        mBinding = binding;
        mVM = findOrCreateViewModel();
        mBinding.setAdapter(mVM.mAdapter);
    }

    @Override
    protected void onLoadData() {
        mBinding.refreshRoot.progress.showLoading();
        mVM.start();
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refreshRoot.refresh;
        refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mVM.loadMoreActivities();
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
    public void onFirstLoadFinish() {
        mBinding.refreshRoot.progress.showContent();
    }

    @Override
    public void onRefreshFinish() {
        mBinding.refreshRoot.progress.showContent();
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

    @Override
    public void viewActivityDetail(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("activity", activity);
        startAct(ActivityDetailActivity.class, bundle);
    }

    @Subscribe
    public void updateActivitySuccess(UpdateActivitySuccessEvent event) {
        mBinding.refreshRoot.progress.showLoading();
        mVM.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
