package com.gxuc.runfast.business.ui.mine.password;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

/**
 * 修改密码
 * Created by Berial on 2017/9/1.
 */
public class ChangePasswordViewModel extends BaseViewModel {

    public final ObservableField<String> oldPassword = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> confirmPassword = new ObservableField<>();

    private BusinessRepo mRepo = BusinessRepo.get();

    private ChangePasswordNavigator mNavigator;
    private ProgressHelper.Callback mCallback;

    public ChangePasswordViewModel(Context context, ChangePasswordNavigator navigator, ProgressHelper.Callback callback) {
        super(context);
        mNavigator = navigator;
        mCallback = callback;
    }

    public void changePassword() {
        if (TextUtils.isEmpty(oldPassword.get())) {
            toast("请输入原密码");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            toast("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword.get())) {
            toast("请确认新密码");
            return;
        }
        if (!password.get().equals(confirmPassword.get())) {
            toast("两次输入的密码不一致");
            return;
        }
        mCallback.setLoading(true);
        mRepo.changePassword(oldPassword.get(), password.get())
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            mNavigator.onChangePasswordSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }
}
