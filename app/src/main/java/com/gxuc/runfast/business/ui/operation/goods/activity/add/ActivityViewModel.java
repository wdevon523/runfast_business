package com.gxuc.runfast.business.ui.operation.goods.activity.add;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.gxuc.runfast.business.data.repo.OperationRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 活动
 * Created by Berial on 2017/9/1.
 */
public class ActivityViewModel extends BaseViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> endTime = new ObservableField<>();
    public ObservableField<String> full = new ObservableField<>();
    public ObservableField<String> choose = new ObservableField<>();
    public ObservableField<String> less = new ObservableField<>();
    public ObservableField<String> discount = new ObservableField<>();
    public ObservableField<String> disprice = new ObservableField<>();
    public ObservableField<String> goodsId = new ObservableField<>();
    public ObservableField<String> standardId = new ObservableField<>();
    public ObservableField<String> goods = new ObservableField<>();
    public ObservableField<String> typeName = new ObservableField<>();
    public ObservableInt type = new ObservableInt(0);

    private OperationRepo mRepo = OperationRepo.get();

    private ActivityNavigator mNavigator;

    ActivityViewModel(Context context, ActivityNavigator navigator) {
        super(context);
        mNavigator = navigator;
    }

    public void saveOrUpdateActivity() {
        if (TextUtils.isEmpty(name.get())) {
            toast("请输入活动名称");
            return;
        }
        if (TextUtils.isEmpty(startTime.get())) {
            toast("开始时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(endTime.get())) {
            toast("结束时间不能为空");
            return;
        }
        if (type.get() == 0) {
            toast("请选择活动类型");
            return;
        }
        if (!checkValuesWithType1()) return;
        if (!checkValuesWithType2()) return;
        if (!checkValuesWithType3()) return;
        if (!checkValuesWithType4()) return;
        if (!checkValuesWithType5()) return;

        mRepo.saveOrUpdateActivity(
                name.get(),
                startTime.get().replace(".", "-"),
                endTime.get().replace(".", "-"),
                type.get(),
                full.get(),
                less.get(),
                discount.get(),
                disprice.get(),
                goodsId.get(),
                goods.get(),
                standardId.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success || "添加成功！".equals(response.msg)) {
                            mNavigator.onEditSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }

    public void selectGoods() {
        mNavigator.toSelectGoods();
    }

    private boolean checkValuesWithType1() {
        if ((full.get() == null || less.get() == null) && type.get() == 1) {
            toast("请设置满减数值");
            return false;
        }
        return true;
    }

    private boolean checkValuesWithType2() {
        if (goodsId.get() == null && type.get() == 2) {
            toast("请选择打折商品");
            return false;
        }
        if (discount.get() == null && type.get() == 2) {
            toast("请输入打折数");
            return false;
        }
        return true;
    }

    private boolean checkValuesWithType3() {
        if (goodsId.get() == null && type.get() == 3) {
            toast("请选择附带赠品的商品");
            return false;
        }
        if (goods.get() == null && type.get() == 3) {
            toast("请填写赠品");
            return false;
        }
        return true;
    }

    private boolean checkValuesWithType4() {
        if (goodsId.get() == null && type.get() == 4) {
            toast("请选择特价商品");
            return false;
        }
        if (disprice.get() == null && type.get() == 4) {
            toast("请填写特价价格");
            return false;
        }
        return true;
    }

    private boolean checkValuesWithType5() {
        if (full.get() == null && type.get() == 5) {
            toast("请设置满减数值");
            return false;
        }
        return true;
    }
}
