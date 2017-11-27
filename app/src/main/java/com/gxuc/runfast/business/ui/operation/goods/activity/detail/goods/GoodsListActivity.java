package com.gxuc.runfast.business.ui.operation.goods.activity.detail.goods;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.LayoutRefreshWithToolbarBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.recyclerview.DividerDecoration;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 商品列表
 * Created by Berial on 2017/9/7.
 */
public class GoodsListActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback,
        NeedDataBinding<LayoutRefreshWithToolbarBinding> {

    private GoodsListViewModel mVM;

    private LayoutRefreshWithToolbarBinding mBinding;

    @Override
    public String thisTitle() {
        return "商品列表";
    }

    @Override
    public int thisLayoutId() {
        return R.layout.layout_refresh_with_toolbar;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GoodsListViewModel thisViewModel() {
        Intent intent = getIntent();
        return new GoodsListViewModel(
                this,
                intent.getIntExtra("type", 2),
                intent.getStringExtra("ids"),
                intent.getStringExtra("standardIds"),
                this);
    }

    @Override
    public void onBoundDataBinding(LayoutRefreshWithToolbarBinding binding) {
        mBinding = binding;
        mVM = findOrCreateViewModel();
        binding.setAdapter(mVM.mAdapter);
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refreshRoot.refresh;
        refresh.setEnableRefresh(false);
        refresh.setOnLoadmoreListener(layout -> mVM.loadMoreActivityGoods());

        final RecyclerView recyclerView = mBinding.refreshRoot.recyclerView;
        recyclerView.setBackgroundResource(R.color.white);
        DividerDecoration decoration = new DividerDecoration(
                ContextCompat.getColor(this, R.color.gray6), (int) Utils.dp2px(this, 1), (int) Utils.dp2px(this, 15), 0
        );
        decoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onLoadData() {
        mBinding.refreshRoot.progress.showLoading();
        mVM.start();
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
        mBinding.refreshRoot.progress.showEmpty(R.drawable.empty_order, "暂无商品！", "");
    }
}
