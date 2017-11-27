package com.gxuc.runfast.business.ui.operation.goods.activity.add.menu;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.LayoutRefreshWithToolbarBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithMenu;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.recyclerview.DividerDecoration;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.Set;

/**
 * 选择菜单
 * Created by Berial on 2017/9/1.
 */
public class SelectGoodsActivity extends BaseActivity
        implements WithToolbar, WithMenu, LayoutProvider, LoadingCallback,
        NeedDataBinding<LayoutRefreshWithToolbarBinding> {

    private SelectGoodsViewModel mVM;

    private LayoutRefreshWithToolbarBinding mBinding;

    @Override
    public String thisTitle() {
        return "选择菜单";
    }

    @Override
    public int thisMenu() {
        return R.menu.menu_select_goods;
    }

    @Override
    public int thisLayoutId() {
        return R.layout.layout_refresh_with_toolbar;
    }

    @Override
    public Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener() {
        return item -> {
            Intent result = new Intent();

            String ids = "";
            Set<Long> goodsIds = mVM.getSelectGoodsIds();
            for (Long id : goodsIds) {
                ids += id + ",";
            }

            String standardIds = "";
            Set<Long> selectStandardIds = mVM.getSelectStandardIds();
            for (Long id : selectStandardIds) {
                standardIds += id + ",";
            }

            result.putExtra("ids", ids.isEmpty() ? "" : ids.substring(0, ids.length() - 1));
            result.putExtra("standardIds", standardIds.isEmpty() ? "" : standardIds.substring(0, standardIds.length() - 1));

            setResult(Activity.RESULT_OK, result);

            onBackPressed();

            return true;
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected SelectGoodsViewModel thisViewModel() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        String ids = intent.getStringExtra("ids");
        String standardIds = intent.getStringExtra("standardIds");
        return new SelectGoodsViewModel(this, type, ids, standardIds, this);
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
        refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mVM.loadMoreActivityGoods();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mVM.start();
            }
        });

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
