package com.gxuc.runfast.business.ui.operation.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityGoodsManageBinding;
import com.gxuc.runfast.business.event.UpdateGoodsSuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.OnTabSelectedAdapter;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.recyclerview.DividerDecoration;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.goods.activity.ActivityListActivity;
import com.gxuc.runfast.business.ui.operation.goods.detail.GoodsDetailActivity;
import com.gxuc.runfast.business.ui.operation.goods.sort.GoodsSortActivity;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.TabLayoutHelper;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 商品管理
 * Created by Berial on 2017/8/20.
 */
public class GoodsManageActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, LoadingCallback, GoodsNavigator,
        ProgressHelper.Callback, NeedDataBinding<ActivityGoodsManageBinding> {

    public LinearLayoutManager sortManager = new LinearLayoutManager(this);
    public LinearLayoutManager goodsManager = new LinearLayoutManager(this);

    private ActivityGoodsManageBinding mBinding;

    private GoodsViewModel mVM;

    private ProgressHelper helper;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_goods_manage;
    }

    @Override
    public String thisTitle() {
        return "商品管理";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GoodsViewModel thisViewModel() {
        return new GoodsViewModel(this, this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityGoodsManageBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
        configTabLayout();
        configSwipeRefreshLayout();
        configRecyclerView();
    }

    private void configSwipeRefreshLayout() {
        final SmartRefreshLayout refresh = mBinding.refresh;
        refresh.setOnRefreshListener(layout -> mVM.start());
    }

    @Override
    protected void onLoadData() {
        mBinding.progress.showLoading();
        mVM.start();
    }

    private void configTabLayout() {
        final TabLayout tabs = mBinding.tabs;

        tabs.addTab(tabs.newTab().setText("全部").setTag(""), true);
        tabs.addTab(tabs.newTab().setText("上架的商品").setTag("0"));
        tabs.addTab(tabs.newTab().setText("下架的商品").setTag("2"));

        TabLayoutHelper.setIndicatorWidth(tabs, 22, 22);

        tabs.addOnTabSelectedListener(new OnTabSelectedAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.progress.showLoading();
                mVM.start((String) tab.getTag());
            }
        });
    }

    private void configRecyclerView() {
        mBinding.sorts.addItemDecoration(new GoodsSortDecoration(this));

        int height = (int) Utils.dp2px(this, 1);
        int padding = (int) Utils.dp2px(this, 28);
        DividerDecoration decoration = new DividerDecoration(
                ContextCompat.getColor(this, R.color.gray6), height, padding, 0
        );
        decoration.setDrawLastItem(false);
        mBinding.items.addItemDecoration(decoration);
    }

    public void toGoodsSort() {
        startAct(GoodsSortActivity.class);
    }

    public void toActivityList() {
        startAct(ActivityListActivity.class);
    }

    public void toAddGoods() {
        startAct(GoodsDetailActivity.class);
    }

    @Override
    public void viewGoodsDetail(long goodsId) {
        Bundle bundle = new Bundle();
        bundle.putLong("goodsId", goodsId);
        startAct(GoodsDetailActivity.class, bundle);
    }

    @Override
    public void onFirstLoadFinish() {
        mBinding.progress.showContent();
    }

    @Override
    public void onRefreshFinish() {
        mBinding.refresh.finishRefresh();
    }

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {}

    @Override
    public void onLoadEmpty(String text) {}

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Subscribe
    public void addGoodsSuccess(UpdateGoodsSuccessEvent event) {
        mBinding.progress.showLoading();
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
