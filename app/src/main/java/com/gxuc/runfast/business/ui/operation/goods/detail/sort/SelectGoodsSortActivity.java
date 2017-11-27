package com.gxuc.runfast.business.ui.operation.goods.detail.sort;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

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

/**
 * 选择商品分类
 * Created by Berial on 2017/9/13.
 */
public class SelectGoodsSortActivity extends BaseActivity
        implements WithToolbar, WithMenu, LayoutProvider, LoadingCallback,
        NeedDataBinding<LayoutRefreshWithToolbarBinding> {

    private SelectGoodsSortViewModel mVM;

    private LayoutRefreshWithToolbarBinding mBinding;

    @Override
    public String thisTitle() {
        return "商品分类";
    }

    @Override
    public int thisMenu() {
        return R.menu.menu_select_goods;
    }

    @Override
    public Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener() {
        return item -> {
            Intent result = new Intent();

            result.putExtra("sortId", mVM.sortId.get());
            result.putExtra("sortName", mVM.sortName.get());

            setResult(Activity.RESULT_OK, result);

            onBackPressed();

            return true;
        };
    }

    @Override
    public int thisLayoutId() {
        return R.layout.layout_refresh_with_toolbar;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected SelectGoodsSortViewModel thisViewModel() {
        return new SelectGoodsSortViewModel(this, this);
    }

    @Override
    public void onBoundDataBinding(LayoutRefreshWithToolbarBinding binding) {
        mBinding = binding;
        mVM = findOrCreateViewModel();
        binding.setAdapter(mVM.adapter);
    }

    @Override
    protected void onInitViews() {
        final SmartRefreshLayout refresh = mBinding.refreshRoot.refresh;
        refresh.setEnableLoadmore(false);
        refresh.setOnRefreshListener(layout -> mVM.start());

        final RecyclerView recyclerView = mBinding.refreshRoot.recyclerView;

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        recyclerView.setLayoutParams(params);

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
        Intent intent = getIntent();
        mVM.sortId.set(intent.getLongExtra("sortId", 0L));
        mVM.sortName.set(intent.getStringExtra("sortName"));

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
    }

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {}

    @Override
    public void onLoadEmpty(String text) {
        mBinding.refreshRoot.progress.showEmpty(R.drawable.empty_order, "暂无商品分类！", "");
    }
}
