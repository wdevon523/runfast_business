package com.gxuc.runfast.business.ui.operation.goods.activity.detail.goods;

import android.content.Context;

import com.gxuc.runfast.business.ItemActivityGoodsBindingModel_;
import com.gxuc.runfast.business.data.bean.Goods;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 选择菜单
 * Created by Berial on 2017/9/2.
 */
class GoodsListViewModel extends BaseViewModel {

    final Adapter mAdapter = new Adapter();

    private OperationRepo mRepo = OperationRepo.get();

    private int mType;
    private LoadingCallback mCallback;

    private int page;
    private boolean isFirstLoad = true;

    private List<String> mIds;
    private List<String> mStandardIds;

    GoodsListViewModel(Context context, int type, String ids, String standardIds, LoadingCallback callback) {
        super(context);
        mType = type;
        mIds = ids == null ? Collections.emptyList() : Arrays.asList(ids.split(","));
        mStandardIds = standardIds == null ? Collections.emptyList() : Arrays.asList(standardIds.split(","));
        mCallback = callback;
    }

    void start() {
        mRepo.resetActivityGoodsPages();
        loadActivityGoods(page = 1);
    }

    void loadMoreActivityGoods() {
        int maxPage = mRepo.getActivityGoodsPages();
        if (page < maxPage) {
            loadActivityGoods(++page);
        }
    }

    private void loadActivityGoods(int page) {
        mRepo.loadActivityGoods(page, mType)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (page == 1) mCallback.onRefreshFinish();
                    if (isFirstLoad) {
                        mCallback.onFirstLoadFinish();
                        isFirstLoad = false;
                    }
                    mCallback.onLoadMoreFinish(isLastPage(page));
                })
                .subscribe(new ResponseSubscriber<List<Goods>>(mContext) {
                    @Override
                    public void onSuccess(List<Goods> goods) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateActivityGoodsModels(goods));
                        } else {
                            mAdapter.addMore(generateActivityGoodsModels(goods));
                        }
                    }
                });
    }

    private List<ItemActivityGoodsBindingModel_> generateActivityGoodsModels(List<Goods> goods) {
        ArrayList<ItemActivityGoodsBindingModel_> models = new ArrayList<>();
        for (Goods item : goods) {
            if (mIds.contains(String.valueOf(item.id)) || mType == 4 && mStandardIds.contains(String.valueOf(item.id))) {
                models.add(new ItemActivityGoodsBindingModel_()
                        .id(item.id)
                        .name(mType == 4 ? String.format(Locale.CHINA, "%s(%s)", item.name, item.standardName) : item.name)
                        .isHidden(true));
            }
        }
        return models;
    }

    private boolean isLastPage(int currentPage) {
        return currentPage == mRepo.getActivityGoodsPages();
    }
}
