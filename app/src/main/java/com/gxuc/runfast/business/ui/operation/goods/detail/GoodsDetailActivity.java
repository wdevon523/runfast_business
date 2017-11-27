package com.gxuc.runfast.business.ui.operation.goods.detail;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityGoodsDetailBinding;
import com.gxuc.runfast.business.event.UpdateGoodsSuccessEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.glide.GlideV4Engine;
import com.gxuc.runfast.business.extension.recyclerview.SpaceDecoration;
import com.gxuc.runfast.business.ui.CropperActivity;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.base.DateBottomSheet;
import com.gxuc.runfast.business.ui.operation.goods.detail.sort.SelectGoodsSortActivity;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.Utils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 添加商品
 * Created by Berial on 2017/8/21.
 */
public class GoodsDetailActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, GoodsDetailNavigator, LoadingCallback,
        ProgressHelper.Callback, NeedDataBinding<ActivityGoodsDetailBinding> {

    private static final int REQUEST_GOODS_SORT = 1;
    private static final int REQUEST_CHOOSE_IMAGE_CODE = 2;

    public LinearLayoutManager standardManager = new LinearLayoutManager(this);
    public LinearLayoutManager optionManager = new LinearLayoutManager(this);

    private ActivityGoodsDetailBinding mBinding;

    private GoodsDetailViewModel mVM;

    private ProgressHelper helper;

    private DateBottomSheet mStartTimePicker;
    private DateBottomSheet mEndTimePicker;

    @Override
    public String thisTitle() {
        return getIntent().getLongExtra("goodsId", 0L) == 0 ? "添加商品" : "修改商品";
    }

    @Override
    public int thisLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GoodsDetailViewModel thisViewModel() {
        Intent intent = getIntent();
        return new GoodsDetailViewModel(this, intent.getLongExtra("goodsId", 0L), this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityGoodsDetailBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);

        mStartTimePicker = new DateBottomSheet(this);
        mStartTimePicker.setCallback(date -> mVM.startTime.set(date));

        mEndTimePicker = new DateBottomSheet(this);
        mEndTimePicker.setCallback(date -> mVM.endTime.set(date));

        int spacing = (int) Utils.dp2px(this, 6);
        SpaceDecoration decoration = new SpaceDecoration(spacing);
        decoration.setPaddingStart(false);
        decoration.setPaddingEdgeSide(false);
        mBinding.rvStandard.addItemDecoration(decoration);
        mBinding.rvOption.addItemDecoration(decoration);
    }

    @Override
    protected void onLoadData() {
        Intent intent = getIntent();
        if (intent.getLongExtra("goodsId", 0L) != 0) {
            mBinding.progress.showLoading();
            mVM.start();
        } else {
            mVM.addStandard();
            mVM.addOption();
        }

        if (intent.getLongExtra("sortId", 0L) != 0) {
            mVM.sortId.set(intent.getLongExtra("sortId", 0L));
            mVM.sortName.set(intent.getStringExtra("sortName"));
        }
    }

    @Override
    public void onFirstLoadFinish() {
        mBinding.progress.showContent();
    }

    @Override
    public void onSuccess() {
        EventBus.getDefault().post(new UpdateGoodsSuccessEvent());
        onBackPressed();
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Override
    public void onRefreshFinish() {}

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {}

    @Override
    public void onLoadEmpty(String text) {}

    public void showStartTimePicker() {
        mStartTimePicker.show(mVM.startTime.get());
    }

    public void showEndTimePicker() {
        mEndTimePicker.show(mVM.endTime.get());
    }

    public void toSelectGoodsSort() {
        Bundle bundle = new Bundle();
        bundle.putLong("sortId", mVM.sortId.get());
        bundle.putString("sortName", mVM.sortName.get());
        startActForResult(SelectGoodsSortActivity.class, bundle, REQUEST_GOODS_SORT);
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
        if (requestCode == REQUEST_GOODS_SORT && resultCode == RESULT_OK && data != null) {
            mVM.sortId.set(data.getLongExtra("sortId", 0L));
            mVM.sortName.set(data.getStringExtra("sortName"));
        } else if (requestCode == REQUEST_CHOOSE_IMAGE_CODE && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", uris.get(0));
            startActForResult(CropperActivity.class, bundle);
        } else if (requestCode == (getClass().hashCode() & 0xffff) && resultCode == RESULT_OK && data != null) {
            mVM.uploadImage(data.getParcelableExtra("cropUri"));
        }
    }
}
