package com.gxuc.runfast.business.ui.operation.goods.activity.add.menu;

import android.content.Context;
import android.text.TextUtils;

import com.gxuc.runfast.business.ItemActivityGoodsBindingModel_;
import com.gxuc.runfast.business.data.bean.Goods;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.gxuc.runfast.business.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

/**
 * 选择菜单
 * Created by Berial on 2017/9/2.
 */
public class SelectGoodsViewModel extends BaseViewModel {

    final Adapter mAdapter = new Adapter();

    private OperationRepo mRepo = OperationRepo.get();

    private int mType;

    private LoadingCallback mCallback;

    private int page;
    private boolean isFirstLoad = true;

    private TreeSet<Long> selectGoodsIds = new TreeSet<>();
    private TreeSet<Long> selectStandardIds = new TreeSet<>();

    SelectGoodsViewModel(Context context, int type, String ids, String standardIds, LoadingCallback callback) {
        super(context);
        mType = type;
        mCallback = callback;
        putIds(Utils.emptyToValue(ids, ""));
        putStandardIds(Utils.emptyToValue(standardIds, ""));
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

    Set<Long> getSelectGoodsIds() {
        return selectGoodsIds;
    }

    Set<Long> getSelectStandardIds() {
        return selectStandardIds;
    }

    public void addGoods(boolean isOn, long id, long standardId) {
        if (isOn) {
            selectGoodsIds.add(mType == 4 ? standardId : id);
            selectStandardIds.add(mType == 4 ? id : standardId);
        } else {
            selectGoodsIds.remove(mType == 4 ? standardId : id);
            selectStandardIds.remove(mType == 4 ? id : standardId);
        }
    }

    private void putIds(String ids) {
        String[] idArr = ids.split(",");
        if (TextUtils.isEmpty(ids) || idArr.length == 0) return;
        for (String id : idArr) {
            selectGoodsIds.add(Long.parseLong(id));
        }
    }

    private void putStandardIds(String standardIds) {
        String[] idArr = standardIds.split(",");
        if (TextUtils.isEmpty(standardIds) || idArr.length == 0) return;
        for (String id : idArr) {
            selectStandardIds.add(Long.parseLong(id));
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
            if (selectGoodsIds.contains(item.id) || mType == 4 && selectStandardIds.contains(item.id)) {
                item.selected = true;
            }
            models.add(new ItemActivityGoodsBindingModel_()
                    .id(item.id)
                    .standardId(item.standardId)
                    .name(mType == 4 ? String.format(Locale.CHINA, "%s(%s)", item.name, item.standardName) : item.name)
                    .selected(item.selected)
                    .viewModel(this));
        }
        return models;
    }

    private boolean isLastPage(int currentPage) {
        return currentPage == mRepo.getActivityGoodsPages();
    }
}
