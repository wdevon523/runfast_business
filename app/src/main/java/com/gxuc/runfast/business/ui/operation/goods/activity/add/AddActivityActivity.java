package com.gxuc.runfast.business.ui.operation.goods.activity.add;

import android.content.Intent;
import android.os.Bundle;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityAddActivityBinding;
import com.gxuc.runfast.business.event.UpdateActivitySuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.base.DateBottomSheet;
import com.gxuc.runfast.business.ui.operation.goods.activity.add.menu.SelectGoodsActivity;

import org.greenrobot.eventbus.EventBus;


/**
 * 活动详细
 * Created by Berial on 2017/8/21.
 */
public class AddActivityActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, ActivityNavigator,
        NeedDataBinding<ActivityAddActivityBinding> {

    private ActivityViewModel mVM;

    private ActivityTypeBottomSheet mTypePicker;
    private DateBottomSheet mStartTimePicker;
    private DateBottomSheet mEndTimePicker;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_add_activity;
    }

    @Override
    public String thisTitle() {
        return "活动详细";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ActivityViewModel thisViewModel() {
        return new ActivityViewModel(this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityAddActivityBinding binding) {
        binding.setViewModel(mVM = findOrCreateViewModel());
        binding.setView(this);
    }

    @Override
    protected void onInitViews() {
        mTypePicker = new ActivityTypeBottomSheet(this);
        mTypePicker.setCallback((type, typeName) -> {
            mVM.type.set(type);
            mVM.typeName.set(typeName);
        });

        mStartTimePicker = new DateBottomSheet(this);
        mStartTimePicker.setCallback(date -> mVM.startTime.set(date));

        mEndTimePicker = new DateBottomSheet(this);
        mEndTimePicker.setCallback(date -> mVM.endTime.set(date));
    }

    @Override
    public void onEditSuccess() {
        EventBus.getDefault().post(new UpdateActivitySuccessEvent());
        onBackPressed();
    }

    @Override
    public void toSelectGoods() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", mVM.type.get());
        bundle.putString("ids", mVM.goodsId.get());
        bundle.putString("standardIds", mVM.standardId.get());
        startActForResult(SelectGoodsActivity.class, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (mVM.type.get() == 4) {
                mVM.standardId.set(data.getStringExtra("standardIds"));
                mVM.goodsId.set(data.getStringExtra("ids"));
            } else {
                mVM.goodsId.set(data.getStringExtra("ids"));
            }
        }
    }

    public void showActivityTypePicker() {
        mTypePicker.show();
    }

    public void showStartTimePicker() {
        mStartTimePicker.show();
    }

    public void showEndTimePicker() {
        mEndTimePicker.show();
    }
}
