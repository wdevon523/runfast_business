package com.gxuc.runfast.business.ui.operation.goods.activity.detail;

import android.os.Bundle;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityActivityDetailBinding;
import com.gxuc.runfast.business.event.UpdateActivitySuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.operation.goods.activity.detail.goods.GoodsListActivity;
import com.gxuc.runfast.business.util.ProgressHelper;

import org.greenrobot.eventbus.EventBus;


/**
 * 活动详细
 * Created by Berial on 2017/8/21.
 */
public class ActivityDetailActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, ProgressHelper.Callback, ActivityDetailNavigator,
        NeedDataBinding<ActivityActivityDetailBinding> {

    private ActivityDetailViewModel mVM;

    private ProgressHelper helper;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_activity_detail;
    }

    @Override
    public String thisTitle() {
        return "活动详细";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ActivityDetailViewModel thisViewModel() {
        return new ActivityDetailViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityActivityDetailBinding binding) {
        binding.setViewModel(mVM = findOrCreateViewModel());
        binding.setView(this);
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
    }

    @Override
    protected void onLoadData() {
        mVM.activityObservable.set(
                getIntent().getParcelableExtra("activity"));
    }

    @Override
    public void viewGoodsList(int type, String ids, String standardIds) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("ids", ids);
        bundle.putString("standardIds", standardIds);
        startAct(GoodsListActivity.class, bundle);
    }

    @Override
    public void changeActivityStatusSuccess() {
        EventBus.getDefault().post(new UpdateActivitySuccessEvent());
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }
}
