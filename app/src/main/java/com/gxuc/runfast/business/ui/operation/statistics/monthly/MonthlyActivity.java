package com.gxuc.runfast.business.ui.operation.statistics.monthly;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityMonthlyBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.statistics.BusinessStatisticsDecoration;
import com.gxuc.runfast.business.ui.operation.statistics.Searching;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 销售月报表
 * Created by Berial on 2017/8/21.
 */
public class MonthlyActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback,
        Searching, NeedDataBinding<ActivityMonthlyBinding> {

    private MonthlyViewModel mVM;

    private ActivityMonthlyBinding mBinding;

    private DateBottomSheet mDatePicker;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_monthly;
    }

    @Override
    public String thisTitle() {
        return "销售月报表";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MonthlyViewModel thisViewModel() {
        return new MonthlyViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityMonthlyBinding binding) {
        mBinding = binding;
        mBinding.setView(this);
        mBinding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        mDatePicker = new DateBottomSheet(this);
        mDatePicker.setCallback(date -> mVM.date.set(date));

        final SmartRefreshLayout refresh = mBinding.refresh;
        refresh.setOnLoadmoreListener(layout -> mVM.loadMoreMonthlies());

        final RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.addItemDecoration(new BusinessStatisticsDecoration(this, true));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onLoadData() {
//        mBinding.progress.showLoading();
        mVM.init();
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

    public void showDatePicker() {
        mDatePicker.show();
    }
}
