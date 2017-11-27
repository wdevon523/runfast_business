package com.gxuc.runfast.business.extension;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gxuc.runfast.business.R;

import cn.jpush.android.api.JPushInterface;

public class ActLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(final Activity activity, Bundle bundle) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (activity instanceof LayoutProvider) {
            if (activity instanceof NeedDataBinding) {
                ViewDataBinding binding = DataBindingUtil.setContentView(activity, ((LayoutProvider) activity).thisLayoutId());
                ((NeedDataBinding) activity).onBoundDataBinding(binding);
            } else {
                activity.setContentView(((LayoutProvider) activity).thisLayoutId());
            }
        }
        if (activity instanceof WithToolbar) {
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            TextView title = activity.findViewById(R.id.title);
            if (title != null) {
                title.setText(((WithToolbar) activity).thisTitle());
            }
            if (toolbar != null) {
                if (activity instanceof WithMenu) {
                    toolbar.inflateMenu(((WithMenu) activity).thisMenu());
                    toolbar.setOnMenuItemClickListener(((WithMenu) activity).thisOnMenuItemClickListener());
                }
                toolbar.setNavigationOnClickListener(view -> activity.onBackPressed());
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        JPushInterface.onResume(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        JPushInterface.onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
