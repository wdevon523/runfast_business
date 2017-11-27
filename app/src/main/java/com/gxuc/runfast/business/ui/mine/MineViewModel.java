package com.gxuc.runfast.business.ui.mine;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.gxuc.runfast.business.data.bean.Business;
import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.repo.LoginRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

public class MineViewModel extends BaseViewModel {

    public final ObservableField<String> logoUrl = new ObservableField<>("http://120.77.70.27/upload/1493110157474.jpg");
    public final ObservableField<String> name = new ObservableField<>("--");
    public final ObservableField<String> workTime = new ObservableField<>("未设置");
    public final ObservableField<String> upPrice = new ObservableField<>("0");
    public final ObservableField<String> range = new ObservableField<>("0km");
    public final ObservableInt status = new ObservableInt(0);
    public final ObservableField<String> statusName = new ObservableField<>("--");
    public final ObservableField<String> account = new ObservableField<>("--");
    public final ObservableField<String> accountName = new ObservableField<>("--");
    public final ObservableField<String> activities = new ObservableField<>();

    private BusinessRepo mRepo = BusinessRepo.get();
    private LoginRepo mLoginRepo = LoginRepo.get();

    private final ObservableField<Business> mBusinessObservable = new ObservableField<>();

    private MineNavigator mNavigator;

    MineViewModel(Context context, MineNavigator navigator) {
        super(context);
        mNavigator = navigator;
    }

    void start() {
        mBusinessObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Business business = mBusinessObservable.get();
                if (business != null) {
                    logoUrl.set(business.logoUrl);
                    name.set(business.name);
                    range.set(business.range);
                    upPrice.set(business.startAmount);
                    workTime.set(business.workTime);
                    status.set(business.status);
                    statusName.set(business.statusName);
                    account.set(business.account);
                    accountName.set(business.accountName);
                    activities.set(business.activities);
                }
            }
        });
        mRepo.loadBusinessInfo()
                .compose(RxLifecycle.bindLifecycle(this))
                .subscribe(new ResponseSubscriber<Business>(mContext) {
                    @Override
                    public void onNext(Business business) {
                        mRepo.savePhone(business.phone);
                        mBusinessObservable.set(business);
                    }
                });
    }

    public void logout() {
        mLoginRepo.logout();
        mNavigator.onLogoutSuccess();
    }

    void changeShopName(String name) {
        mRepo.changeShopName(name)
                .compose(RxLifecycle.bindLifecycle(this))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }
}
