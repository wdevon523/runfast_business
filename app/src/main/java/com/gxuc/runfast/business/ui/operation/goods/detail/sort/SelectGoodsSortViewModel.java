package com.gxuc.runfast.business.ui.operation.goods.detail.sort;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;

import com.airbnb.epoxy.EpoxyModel;
import com.gxuc.runfast.business.ItemSelectGoodsSortBindingModel_;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择商品分类
 * Created by Berial on 2017/9/13.
 */
public class SelectGoodsSortViewModel extends BaseViewModel {

    Adapter adapter = new Adapter();

    ObservableLong sortId = new ObservableLong(0);
    ObservableField<String> sortName = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private LoadingCallback mCallback;

    SelectGoodsSortViewModel(Context context, LoadingCallback callback) {
        super(context);
        mCallback = callback;
    }

    void start() {
        mRepo.loadGoodsSorts()
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (adapter.isEmpty()) {
                        mCallback.onLoadEmpty("暂时没有商品分类!");
                    } else {
                        mCallback.onFirstLoadFinish();
                    }
                    mCallback.onRefreshFinish();
                })
                .subscribe(new ResponseSubscriber<List<GoodsSort>>(mContext) {
                    @Override
                    public void onSuccess(List<GoodsSort> sorts) {
                        adapter.swap(generateSortModels(sorts));
                    }
                });
    }

    public void selectSort(long id) {
        List<EpoxyModel<?>> models = adapter.getModels();
        for (EpoxyModel<?> model : models) {
            ItemSelectGoodsSortBindingModel_ m = (ItemSelectGoodsSortBindingModel_) model;
            if (m.id() == sortId.get()) m.selected(false);
            if (m.id() == id) {
                m.selected(true);
                sortName.set(m.name());
            }
        }
        sortId.set(id);
        adapter.notifyDataSetChanged();
    }

    private List<ItemSelectGoodsSortBindingModel_> generateSortModels(List<GoodsSort> sorts) {
        ArrayList<ItemSelectGoodsSortBindingModel_> models = new ArrayList<>(sorts.size());
        for (GoodsSort sort : sorts) {
            models.add(new ItemSelectGoodsSortBindingModel_()
                    .id(sort.id)
                    .name(sort.name)
                    .selected(sort.id == sortId.get())
                    .viewModel(this));
        }
        return models;
    }
}
