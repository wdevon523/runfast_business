package com.gxuc.runfast.business.ui.operation.goods.detail;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.airbnb.epoxy.EpoxyModel;
import com.gxuc.runfast.business.ItemGoodsOptionBindingModel_;
import com.gxuc.runfast.business.ItemGoodsSubOptionBindingModel_;
import com.gxuc.runfast.business.ItemStandardBindingModel_;
import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.bean.Goods;
import com.gxuc.runfast.business.data.bean.Option;
import com.gxuc.runfast.business.data.bean.Standard;
import com.gxuc.runfast.business.data.bean.SubOption;
import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.data.response.UploadFileResponse;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 * Created by Berial on 2017/9/7.
 */
public class GoodsDetailViewModel extends BaseViewModel {

    public Adapter standardAdapter = new Adapter();
    public Adapter optionAdapter = new Adapter();

    public ObservableInt status = new ObservableInt(2);
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> sortName = new ObservableField<>();
    public ObservableLong sortId = new ObservableLong();
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableBoolean isLimited = new ObservableBoolean(false);
    public ObservableBoolean needFullPrice = new ObservableBoolean(false);
    public ObservableField<String> limitCount = new ObservableField<>();
    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> endTime = new ObservableField<>();
    public ObservableBoolean needPackingCharge = new ObservableBoolean(false);
    public ObservableField<String> count = new ObservableField<>();
    public ObservableField<String> describe = new ObservableField<>();
    public ObservableField<String> imagePath = new ObservableField<>();
    public ObservableField<String> thumbnailPath = new ObservableField<>();

    public ObservableBoolean standardIsEmpty = new ObservableBoolean(true);
    public ObservableBoolean standardHasOne = new ObservableBoolean(true);
    public ObservableBoolean optionIsEmpty = new ObservableBoolean(true);
    public ObservableBoolean isAddGoods = new ObservableBoolean(true);

    private ObservableField<Goods> mGoodsObservable = new ObservableField<>();

    private OperationRepo mRepo = OperationRepo.get();

    private long mGoodsId;

    private GoodsDetailNavigator mNavigator;
    private LoadingCallback mLoadingCallback;
    private ProgressHelper.Callback mCallback;

    private ArrayList<Standard> mStandards = new ArrayList<>();
    private ArrayList<Option> mOptions = new ArrayList<>();

    GoodsDetailViewModel(Context context, long goodsId,
                         GoodsDetailNavigator navigator,
                         LoadingCallback loadingCallback,
                         ProgressHelper.Callback callback) {
        super(context);
        mGoodsId = goodsId;
        mNavigator = navigator;
        mLoadingCallback = loadingCallback;
        mCallback = callback;
        isAddGoods.set(mGoodsId == 0);
    }

    void start() {
        mGoodsObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Goods goods = mGoodsObservable.get();
                if (goods != null) {
                    name.set(goods.name);
                    status.set(goods.status);
                    sortName.set(goods.sortName);
                    sortId.set(goods.sortId);
                    imageUrl.set(goods.imageUrl);
                    isLimited.set(goods.isLimited);
                    needFullPrice.set(goods.needFullPrice);
                    limitCount.set(goods.limitCount);
                    startTime.set(goods.startTime);
                    endTime.set(goods.endTime);
                    needPackingCharge.set(goods.needPackingCharge);
                    count.set(goods.count);
                    describe.set(goods.describe);
                    imagePath.set(goods.imagePath);
                    thumbnailPath.set(goods.thumbnailPath);

                    mStandards.addAll(goods.standards);
                    standardAdapter.addMore(generateStandardModels(mStandards));
                    standardIsEmpty.set(standardAdapter.isEmpty());
                    standardHasOne.set(mStandards.size() == 1);

                    mOptions.addAll(goods.options);
                    optionAdapter.addMore(generateOptionModels(mOptions));
                    optionIsEmpty.set(optionAdapter.isEmpty());
                }
            }
        });
        mRepo.loadGoodsDetail(mGoodsId)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mLoadingCallback.onFirstLoadFinish())
                .subscribe(new ResponseSubscriber<Goods>(mContext) {
                    @Override
                    public void onNext(Goods goods) {
                        mGoodsObservable.set(goods);
                    }
                });
    }

    public void addStandard() {
        Standard standard = new Standard();
        mStandards.add(standard);
        ItemStandardBindingModel_ model = new ItemStandardBindingModel_()
                .standard(standard).viewModel(this);
        standardAdapter.addModel(model);
        standardIsEmpty.set(standardAdapter.isEmpty());
        standardHasOne.set(mStandards.size() == 1);
    }

    public void addOption() {
        Option option = new Option();
        mOptions.add(option);
        Adapter adapter = new Adapter();
        ItemGoodsOptionBindingModel_ model = new ItemGoodsOptionBindingModel_()
                .option(option)
                .adapter(adapter)
                .manager(new LinearLayoutManager(mContext))
                .viewModel(this);
        optionAdapter.addModel(model);
        optionIsEmpty.set(optionAdapter.isEmpty());
    }

    public void addSubOption(Adapter adapter, Option option) {
        SubOption subOption = new SubOption();
        option.options.add(subOption);
        ItemGoodsSubOptionBindingModel_ model = new ItemGoodsSubOptionBindingModel_()
                .option(option)
                .subOption(subOption)
                .adapter(adapter)
                .viewModel(this);
        adapter.addModel(model);
    }

    public void deleteStandard(Standard standard) {
        if (standardAdapter.getModels().size() == 1) {
            toast("商品规格不能为空");
            return;
        }
        List<EpoxyModel<?>> models = standardAdapter.getModels();
        for (EpoxyModel<?> model : models) {
            ItemStandardBindingModel_ m = (ItemStandardBindingModel_) model;
            if (m.standard() == standard) {
                standardAdapter.removeModel(model);
                mStandards.remove(standard);
                break;
            }
        }
        standardIsEmpty.set(standardAdapter.isEmpty());
        standardHasOne.set(mStandards.size() == 1);
    }

    public void deleteOption(Option option) {
        List<EpoxyModel<?>> models = optionAdapter.getModels();
        for (EpoxyModel<?> model : models) {
            ItemGoodsOptionBindingModel_ m = (ItemGoodsOptionBindingModel_) model;
            if (m.option() == option) {
                optionAdapter.removeModel(model);
                mOptions.remove(option);
                break;
            }
        }
        optionIsEmpty.set(optionAdapter.isEmpty());
    }

    public void deleteSubOption(Adapter adapter, Option option, SubOption subOption) {
        List<EpoxyModel<?>> models = adapter.getModels();
        for (EpoxyModel<?> model : models) {
            ItemGoodsSubOptionBindingModel_ m = (ItemGoodsSubOptionBindingModel_) model;
            if (subOption == m.subOption()) {
                adapter.removeModel(model);
                option.options.remove(subOption);
                break;
            }
        }
    }

    public void submit() {
        if (TextUtils.isEmpty(name.get())) {
            toast("请填写商品名称");
            return;
        }
        if (TextUtils.isEmpty(count.get())) {
            toast("商品数量不能为空");
            return;
        }
        if (sortId.get() == 0 || TextUtils.isEmpty(sortName.get())) {
            toast("请选择商品分类");
            return;
        }
        if (mStandards.isEmpty()) {
            toast("商品规格不能为空");
            return;
        }
        for (Standard standard : mStandards) {
            if (TextUtils.isEmpty(standard.name) || TextUtils.isEmpty(standard.price)) {
                toast("商品规格名称或价格不能为空");
                return;
            }
        }

        if (isLimited.get()) {
            if (TextUtils.isEmpty(startTime.get())) {
                toast("请设置限购开始时间");
                return;
            }
            if (TextUtils.isEmpty(endTime.get())) {
                toast("请设置限购结束时间");
                return;
            }
            if (TextUtils.isEmpty(limitCount.get())) {
                toast("请设输入限购数量");
                return;
            }
        }

        mCallback.setLoading(true);
        mRepo.saveOrUpdateGoods(status.get(), mGoodsId, name.get(), sortId.get(), sortName.get(), imagePath.get(),
                thumbnailPath.get(), isLimited.get(), needFullPrice.get(), limitCount.get(), startTime.get(),
                endTime.get(), needPackingCharge.get(), count.get(), describe.get(), mStandards, mOptions)
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

    void uploadImage(Uri uri) {
        mCallback.setLoading(true);
        try {
            BusinessRepo repo = BusinessRepo.get();
            String filePath = new File(new URI(uri.toString())).getAbsolutePath();
            repo.uploadImage(filePath)
                    .compose(RxLifecycle.bindLifecycle(this))
                    .doFinally(() -> mCallback.setLoading(false))
                    .subscribe(new ResponseSubscriber<UploadFileResponse>(mContext) {
                        @Override
                        public void onNext(UploadFileResponse response) {
                            if (response.success) {
                                imagePath.set(response.filePath);
                                thumbnailPath.set(response.thumbnailPath);
                                imageUrl.set(ApiServiceFactory.HOST + response.filePath);
                            } else {
                                toast(response.msg);
                            }
                        }
                    });
        } catch (Exception e) {
            toast("选择了无效的图片");
            Logger.e(e.toString());
        }
    }

    private List<ItemStandardBindingModel_> generateStandardModels(List<Standard> standards) {
        ArrayList<ItemStandardBindingModel_> models = new ArrayList<>(standards.size());
        if (standards.isEmpty()) standards.add(new Standard());
        for (Standard standard : standards) {
            models.add(new ItemStandardBindingModel_()
                    .id(standard.id)
                    .standard(standard)
                    .viewModel(this));
        }
        return models;
    }

    private List<ItemGoodsOptionBindingModel_> generateOptionModels(List<Option> options) {
        ArrayList<ItemGoodsOptionBindingModel_> models = new ArrayList<>(options.size());
        if (options.isEmpty()) options.add(new Option());
        for (Option option : options) {

            Adapter adapter = new Adapter();
            adapter.addMore(generateSubOptionModels(adapter, option));

            models.add(new ItemGoodsOptionBindingModel_()
                    .id(option.id)
                    .option(option)
                    .adapter(adapter)
                    .manager(new LinearLayoutManager(mContext))
                    .viewModel(this));
        }
        return models;
    }

    private List<ItemGoodsSubOptionBindingModel_> generateSubOptionModels(Adapter adapter, Option option) {
        List<SubOption> options = option.options;
        ArrayList<ItemGoodsSubOptionBindingModel_> models = new ArrayList<>(options.size());
        if (options.isEmpty()) options.add(new SubOption());
        for (SubOption subOption : options) {
            models.add(new ItemGoodsSubOptionBindingModel_()
                    .id(option.id)
                    .option(option)
                    .subOption(subOption)
                    .adapter(adapter)
                    .viewModel(this));
        }
        return models;
    }
}
