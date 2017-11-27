package com.gxuc.runfast.business.ui.operation.cash;

import android.os.Bundle;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityCashApplicationBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.cash.record.RecordActivity;
import com.gxuc.runfast.business.util.ProgressHelper;

/**
 * 提现申请
 * Created by Berial on 2017/8/20.
 */
public class CashApplicationActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback, ProgressHelper.Callback,
        NeedDataBinding<ActivityCashApplicationBinding> {

    private CashViewModel mVM;

    private ActivityCashApplicationBinding mBinding;

    private ProgressHelper helper;

    @Override
    public String thisTitle() {
        return "提现申请";
    }

    @Override
    public int thisLayoutId() {
        return R.layout.activity_cash_application;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected CashViewModel thisViewModel() {
        return new CashViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityCashApplicationBinding binding) {
        mBinding = binding;
        binding.setView(this);
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

    public void toRecord(int recordType) {
        Bundle data = new Bundle();
        data.putInt("recordType", recordType);
        startAct(RecordActivity.class, data);
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
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
