package com.gxuc.runfast.business.ui.operation.cash;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.data.bean.CashInfo;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 提现申请
 * Created by Berial on 2017/8/26.
 */
public class CashViewModel extends BaseViewModel {

    public final ObservableField<String> date = new ObservableField<>("当前金额统计日期为：未统计");
    public final ObservableField<String> amount = new ObservableField<>("0.00");

    private final ObservableField<CashInfo> mCashInfoObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mLoadingCallback;
    private ProgressHelper.Callback mCallback;

    CashViewModel(Context context, LoadingCallback loadingCallback, ProgressHelper.Callback callback) {
        super(context);
        mLoadingCallback = loadingCallback;
        mCallback = callback;
    }

    void start() {
        mCashInfoObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                CashInfo info = mCashInfoObservable.get();
                if (info != null) {
                    date.set(info.date);
                    amount.set(info.amount);
                }
            }
        });
        mRepo.loadCashInfo()
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mLoadingCallback.onFirstLoadFinish())
                .subscribe(new ResponseSubscriber<CashInfo>(mContext) {
                    @Override
                    public void onSuccess(CashInfo cashInfo) {
                        mCashInfoObservable.set(cashInfo);
                    }
                });
    }

    public void applyForCash() {
        mCallback.setLoading(true);
        mRepo.applyForCash()
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.success || "申请成功！".equals(response.msg)) {
                            toast("提现申请成功");
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }
}
