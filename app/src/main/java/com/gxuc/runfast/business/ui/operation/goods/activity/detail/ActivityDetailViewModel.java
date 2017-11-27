package com.gxuc.runfast.business.ui.operation.goods.activity.detail;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.gxuc.runfast.business.data.bean.Activity;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 活动
 * Created by Berial on 2017/9/1.
 */
public class ActivityDetailViewModel extends BaseViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> endTime = new ObservableField<>();
    public ObservableField<String> full = new ObservableField<>();
    public ObservableField<String> less = new ObservableField<>();
    public ObservableField<String> discount = new ObservableField<>();
    public ObservableField<String> disprice = new ObservableField<>();
    public ObservableField<String> goodsIds = new ObservableField<>();
    public ObservableField<String> standardIds = new ObservableField<>();
    public ObservableField<String> freebie = new ObservableField<>();
    public ObservableField<String> typeName = new ObservableField<>();
    public ObservableInt type = new ObservableInt(0);
    public ObservableField<String> statusName = new ObservableField<>();
    public ObservableInt status = new ObservableInt();
    public ObservableField<String> menuName = new ObservableField<>();

    private long mActivityId;

    ObservableField<Activity> activityObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private ProgressHelper.Callback mCallback;
    private ActivityDetailNavigator mNavigator;

    ActivityDetailViewModel(Context context, ProgressHelper.Callback callback, ActivityDetailNavigator navigator) {
        super(context);
        mCallback = callback;
        mNavigator = navigator;
        register();
    }

    private void register() {
        activityObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Activity activity = activityObservable.get();
                if (activity != null) {
                    mActivityId = activity.id;
                    name.set(activity.name);
                    startTime.set(activity.startTime);
                    endTime.set(activity.endTime);
                    full.set(activity.full);
                    less.set(activity.less);
                    discount.set(activity.discount);
                    disprice.set(activity.disprice);
                    freebie.set(activity.freebie);
                    goodsIds.set(activity.goodsIds);
                    standardIds.set(activity.standardIds);
                    type.set(activity.type);
                    typeName.set(activity.typeName);
                    statusName.set(activity.status == 1 ? "暂停活动" : activity.status == 2 ? "恢复活动" : "活动已结束");
                    status.set(activity.status);
                    menuName.set(activity.typeName.substring(0, 2));
                }
            }
        });
    }

    public void viewGoods() {
        mNavigator.viewGoodsList(type.get(), goodsIds.get(), standardIds.get());
    }

    public void changeStatus() {
        mCallback.setLoading(true);
        mRepo.changeActivityStatus(mActivityId)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success || "操作成功！".equals(response.msg)) {
                            status.set(status.get() % 2 + 1);
                            statusName.set("恢复活动".equals(statusName.get()) ? "暂停活动" : "恢复活动");
                            mNavigator.changeActivityStatusSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }
}
