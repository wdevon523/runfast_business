package com.gxuc.runfast.business.ui.operation.statistics.order;

import android.content.Context;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.FooterDayOrderBindingModel_;
import com.gxuc.runfast.business.HeaderDayOrderBindingModel_;
import com.gxuc.runfast.business.ItemDayOrderBindingModel_;
import com.gxuc.runfast.business.data.bean.DayOrder;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.ui.operation.statistics.SearchViewModel;
import com.gxuc.runfast.business.ui.operation.statistics.Searching;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.gxuc.runfast.business.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 当日订单清单
 * Created by Berial on 2017/8/26.
 */
public class DayOrderViewModel extends BaseViewModel {

    public final ObservableField<String> amount = new ObservableField<>("0.00");
    public final ObservableField<String> subsidy = new ObservableField<>("0.00");
    public final Adapter adapter = new Adapter();

    private double totalAmount = 0;
    private double totalSubsidy = 0;

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    private int page;
    private boolean isFirstLoad = true;

    private final HeaderDayOrderBindingModel_ header = new HeaderDayOrderBindingModel_().id("header");
    private final FooterDayOrderBindingModel_ footer = new FooterDayOrderBindingModel_().id("footer").viewModel(this);

    public final SearchViewModel search;

    DayOrderViewModel(Context context, LoadingCallback callback, final Searching searching) {
        super(context);
        mCallback = callback;
        search = new SearchViewModel(context) {
            @Override
            public void search() {
                isFirstLoad = true;
                searching.onSearching();
                mRepo.resetDayOrderPages();
                loadOrders(page = 1);
            }
        };
    }

    void start() {
        String currentDate = Utils.getCurrentDate();
        search.startTime.set(currentDate + "\n00:00");
        search.endTime.set(currentDate + "\n23:59");
        mRepo.resetDayOrderPages();
        loadOrders(page = 1);
    }

    void loadMoreOrders() {
        int maxPage = mRepo.getDayOrderPages();
        if (page < maxPage) {
            loadOrders(++page);
        }
    }

    private void loadOrders(final int page) {
        if (page == 1) {
            totalAmount = 0;
            totalSubsidy = 0;
            adapter.removeModel(header);
        }
        adapter.removeModel(footer);
        mRepo.loadDayOrders(page,
                search.startTime.get().replace(".", "-").replace("\n", " "),
                search.endTime.get().replace(".", "-").replace("\n", " "))
                .compose(RxLifecycle.<List<DayOrder>>bindLifecycle(this))
                .doFinally(() -> {
                    if (page == 1) mCallback.onRefreshFinish();
                    if (isFirstLoad) {
                        mCallback.onFirstLoadFinish();
                        isFirstLoad = false;
                    }
                    mCallback.onLoadMoreFinish(isLastPage(page));

                    amount.set(Utils.formatFloorNumber(totalAmount, 2));
                    subsidy.set(Utils.formatFloorNumber(totalSubsidy, 2));

                    adapter.addModel(footer);
                })
                .subscribe(new ResponseSubscriber<List<DayOrder>>(mContext) {
                    @Override
                    public void onNext(List<DayOrder> orders) {
                        if (page == 1 && !adapter.isEmpty()) {
                            adapter.swap(generateDayOrderModels(orders));
                        } else {
                            adapter.addMore(generateDayOrderModels(orders));
                        }
                        if (page == 1) {
                            adapter.addHeader(header);
                        }
                    }
                });
    }

    private List<ItemDayOrderBindingModel_> generateDayOrderModels(List<DayOrder> orders) {
        ArrayList<ItemDayOrderBindingModel_> models = new ArrayList<>();
        for (DayOrder order : orders) {
            totalAmount += order.amountValue;
            totalSubsidy += order.subsidyValue;
            models.add(new ItemDayOrderBindingModel_()
                    .orderId(order.orderId)
                    .time(order.time)
                    .amount(order.amount)
                    .subsidy(order.subsidy));
        }
        return models;
    }

    private boolean isLastPage(int page) {
        return page == mRepo.getDayOrderPages();
    }

    @Override
    public void onDestroy() {
        search.onDestroy();
        super.onDestroy();
    }
}
