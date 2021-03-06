package com.gxuc.runfast.business.data.repo;


import com.gxuc.runfast.business.data.ApiService;
import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.bean.User;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.data.response.LoginResponse;
import com.gxuc.runfast.business.data.response.UpdateAppResponse;
import com.gxuc.runfast.business.util.Md5Utils;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginRepo {

    private LoginRepo() {
    }

    public static LoginRepo get() {
        return LoginRepoHolder.sInstance;
    }

    private static class LoginRepoHolder {
        private static final LoginRepo sInstance = new LoginRepo();
    }

    private ApiService getApi() {
        return ApiServiceFactory.getApi();
    }

    public Observable<LoginResponse> login(String username, String password, String jPushId, int versionCode) {
        final String md5Password = Md5Utils.getMd5(password);
        return getApi().login(username, md5Password, "3", "", "", "", "0", versionCode, jPushId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void successLogin(User user) {
        Paper.book().write("hasLoggedIn", true);
        Paper.book().write("user", user);
        Paper.book().write("id", user.id);
        Paper.book().write("businessId", user.businessId);
    }

    public void successLogout() {
        Paper.book().destroy();
        Paper.book().write("hasLoggedIn", false);
    }

    public boolean hasLoggedIn() {
        return Paper.book().read("hasLoggedIn", false);
    }

    public Observable<LoginResponse> logout(String alias) {

        return getApi().logout(alias)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<UpdateAppResponse> updateApp(int versionCode) {

        return getApi().updateApp(versionCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
