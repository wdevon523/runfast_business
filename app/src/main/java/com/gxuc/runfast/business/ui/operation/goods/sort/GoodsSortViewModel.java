package com.gxuc.runfast.business.ui.operation.goods.sort;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.gxuc.runfast.business.ItemGoodsSortBindingModel_;
import com.gxuc.runfast.business.data.bean.GoodsSort;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类
 * Created by Berial on 2017/8/30.
 */
public class GoodsSortViewModel extends BaseViewModel {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> describe = new ObservableField<>();
    public final ObservableBoolean isEditable = new ObservableBoolean();

    public final Adapter mAdapter = new Adapter();

    private final ObservableField<GoodsSort> mSortObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private ProgressHelper.Callback mCallback;
    private GoodsSortNavigator mNavigator;
    private LoadingCallback mLoadingCallback;

    GoodsSortViewModel(Context context, GoodsSort sort, GoodsSortNavigator navigator, ProgressHelper.Callback callback, LoadingCallback loadingCallback) {
        super(context);
        if (sort != null) {
            register();
            mSortObservable.set(sort);
        }
        mSortObservable.set(sort);
        mCallback = callback;
        mLoadingCallback = loadingCallback;
        mNavigator = navigator;
    }

    private void register() {
        mSortObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                GoodsSort sort = mSortObservable.get();
                name.set(sort.name);
                describe.set(sort.describe);
                isEditable.set(true);
            }
        });
    }

    void start() {
        mRepo.loadGoods("")
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (mAdapter.isEmpty()) {
                        mLoadingCallback.onLoadEmpty("暂时没有商品分类!");
                    } else {
                        mLoadingCallback.onFirstLoadFinish();
                    }
                    mLoadingCallback.onRefreshFinish();
                })
                .subscribe(new ResponseSubscriber<List<GoodsSort>>(mContext) {
                    @Override
                    public void onNext(List<GoodsSort> sorts) {
                        mAdapter.swap(generateSortModels(sorts));
                    }
                });
    }

    public void edit(GoodsSort sort) {
        mNavigator.toEdit(sort);
    }

    public void toAddGoods(GoodsSort sort) {
        mNavigator.toAddGoods(sort);
    }

    public void saveOrUpdate() {
        if (mSortObservable.get() == null) addGoodsSort();
        else updateGoodsSort();
    }

    private void addGoodsSort() {
        String name = this.name.get();
        if (TextUtils.isEmpty(name)) {
            toast("请输入分类名称");
            return;
        }
        mCallback.setLoading(true);

        mRepo.addGoodsSort(name, describe.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }

    private void updateGoodsSort() {
        String name = this.name.get();
        if (TextUtils.isEmpty(name)) {
            toast("请输入分类名称");
            return;
        }
        mCallback.setLoading(true);

        mRepo.updateGoodsSort(mSortObservable.get().id, name, describe.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }

    void deleteGoodsSort() {
        mCallback.setLoading(true);
        mRepo.deleteGoodsSort(mSortObservable.get().id)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }

    private ArrayList<ItemGoodsSortBindingModel_> generateSortModels(List<GoodsSort> sorts) {
        ArrayList<ItemGoodsSortBindingModel_> models = new ArrayList<>();
        for (GoodsSort sort : sorts) {
            models.add(new ItemGoodsSortBindingModel_()
                    .sort(sort)
                    .viewModel(this));
        }
        return models;
    }
}
