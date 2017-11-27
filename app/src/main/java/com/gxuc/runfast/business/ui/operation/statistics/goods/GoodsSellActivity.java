package com.gxuc.runfast.business.ui.operation.statistics.goods;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityGoodsSellBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.statistics.BusinessStatisticsDecoration;
import com.gxuc.runfast.business.ui.operation.statistics.Searching;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 商品销售统计
 * Created by Berial on 2017/8/21.
 */
public class GoodsSellActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback,
        Searching, NeedDataBinding<ActivityGoodsSellBinding> {

    private GoodsSellViewModel mVM;

    private ActivityGoodsSellBinding mBinding;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_goods_sell;
    }

    @Override
    public String thisTitle() {
        return "商品销售统计";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GoodsSellViewModel thisViewModel() {
        return new GoodsSellViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityGoodsSellBinding binding) {
        mBinding = binding;
        mBinding.setView(this);
        mBinding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refresh;
        refresh.setOnLoadmoreListener(layout -> mVM.loadMore());

        final RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.addItemDecoration(new BusinessStatisticsDecoration(this, true));
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
