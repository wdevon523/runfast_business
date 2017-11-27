package com.gxuc.runfast.business.ui.operation.goods.sort;

import android.support.v7.widget.Toolbar;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.databinding.ActivityGoodsSortDetailBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithMenu;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;


/**
 * Created by huiliu on 2017/8/21.
 *
 * @email liu594545591@126.com
 * @introduce
 */
public class GoodsSortDetailActivity extends BaseActivity
        implements WithToolbar, WithMenu, LayoutProvider, ProgressHelper.Callback, GoodsSortNavigator,
        NeedDataBinding<ActivityGoodsSortDetailBinding> {

    private GoodsSortViewModel mVM;

    private ProgressHelper helper;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_goods_sort_detail;
    }

    @Override
    public String thisTitle() {
        GoodsSort sort = getIntent().getParcelableExtra("sort");
        return sort == null ? "新增分类" : "修改分类";
    }

    @Override
    public int thisMenu() {
        return R.menu.menu_goods_sort;
    }

    @Override
    public Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener() {
        return item -> {
            mVM.deleteGoodsSort();
            return true;
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GoodsSortViewModel thisViewModel() {
        GoodsSort sort = getIntent().getParcelableExtra("sort");
        return new GoodsSortViewModel(this, sort, this, this, null);
    }

    @Override
    public void onBoundDataBinding(ActivityGoodsSortDetailBinding binding) {
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        GoodsSort sort = getIntent().getParcelableExtra("sort");
        if (sort == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.getMenu().clear();
        }
        helper = new ProgressHelper(this);
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Override
    public void toEdit(GoodsSort sort) {}

    @Override
    public void toAddGoods(GoodsSort sort) {}

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        onBackPressed();
    }
}
