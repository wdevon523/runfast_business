package com.gxuc.runfast.business.ui.operation.cash.record;

import android.content.Context;

import com.gxuc.runfast.business.ItemCashRecordBindingModel_;
import com.gxuc.runfast.business.ItemIncomeRecordBindingModel_;
import com.gxuc.runfast.business.data.bean.CashRecord;
import com.gxuc.runfast.business.data.bean.IncomeRecord;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录
 * Created by Berial on 2017/8/26.
 */
class RecordViewModel extends BaseViewModel {

    final Adapter mAdapter = new Adapter();

    private int mRecordType;
    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    private boolean isFirstLoad = true;
    private int page;

    RecordViewModel(Context context, int recordType, LoadingCallback callback) {
        super(context);
        mRecordType = recordType;
        mCallback = callback;
    }

    void start() {
        if (mRecordType == 0) {
            mRepo.resetIncomeRecordPages();
            loadIncomeRecords(page = 1);
        } else {
            mRepo.resetCashRecordPages();
            loadCashRecords(page = 1);
        }
    }

    void loadMoreRecords() {
        if (mRecordType == 0) {
            loadMoreIncomeRecords();
        } else {
            loadMoreCashRecords();
        }
    }

    private void loadMoreIncomeRecords() {
        int maxPage = mRepo.getIncomeRecordPages();
        if (page < maxPage) {
            loadIncomeRecords(++page);
        }
    }

    private void loadMoreCashRecords() {
        int maxPage = mRepo.getCashRecordPages();
        if (page < maxPage) {
            loadCashRecords(++page);
        }
    }

    private void loadIncomeRecords(final int page) {
        mRepo.loadIncomeRecords(page)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (page == 1) mCallback.onRefreshFinish();
                    if (isFirstLoad) {
                        isFirstLoad = false;
                        if (mAdapter.isEmpty()) {
                            mCallback.onLoadEmpty("暂时没有记录!");
                        } else {
                            mCallback.onFirstLoadFinish();
                        }
                    }
                    mCallback.onLoadMoreFinish(isLastPage(page));
                })
                .subscribe(new ResponseSubscriber<List<IncomeRecord>>(mContext) {
                    @Override
                    public void onSuccess(List<IncomeRecord> records) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateIncomeRecordModels(records));
                        } else {
                            mAdapter.addMore(generateIncomeRecordModels(records));
                        }
                    }
                });
    }

    private List<ItemIncomeRecordBindingModel_> generateIncomeRecordModels(List<IncomeRecord> records) {
        ArrayList<ItemIncomeRecordBindingModel_> models = new ArrayList<>();
        for (IncomeRecord record : records) {
            models.add(new ItemIncomeRecordBindingModel_()
                    .date(record.date)
                    .amount(record.amount)
                    .orderId(record.orderId));
        }
        return models;
    }

    private void loadCashRecords(final int page) {
        mRepo.loadCashRecords(page)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (page == 1) mCallback.onRefreshFinish();
                    if (isFirstLoad) {
                        mCallback.onFirstLoadFinish();
                        isFirstLoad = false;
                    }
                    mCallback.onLoadMoreFinish(isLastPage(page));
                })
                .subscribe(new ResponseSubscriber<List<CashRecord>>(mContext) {
                    @Override
                    public void onSuccess(List<CashRecord> records) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateCashRecordModels(records));
                        } else {
                            mAdapter.addMore(generateCashRecordModels(records));
                        }
                    }
                });
    }

    private List<ItemCashRecordBindingModel_> generateCashRecordModels(List<CashRecord> records) {
        ArrayList<ItemCashRecordBindingModel_> models = new ArrayList<>();
        for (CashRecord record : records) {
            models.add(new ItemCashRecordBindingModel_()
                    .date(record.date)
                    .amount(record.amount)
                    .state(record.state)
                    .stateName(record.stateName)
                    .cashDate(record.cashDate));
        }
        return models;
    }

    private boolean isLastPage(int currentPage) {
        if (mRecordType == 0) {
            return currentPage == mRepo.getIncomeRecordPages();
        } else {
            return currentPage == mRepo.getCashRecordPages();
        }
    }
}
