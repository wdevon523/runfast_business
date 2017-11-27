package com.gxuc.runfast.business.ui.mine.archive;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gxuc.runfast.business.FooterArchiveBindingModel_;
import com.gxuc.runfast.business.ItemArchiveBindingModel_;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.bean.Archive;
import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.gxuc.runfast.business.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * 档案
 * Created by Berial on 2017/9/6.
 */
public class ArchiveViewModel extends BaseViewModel {

    public Adapter adapter = new Adapter();

    private BusinessRepo mRepo = BusinessRepo.get();

    private ArchiveNavigator mNavigator;
    private LoadingCallback mLoadingCallback;
    private ProgressHelper.Callback mCallback;

    private FooterArchiveBindingModel_ footer;

    private float mViewSize;

    private WeakReference<Context> mActContext;

    ArchiveViewModel(Context context, ArchiveNavigator navigator, LoadingCallback loadingCallback, ProgressHelper.Callback callback) {
        super(context);
        computeViewSize();
        mNavigator = navigator;
        mLoadingCallback = loadingCallback;
        mCallback = callback;
        mActContext = new WeakReference<>(context);

        footer = new FooterArchiveBindingModel_()
                .id("footer")
                .size(mViewSize)
                .viewModel(this);
    }

    void start() {
        mRepo.loadArchives()
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mLoadingCallback.onFirstLoadFinish())
                .subscribe(new ResponseSubscriber<List<Archive>>(mContext) {
                    @Override
                    public void onNext(List<Archive> archives) {
                        if (!adapter.isEmpty()) adapter.clear();
                        adapter.addMore(generateArchiveModels(archives));
                        adapter.addFooter(footer);
                    }
                });
    }

    public void selectImage() {
        mNavigator.toSelectImage();
    }

    public void showDeleteArchiveDialog(long id) {
        if (mActContext.get() == null) return;
        AlertDialog dialog = new AlertDialog.Builder(mActContext.get())
                .setTitle("提示")
                .setMessage("确定要删除当前图片？")
                .setCancelable(false)
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    deleteArchive(id);
                    dialogInterface.dismiss();
                }).show();

        dialog.setCanceledOnTouchOutside(false);
        Button negativeButton = dialog.getButton(BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(mContext, R.color.gray19));
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
        Button positiveButton = dialog.getButton(BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
    }

    private void deleteArchive(long id) {
        mCallback.setLoading(true);
        mRepo.deleteArchive(id)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success || "删除成功！".equals(response.msg)) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }

    void uploadImage(Uri uri) {
        mCallback.setLoading(true);
        try {
            String imagePath = new File(new URI(uri.toString())).getAbsolutePath();
            mRepo.uploadImage(imagePath)
                    .compose(RxLifecycle.bindLifecycle(this))
                    .doFinally(() -> mCallback.setLoading(false))
                    .flatMap(response -> {
                        if (response.success) {
                            return mRepo.updateArchivePath(response.filePath);
                        } else {
                            return Observable.just(new BaseResponse());
                        }
                    })
                    .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                        @Override
                        public void onNext(BaseResponse response) {
                            if (response.success || "添加成功！".equals(response.msg)) {
                                start();
                            } else {
                                toast(response.msg);
                            }
                        }
                    });
        } catch (Exception e) {
            toast("选择了无效的图片");
            Logger.e(e.toString());
        }
    }

    private List<ItemArchiveBindingModel_> generateArchiveModels(List<Archive> archives) {
        ArrayList<ItemArchiveBindingModel_> models = new ArrayList<>(archives.size());
        for (Archive archive : archives) {
            models.add(new ItemArchiveBindingModel_()
                    .id(archive.id)
                    .size(mViewSize)
                    .imageUrl(archive.imageUrl)
                    .viewModel(this));
        }
        return models;
    }

    private void computeViewSize() {
        float screenWidth = Utils.getScreenWidth(mContext);
        float padding = Utils.dp2px(mContext, 10);
        mViewSize = (screenWidth - padding * 2) / 4;
    }

    @BindingAdapter("size")
    public static void setSize(FrameLayout layout, float size) {
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        if (params instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) params;
            lp.width = (int) size;
            lp.height = (int) size;
            layout.setLayoutParams(lp);
        }
    }
}
