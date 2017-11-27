package com.gxuc.runfast.business.ui.operation.statistics;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityBusinessStatisticsBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.statistics.goods.GoodsSellActivity;
import com.gxuc.runfast.business.ui.operation.statistics.monthly.MonthlyActivity;
import com.gxuc.runfast.business.ui.operation.statistics.order.DayOrderActivity;

/**
 * 营业统计
 * Created by Berial on 2017/8/20.
 */
public class BusinessStatisticsActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback,
        NeedDataBinding<ActivityBusinessStatisticsBinding> {

    private BusinessStatisticsViewModel mVM;

    private ActivityBusinessStatisticsBinding mBinding;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_business_statistics;
    }

    @Override
    public String thisTitle() {
        return "营业统计";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BusinessStatisticsViewModel thisViewModel() {
        return new BusinessStatisticsViewModel(this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityBusinessStatisticsBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onLoadData() {
        mBinding.progress.showLoading();
        mVM.start();
    }

    public void toGoodsSell() {
        startAct(GoodsSellActivity.class);
    }

    public void toMonthly() {
        startAct(MonthlyActivity.class);
    }

    public void toDayOrder() {
        startAct(DayOrderActivity.class);
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
