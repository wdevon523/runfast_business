package com.gxuc.runfast.business.ui.mine.archive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityArchiveBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.extension.glide.GlideV4Engine;
import com.gxuc.runfast.business.ui.CropperActivity;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.List;

/**
 * 档案
 * Created by Berial on 2017/9/6.
 */
public class ArchiveActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, ArchiveNavigator,
        ProgressHelper.Callback, NeedDataBinding<ActivityArchiveBinding>, LoadingCallback {

    public GridLayoutManager manager = new GridLayoutManager(this, 4);

    private static final int REQUEST_CHOOSE_IMAGE_CODE = 1;

    private ArchiveViewModel mVM;

    private ActivityArchiveBinding mBinding;

    private ProgressHelper helper;

    @Override
    public String thisTitle() {
        return "食品安全档案";
    }

    @Override
    public int thisLayoutId() {
        return R.layout.activity_archive;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ArchiveViewModel thisViewModel() {
        return new ArchiveViewModel(this, this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityArchiveBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
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
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Override
    public void toSelectImage() {
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
