package com.gxuc.runfast.business.ui.operation.statistics.monthly;

import android.content.Context;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.FooterMonthlyBindingModel_;
import com.gxuc.runfast.business.HeaderMonthlyBindingModel_;
import com.gxuc.runfast.business.ItemMonthlyBindingModel_;
import com.gxuc.runfast.business.data.bean.Monthly;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.ui.operation.statistics.Searching;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.gxuc.runfast.business.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 月报
 * Created by Berial on 2017/9/5.
 */
public class MonthlyViewModel extends BaseViewModel {

    public final ObservableField<String> amount = new ObservableField<>("0.00");
    public final ObservableField<String> count = new ObservableField<>("0");
    public final ObservableField<String> price = new ObservableField<>("0.00");

    public final Adapter adapter = new Adapter();

    public final ObservableField<String> date = new ObservableField<>();

    private double totalAmount = 0;
    private double avgPrice = 0;
    private int totalCount = 0;

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    private int page;
    private boolean isFirstLoad = true;

    private final HeaderMonthlyBindingModel_ header = new HeaderMonthlyBindingModel_().id("header");
    private final FooterMonthlyBindingModel_ footer = new FooterMonthlyBindingModel_().id("footer").viewModel(this);

    private Searching mSearching;

    MonthlyViewModel(Context context, LoadingCallback callback, Searching searching) {
        super(context);
        mCallback = callback;
        mSearching = searching;
    }

    void init() {
        String currentDate = Utils.getCurrentDate();
        date.set(currentDate.substring(0, 7));
    }

    void start() {
        String currentDate = Utils.getCurrentDate();
        date.set(currentDate.substring(0, 7));
        mRepo.resetMonthlyPages();
        loadMonthlies(page = 1);
    }

    public void search() {
        isFirstLoad = true;
        mSearching.onSearching();
        mRepo.resetMonthlyPages();
        loadMonthlies(page = 1);
    }

    void loadMoreMonthlies() {
        int maxPage = mRepo.getMonthliesPages();
        if (page < maxPage) {
            loadMonthlies(++page);
        }
    }

    private void loadMonthlies(final int page) {
        if (page == 1) {
            adapter.removeModel(header);
            totalAmount = 0;
            avgPrice = 0;
            totalCount = 0;
        }
        adapter.removeModel(footer);
        mRepo.loadMonthlies(page,
                date.get().replace(".", "-").replace("\n", " "))
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (page == 1) mCallback.onRefreshFinish();
                    if (isFirstLoad) {
                        mCallback.onFirstLoadFinish();
                        isFirstLoad = false;
                    }
                    mCallback.onLoadMoreFinish(isLastPage(page));

                    int itemCount = adapter.getItemCount();

                    amount.set(Utils.formatFloorNumber(totalAmount, 2));
//                    price.set(Utils.formatFloorNumber(itemCount == 1 ? 0 : avgPrice / (itemCount - 1), 2));
                    price.set(Utils.formatFloorNumber(itemCount == 1 ? 0 : avgPrice, 2));
                    count.set(String.valueOf(totalCount));

                    adapter.addModel(footer);
                })
                .subscribe(new ResponseSubscriber<List<Monthly>>(mContext) {
                    @Override
                    public void onNext(List<Monthly> monthlies) {
                        if (page == 1 && !adapter.isEmpty()) {
                            adapter.swap(generateMonthlyModels(monthlies));
                        } else {
                            adapter.addMore(generateMonthlyModels(monthlies));
                        }
                        if (page == 1) {
                            adapter.addHeader(header);
                        }
                    }
                });
    }

    private List<ItemMonthlyBindingModel_> generateMonthlyModels(List<Monthly> monthlies) {
        ArrayList<ItemMonthlyBindingModel_> models = new ArrayList<>();
        for (Monthly monthly : monthlies) {
            totalAmount += monthly.amountValue;
            totalCount += monthly.countValue;
//            avgPrice += monthly.priceValue;
            avgPrice = totalAmount / totalCount;
            models.add(new ItemMonthlyBindingModel_()
                    .name(monthly.name)
                    .amount(monthly.amount)
                    .count(monthly.count)
                    .price(monthly.price));
        }
        return models;
    }

    private boolean isLastPage(int page) {
        return page == mRepo.getMonthliesPages();
    }
}
