package com.gxuc.runfast.business.ui.operation.statistics;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.data.bean.BusinessStatistics;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 营业统计
 * Created by Berial on 2017/8/25.
 */
public class BusinessStatisticsViewModel extends BaseViewModel {

    public final ObservableField<String> todaySale = new ObservableField<>("0");
    public final ObservableField<String> todayIncome = new ObservableField<>("0");
    public final ObservableField<String> monthIncome = new ObservableField<>("0");
    public final ObservableField<String> totalIncome = new ObservableField<>("0");

    private ObservableField<BusinessStatistics> mStatisticsObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    BusinessStatisticsViewModel(Context context, LoadingCallback callback) {
        super(context);
        mCallback = callback;
    }

    void start() {
        mStatisticsObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                BusinessStatistics statistics = mStatisticsObservable.get();
                if (statistics != null) {
                    todaySale.set(statistics.todaySale);
                    todayIncome.set(statistics.todayIncome);
                    monthIncome.set(statistics.monthIncome);
                    totalIncome.set(statistics.totalIncome);
                }
            }
        });
        mRepo.loadBusinessStatistics()
                .compose(RxLifecycle.<BusinessStatistics>bindLifecycle(this))
                .doFinally(() -> mCallback.onFirstLoadFinish())
                .subscribe(new ResponseSubscriber<BusinessStatistics>(mContext) {
                    @Override
                    public void onSuccess(BusinessStatistics statistics) {
                        mStatisticsObservable.set(statistics);
                    }
                });
    }
}
