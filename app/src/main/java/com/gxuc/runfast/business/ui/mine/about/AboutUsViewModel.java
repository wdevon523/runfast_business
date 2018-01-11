package com.gxuc.runfast.business.ui.mine.about;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.gxuc.runfast.business.data.repo.LoginRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.data.response.LoginResponse;
import com.gxuc.runfast.business.data.response.UpdateAppResponse;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.gxuc.runfast.business.util.SystemUtils;

/**
 * Created by Devon on 2017/11/28.
 */

public class AboutUsViewModel extends BaseViewModel {
    public ObservableField<String> versionName = new ObservableField<>();
    private Context context;
    private LoginRepo mLoginRepo = LoginRepo.get();
    private AboutUsNavigator mNavigator;

    private int versionCode = 0;

    public AboutUsViewModel(Context context, AboutUsNavigator navigator) {
        super(context);
        this.context = context;
        mNavigator = navigator;
    }

    public void updateApp() {
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mLoginRepo.updateApp(versionCode)
                .compose(RxLifecycle.<UpdateAppResponse>bindLifecycle(this))
                .subscribe(new ResponseSubscriber<UpdateAppResponse>(mContext) {
                    @Override
                    public void onNext(UpdateAppResponse response) {
                        if (response.success && response.update) {
                            mNavigator.onUpdateApp(response.msg, response.downloadUrl);
                        } else {
//                            Toast.makeText(mContext, response.msg, Toast.LENGTH_SHORT).show();
                            toast(response.msg);
                        }
                    }
                });

    }
}
