package com.gxuc.runfast.business.ui.mine.shop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityShopInfoBinding;
import com.gxuc.runfast.business.event.ChangeBusinessStatusEvent;
import com.gxuc.runfast.business.event.UpdateShopInfoSuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.glide.GlideV4Engine;
import com.gxuc.runfast.business.ui.CropperActivity;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.mine.state.ChangeBusinessStateActivity;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 店铺信息
 * Created by Berial on 2017/8/20.
 */
public class ShopInfoActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, ShopInfoNavigator, LoadingCallback,
        ProgressHelper.Callback, NeedDataBinding<ActivityShopInfoBinding> {

    private static final int REQUEST_CHOOSE_IMAGE_CODE = 1;

    private ActivityShopInfoBinding mBinding;

    private ShopInfoViewModel mVM;

    private ProgressHelper helper;

    private ShopTimeBottomSheet mStartTimePicker;
    private ShopTimeBottomSheet mEndTimePicker;

    private ShopTimeBottomSheet mNextStartTimePicker;
    private ShopTimeBottomSheet mNextEndTimePicker;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_shop_info;
    }

    @Override
    public String thisTitle() {
        return "店铺信息";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ShopInfoViewModel thisViewModel() {
        return new ShopInfoViewModel(this, this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityShopInfoBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);

        mStartTimePicker = new ShopTimeBottomSheet(this);
        mStartTimePicker.setCallback(date -> mVM.startTime.set(date));

        mEndTimePicker = new ShopTimeBottomSheet(this);
        mEndTimePicker.setCallback(date -> mVM.endTime.set(date));

        mNextStartTimePicker = new ShopTimeBottomSheet(this);
        mNextStartTimePicker.setCallback(date -> mVM.nextStartTime.set(date));

        mNextEndTimePicker = new ShopTimeBottomSheet(this);
        mNextEndTimePicker.setCallback(date -> mVM.nextEndTime.set(date));

        mVM.logoUrl.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (TextUtils.isEmpty(mVM.logoUrl.get())) {
                    mBinding.ivLogo.setImageResource(R.drawable.shop_info_add_logo);
                }
            }
        });
    }

    @Override
    protected void onLoadData() {
        mBinding.progress.showLoading();
        mVM.start();
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void changeBusinessState(ChangeBusinessStatusEvent event) {
        mVM.status.set(event.status);
        mVM.statusName.set(event.statusName);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onUpdateSuccess() {
        EventBus.getDefault().post(new UpdateShopInfoSuccessEvent());
        onBackPressed();
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    public void toChangeBusinessState() {
        Bundle bundle = new Bundle();
        bundle.putInt("status", mVM.status.get());
        startAct(ChangeBusinessStateActivity.class, bundle);
    }

    public void showStartTimePicker() {
        mStartTimePicker.show(mVM.startTime.get());
    }

    public void showEndTimePicker() {
        mEndTimePicker.show(mVM.endTime.get());
    }

    public void showNextStartTimePicker() {
        mNextStartTimePicker.show(mVM.nextStartTime.get());
    }

    public void showNextEndTimePicker() {
        mNextEndTimePicker.show(mVM.nextEndTime.get());
    }

    public void selectImage() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .maxSelectable(1)
                .thumbnailScale(0.85f) // 缩略图的比例
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .imageEngine(new GlideV4Engine())
                .theme(R.style.MatisseTheme)
                .forResult(REQUEST_CHOOSE_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE_CODE && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", uris.get(0));
            startActForResult(CropperActivity.class, bundle);
        } else if (requestCode == (getClass().hashCode() & 0xffff) && resultCode == RESULT_OK && data != null) {
            mVM.uploadImage(data.getParcelableExtra("cropUri"));
        }
    }
}
