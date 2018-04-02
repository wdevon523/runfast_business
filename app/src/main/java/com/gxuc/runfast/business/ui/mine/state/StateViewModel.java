package com.gxuc.runfast.business.ui.mine.state;

import android.content.Context;

import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 营业状态
 * Created by Berial on 2017/8/31.
 */
class StateViewModel extends BaseViewModel {

    private BusinessRepo mRepo = BusinessRepo.get();

    private StateNavigator mNavigator;

    StateViewModel(Context context, StateNavigator navigator) {
        super(context);
        mNavigator = navigator;
    }

    void changeBusinessState(String state) {
        mRepo.changeBusinessState(state)
                .compose(RxLifecycle.bindLifecycle(this))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onChangeStateSuccess();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }
}
