package com.gxuc.runfast.business.ui.operation.goods;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.airbnb.epoxy.EpoxyModel;
import com.gxuc.runfast.business.ItemGoodsBindingModel_;
import com.gxuc.runfast.business.ItemGoodsSortNameBindingModel_;
import com.gxuc.runfast.business.data.bean.Goods;
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
 * 商品管理
 * Created by Berial on 2017/8/28.
 */
public class GoodsViewModel extends BaseViewModel {

    public final Adapter sortAdapter = new Adapter();
    public final Adapter goodsAdapter = new Adapter();
    public final ObservableField<Integer> count = new ObservableField<>(0);


    private String status = "";

    private OperationRepo mRepo = OperationRepo.get();

    private GoodsSort selectedSort;

    private LoadingCallback mLoadingCallback;
    private GoodsNavigator mNavigator;
    private ProgressHelper.Callback mCallback;

    GoodsViewModel(Context context, LoadingCallback loadingCallback, GoodsNavigator navigator, ProgressHelper.Callback callback) {
        super(context);
        mLoadingCallback = loadingCallback;
        mNavigator = navigator;
        mCallback = callback;
    }

    void start(String status) {
        if (!this.status.equals(status)) {
            this.status = status;
        }
        start();
    }

    void start() {
        mRepo.loadGoods(status)
                .compose(RxLifecycle.<List<GoodsSort>>bindLifecycle(this))
                .doFinally(() -> {
                    mLoadingCallback.onFirstLoadFinish();
                    mLoadingCallback.onRefreshFinish();
                })
                .subscribe(new ResponseSubscriber<List<GoodsSort>>(mContext) {
                    @Override
                    public void onNext(List<GoodsSort> sorts) {
                        sortAdapter.swap(generateSortModels(sorts));
                        goodsAdapter.swap(generateGoodsModels(mRepo.getGoodsList()));
                    }
                });
    }

    public void selectGoodsSort(GoodsSort sort) {
        sort.selected = true;
        selectedSort.selected = false;
        sortAdapter.notifyDataSetChanged();
        goodsAdapter.swap(generateGoodsModels(mRepo.getGoodsList(sort.id)));
    }

    public void viewGoodsDetail(long goodsId) {
        mNavigator.viewGoodsDetail(goodsId);
    }

    public void deleteGood(long goodsId) {
        mRepo.deleteGood(goodsId)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            List<EpoxyModel<?>> models = goodsAdapter.getModels();
                            for (EpoxyModel<?> model : models) {
                                ItemGoodsBindingModel_ m = (ItemGoodsBindingModel_) model;
                                if (m.id() == goodsId) {
                                    goodsAdapter.removeModel(m);
                                    break;
                                }
                            }
                        } else {
                            toast("删除失败");
                        }
                    }
                });
    }

    public void manageGoodsStatus(Goods goods) {

        if (goods.count == 0 && goods.status == 2) {
            toast("请先添加商品数量，再进行上架操作");
            return;
        }

        mCallback.setLoading(true);
        mRepo.manageGoodsStatus(goods.id, goods.status)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            notifyItem(goods);
                        }
//                        else {
                        if (!TextUtils.isEmpty(response.msg)) {
                            toast(response.msg);
                        }
//                        }
                    }
                });
    }

    private ArrayList<ItemGoodsBindingModel_> generateGoodsModels(List<Goods> list) {
        ArrayList<ItemGoodsBindingModel_> models = new ArrayList<>();
        for (Goods goods : list) {
            models.add(new ItemGoodsBindingModel_()
                    .id(goods.id)
                    .goods(goods)
                    .viewModel(this));
        }
        return models;
    }

    private ArrayList<ItemGoodsSortNameBindingModel_> generateSortModels(List<GoodsSort> sorts) {
        ArrayList<ItemGoodsSortNameBindingModel_> models = new ArrayList<>();
        for (GoodsSort sort : sorts) {
            if (sort.selected) selectedSort = sort;
            models.add(new ItemGoodsSortNameBindingModel_()
                    .id(sort.id)
                    .sort(sort)
                    .viewModel(this));
        }
        return models;
    }

    private void notifyItem(Goods goods) {
        List<EpoxyModel<?>> models = goodsAdapter.getModels();
        for (EpoxyModel<?> model : models) {
            if (model.id() == goods.id) {
                ItemGoodsBindingModel_ m = (ItemGoodsBindingModel_) model;
                goods.status = goods.status == 0 ? 2 : 0;
                m.reset().id(goods.id).goods(goods).viewModel(this);
                goodsAdapter.notifyItemChanged(goodsAdapter.getModelPosition(model));
            }
        }
    }
}
