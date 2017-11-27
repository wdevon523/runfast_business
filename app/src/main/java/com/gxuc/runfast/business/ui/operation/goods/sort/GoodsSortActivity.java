package com.gxuc.runfast.business.ui.operation.goods.sort;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.databinding.ActivityGoodsSortBinding;
import com.gxuc.runfast.business.event.UpdateGoodsSuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.recyclerview.DividerDecoration;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.goods.detail.GoodsDetailActivity;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by huiliu on 2017/8/21.
 *
 * @email liu594545591@126.com
 * @introduce 管理分类
 */
public class GoodsSortActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, GoodsSortNavigator,
        LoadingCallback, NeedDataBinding<ActivityGoodsSortBinding> {

    private GoodsSortViewModel mVM;

    private ActivityGoodsSortBinding mBinding;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_goods_sort;
    }

    @Override
    public String thisTitle() {
        return "管理分类";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GoodsSortViewModel thisViewModel() {
        return new GoodsSortViewModel(this, null, this, null, this);
    }

    @Override
    public void onBoundDataBinding(ActivityGoodsSortBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onLoadData() {
        mBinding.progress.showLoading();
        mVM.start();
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refresh;
        refresh.setOnRefreshListener(layout -> {
            mVM.start();
        });

        int height = (int) Utils.dp2px(this, 1);
        int padding = (int) Utils.dp2px(this, 15);
        DividerDecoration decoration = new DividerDecoration(
                ContextCompat.getColor(this, R.color.gray6), height, padding, 0
        );
        decoration.setDrawLastItem(false);

        RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void addGoodsSuccess(UpdateGoodsSuccessEvent event) {
        mBinding.progress.showLoading();
        mVM.start();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void toAddGoodsSort() {
        startActForResult(GoodsSortDetailActivity.class);
    }

    @Override
    public void toEdit(GoodsSort sort) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("sort", sort);
        startActForResult(GoodsSortDetailActivity.class, bundle);
    }

    @Override
    public void toAddGoods(GoodsSort sort) {
        Bundle bundle = new Bundle();
        bundle.putLong("sortId", sort.id);
        bundle.putString("sortName", sort.name);
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
    public void onLoadEmpty(String text) {
        mBinding.progress.showEmpty(R.drawable.empty_order, text, "");
    }

    @Override
    public void onSuccess() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mVM.start();
        }
    }
}
