package com.gxuc.runfast.business.ui;

import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.login.LoginActivity;

/**
 * 启动页
 * Created by Berial on 2017/8/31.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onLoadData() {
        navigate();
    }

    private void navigate() {
        if (!CheckLoginState.hasLoggedIn()) {
            startAct(LoginActivity.class);
        } else {
            startAct(MainActivity.class);
        }
        finish();
    }
}
