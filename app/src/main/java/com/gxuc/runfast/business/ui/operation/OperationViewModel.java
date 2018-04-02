package com.gxuc.runfast.business.ui.operation;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.data.bean.Operation;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 门店运营
 * Created by Berial on 2017/8/25.
 */
public class OperationViewModel extends BaseViewModel {

    public final ObservableField<String> todayOrderCount = new ObservableField<>("--");
    public final ObservableField<String> todayIncome = new ObservableField<>("--");

    private final ObservableField<Operation> mOperationObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    OperationViewModel(Context context) {
        super(context);
        mOperationObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Operation operation = mOperationObservable.get();
                todayOrderCount.set(operation.todayOrderCount);
                todayIncome.set(operation.todayIncome);
            }
        });
    }

    void start() {
        mRepo.loadOperationInfo()
                .compose(RxLifecycle.bindLifecycle(this))
                .subscribe(new ResponseSubscriber<Operation>(mContext) {
                    @Override
                    public void onSuccess(Operation operation) {
                        mOperationObservable.set(operation);
                    }
                });
    }
}
