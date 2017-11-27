package com.gxuc.runfast.business.ui.mine.shop;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;

import com.gxuc.runfast.business.data.bean.Business;
import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.net.URI;

import io.reactivex.Observable;

/**
 * 店铺信息
 * Created by Berial on 2017/9/5.
 */
public class ShopInfoViewModel extends BaseViewModel {

    public final ObservableField<String> logoUrl = new ObservableField<>();
    public final ObservableInt status = new ObservableInt(0);
    public final ObservableField<String> statusName = new ObservableField<>("营业");

    public final ObservableField<String> startTime = new ObservableField<>();
    public final ObservableField<String> endTime = new ObservableField<>();
    public final ObservableField<String> nextStartTime = new ObservableField<>();
    public final ObservableField<String> nextEndTime = new ObservableField<>();

    public final ObservableBoolean isDeliver = new ObservableBoolean(false);
    public final ObservableField<String> packingCharge = new ObservableField<>();
    public final ObservableField<String> deliveryCost = new ObservableField<>();
    public final ObservableField<String> subsidy = new ObservableField<>();
    public final ObservableField<String> describe = new ObservableField<>();
    public final ObservableBoolean hasSubsidy = new ObservableBoolean(false);
    public final ObservableBoolean isAutomatic = new ObservableBoolean(false);

    private final ObservableField<Business> mBusinessObservable = new ObservableField<>();

    private BusinessRepo mRepo = BusinessRepo.get();

    private ShopInfoNavigator mNavigator;
    private LoadingCallback mLoadingCallback;
    private ProgressHelper.Callback mCallback;

    ShopInfoViewModel(Context context, ShopInfoNavigator navigator, LoadingCallback loadingCallback, ProgressHelper.Callback callback) {
        super(context);
        mNavigator = navigator;
        mLoadingCallback = loadingCallback;
        mCallback = callback;
        isAutomatic.set(mRepo.isAutomatic());
    }

    void start() {
        mBusinessObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                Business business = mBusinessObservable.get();
                if (business != null) {
                    logoUrl.set(business.logoUrl);
                    status.set(business.status);
                    statusName.set(business.statusName);
                    packingCharge.set(business.packingCharge);
                    subsidy.set(business.subsidy);
                    hasSubsidy.set(business.hasSubsidy);
                    startTime.set(business.startTime);
                    endTime.set(business.endTime);
                    nextStartTime.set(business.nextStartTime);
                    nextEndTime.set(business.nextEndTime);
                    deliveryCost.set(business.deliveryCost);
                    describe.set(business.describe);
                    isDeliver.set(business.isDeliver);
                }
            }
        });
        mRepo.loadBusinessInfo()
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mLoadingCallback.onFirstLoadFinish())
                .subscribe(new ResponseSubscriber<Business>(mContext) {
                    @Override
                    public void onNext(Business business) {
                        mRepo.savePhone(business.phone);
                        mBusinessObservable.set(business);
                    }
                });
    }

    public void submit() {
        mCallback.setLoading(true);
        mRepo.updateShopInfo(status.get(), startTime.get(), endTime.get(), nextStartTime.get(), nextEndTime.get(),
                packingCharge.get(), deliveryCost.get(), describe.get(), hasSubsidy.get(), subsidy.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            mRepo.setAutomatic(isAutomatic.get());
                            mNavigator.onUpdateSuccess();
                        }
                        toast(response.msg);
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
                            return mRepo.updateShopLogoPath(response.filePath, response.thumbnailPath);
                        } else {
                            return Observable.just(new BaseResponse());
                        }
                    })
                    .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                        @Override
                        public void onNext(BaseResponse response) {
                            if (!response.success) {
                                toast(response.msg);
                            }
                        }
                    });
        } catch (Exception e) {
            toast("选择了无效的图片");
            Logger.e(e.toString());
        }
    }
}
