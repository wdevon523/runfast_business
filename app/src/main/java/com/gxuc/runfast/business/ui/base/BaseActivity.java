package com.gxuc.runfast.business.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gxuc.runfast.business.util.ActivityUtils;
import com.gxuc.runfast.business.util.IMMLeaks;
import com.gxuc.runfast.business.util.ViewModelHolder;

import org.greenrobot.eventbus.EventBus;

public class BaseActivity extends AppCompatActivity {

    public static final String VIEW_MODEL_TAG = "VIEW_MODEL_TAG";

    private BaseViewModel mVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitViews();
        onLoadData();
    }

    protected void onInitViews() {}

    protected void onLoadData() {}

    protected void startAct(Class actClazz) {
        startActivity(new Intent(this, actClazz));
    }

    protected void startActForResult(Class actClazz) {
        startActivityForResult(new Intent(this, actClazz), getClass().hashCode() & 0xffff);
    }

    protected void startActForResult(Class actClazz, Bundle bundle) {
        startActForResult(actClazz, bundle, getClass().hashCode() & 0xffff);
    }

    protected void startActForResult(Class actClazz, Bundle bundle, int requestCode) {
        if (bundle == null) {
            startActivityForResult(new Intent(this, actClazz), requestCode);
        } else {
            startActivityForResult(new Intent(this, actClazz).putExtras(bundle), requestCode);
        }
    }

    protected void startAct(Class actClazz, Bundle data) {
        startActivity(new Intent(this, actClazz).putExtras(data));
    }

    @SuppressWarnings("unchecked")
    protected <T extends BaseViewModel> T findOrCreateViewModel() {
        ViewModelHolder<T> retainedViewModel =
                (ViewModelHolder<T>) getFragmentManager()
                        .findFragmentByTag(VIEW_MODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewModel() != null) {
            // If the model was retained, return it.
            mVM = retainedViewModel.getViewModel();
            return retainedViewModel.getViewModel();
        } else {
            // There is no GoodsSortViewModel yet, create it.
            T vm = thisViewModel();
            mVM = vm;
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            if (vm != null) {
                ActivityUtils.addFragmentToActivity(
                        getFragmentManager(),
                        ViewModelHolder.createContainer(vm),
                        VIEW_MODEL_TAG);
            }
            return vm;
        }
    }

    protected <T extends BaseViewModel> T thisViewModel() {
        return null;
    }

    protected void post(Object event) {
        EventBus.getDefault().post(event);
    }

    public void registerBus() {
        EventBus.getDefault().register(this);
    }

    public void unregisterBus() {
        EventBus.getDefault().unregister(this);
    }

    protected void toast(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        IMMLeaks.fixFocusedViewLeak(getApplication());
        if (mVM != null) {
            mVM.onDestroy();
        }
        super.onDestroy();
    }
}
