package com.gxuc.runfast.business.ui.operation.statistics.goods;

import android.content.Context;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.FooterGoodsSellBindingModel_;
import com.gxuc.runfast.business.HeaderGoodsSellBindingModel_;
import com.gxuc.runfast.business.ItemGoodsSellBindingModel_;
import com.gxuc.runfast.business.data.bean.GoodsSell;
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
 * 商品销售统计
 * Created by Berial on 2017/8/26.
 */
public class GoodsSellViewModel extends BaseViewModel {

    public final ObservableField<String> amount = new ObservableField<>("0.00");
    public final ObservableField<String> count = new ObservableField<>("0");
    public final ObservableField<String> price = new ObservableField<>("0.00");

    public final Adapter adapter = new Adapter();

    private double totalAmount = 0;
    private int totalCount = 0;
    private double avgPrice = 0;

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    private int page;
    private boolean isFirstLoad = true;

    private final HeaderGoodsSellBindingModel_ header = new HeaderGoodsSellBindingModel_().id("header");
    private final FooterGoodsSellBindingModel_ footer = new FooterGoodsSellBindingModel_().id("footer").viewModel(this);

    public final SearchViewModel search;

    GoodsSellViewModel(Context context, LoadingCallback callback, final Searching searching) {
        super(context);
        mCallback = callback;
        search = new SearchViewModel(context) {
            @Override
            public void search() {
                isFirstLoad = true;
                searching.onSearching();
                mRepo.resetGoodsSellPages();
                loadGoodsSellList(page = 1);
            }
        };
    }

    void start() {
        String currentDate = Utils.getCurrentDate();
        search.startTime.set(currentDate + "\n00:00");
        search.endTime.set(currentDate + "\n23:59");
        mRepo.resetGoodsSellPages();
        loadGoodsSellList(page = 1);
    }

    void loadMore() {
        int maxPage = mRepo.getGoodsSellPages();
        if (page < maxPage) {
            loadGoodsSellList(++page);
        }
    }

    private void loadGoodsSellList(final int page) {
        if (page == 1) {
            adapter.removeModel(header);
            totalAmount = 0;
            avgPrice = 0;
            totalCount = 0;
        }
        adapter.removeModel(footer);
        mRepo.loadGoodsSellList(page,
                search.startTime.get().replace(".", "-").replace("\n", " "),
                search.endTime.get().replace(".", "-").replace("\n", " "))
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
                    count.set(String.valueOf(totalCount));
                    price.set(Utils.formatFloorNumber(itemCount == 1 ? 0 : avgPrice / (itemCount - 1), 2));

                    adapter.addModel(footer);
                })
                .subscribe(new ResponseSubscriber<List<GoodsSell>>(mContext) {
                    @Override
                    public void onNext(List<GoodsSell> list) {
                        if (page == 1 && !adapter.isEmpty()) {
                            adapter.swap(generateGoodsSellModels(list));
                        } else {
                            adapter.addMore(generateGoodsSellModels(list));
                        }
                        if (page == 1) {
                            adapter.addHeader(header);
                        }
                    }
                });
    }

    private List<ItemGoodsSellBindingModel_> generateGoodsSellModels(List<GoodsSell> list) {
        ArrayList<ItemGoodsSellBindingModel_> models = new ArrayList<>();
        for (GoodsSell sell : list) {
            totalAmount += sell.amountValue;
            totalCount += sell.countValue;
            avgPrice += sell.priceValue;
            models.add(new ItemGoodsSellBindingModel_()
                    .name(sell.name)
                    .amount(sell.amount)
                    .count(sell.count)
                    .price(sell.price));
        }
        return models;
    }

    private boolean isLastPage(int page) {
        return page == mRepo.getGoodsSellPages();
    }

    @Override
    public void onDestroy() {
        search.onDestroy();
        super.onDestroy();
    }
}
