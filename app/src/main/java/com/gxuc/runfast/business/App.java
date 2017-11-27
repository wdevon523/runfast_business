package com.gxuc.runfast.business;

import android.app.Application;
import android.content.Intent;

import com.gxuc.runfast.business.BuildConfig;
import com.gxuc.runfast.business.data.DataLayer;
import com.gxuc.runfast.business.extension.ActLifecycleCallback;
import com.gxuc.runfast.business.extension.glide.GlideApp;
import com.gxuc.runfast.business.service.GrayService;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Intent grayIntent = new Intent(getApplicationContext(), GrayService.class);
//        startService(grayIntent);

        initJPush();

        CrashReport.initCrashReport(getApplicationContext(), "09a3ddcce0", BuildConfig.DEBUG);

        DataLayer.init(this);

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        registerActivityLifecycleCallbacks(new ActLifecycleCallback());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GlideApp.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            GlideApp.get(this).clearMemory();
        }
        GlideApp.get(this).trimMemory(level);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        JPushInterface.setAlias(this, 0, JPushInterface.getRegistrationID(this));
    }
}
