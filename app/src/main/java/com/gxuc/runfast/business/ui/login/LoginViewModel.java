package com.gxuc.runfast.business.ui.login;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.gxuc.runfast.business.data.repo.LoginRepo;
import com.gxuc.runfast.business.data.response.LoginResponse;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;

import cn.jpush.android.api.JPushInterface;

/**
 * 登录
 * Created by Berial on 2017/8/24.
 */
public class LoginViewModel extends BaseViewModel {

    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    private LoginRepo mRepo = LoginRepo.get();
    private LoginNavigator mNavigator;
    private ProgressHelper.Callback mCallback;

    LoginViewModel(Context context, LoginNavigator navigator, ProgressHelper.Callback callback) {
        super(context);
        mNavigator = navigator;
        mCallback = callback;
    }

    public void login() {
        if (TextUtils.isEmpty(username.get())) {
            toast("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            toast("请输入密码");
            return;
        }
        mCallback.setLoading(true);
        mRepo.login(username.get(), password.get(), JPushInterface.getRegistrationID(mContext))
                .compose(RxLifecycle.<LoginResponse>bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<LoginResponse>(mContext) {
                    @Override
                    public void onNext(LoginResponse response) {
                        if (response.success) {
                            mRepo.successLogin(response.user);
                            mNavigator.onLoginSuccess();
                        }
                        toast(response.msg);
                    }
                });
    }
}
