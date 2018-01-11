package com.gxuc.runfast.business.ui.operation.statistics.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityDayOrderBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.statistics.BusinessStatisticsDecoration;
import com.gxuc.runfast.business.ui.operation.statistics.Searching;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 当日订单清单
 * Created by Berial on 2017/8/21.
 */
public class DayOrderActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback,
        Searching, NeedDataBinding<ActivityDayOrderBinding> {

    private DayOrderViewModel mVM;

    private ActivityDayOrderBinding mBinding;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_day_order;
    }

    @Override
    public String thisTitle() {
        return "订单清单";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DayOrderViewModel thisViewModel() {
        return new DayOrderViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityDayOrderBinding binding) {
        mBinding = binding;
        mBinding.setView(this);
        mBinding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refresh;
        refresh.setOnLoadmoreListener(layout -> mVM.loadMoreOrders());

        final RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.addItemDecoration(new BusinessStatisticsDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onLoadData() {
        mBinding.progress.showLoading();
        mVM.start();
    }

    @Override
    public void onSearching() {
        mBinding.progress.showLoading();
    }

    @Override
    public void onFirstLoadFinish() {
        mBinding.progress.showContent();
    }

    @Override
    public void onRefreshFinish() {}

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {
        mBinding.refresh.finishLoadmore();
        mBinding.refresh.setEnableLoadmore(!isLastPage);
    }

    @Override
    public void onLoadEmpty(String text) {}
}
