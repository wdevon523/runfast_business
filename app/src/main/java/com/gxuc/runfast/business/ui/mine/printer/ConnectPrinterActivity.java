package com.gxuc.runfast.business.ui.mine.printer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.gxuc.runfast.business.BuildConfig;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityConnectPrinterBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 设置打印机
 * Created by Berial on 2017/8/20.
 */
public class ConnectPrinterActivity extends BaseActivity
        implements WithToolbar, LayoutProvider, PrinterNavigator, ProgressHelper.Callback,
        NeedDataBinding<ActivityConnectPrinterBinding> {

    public LinearLayoutManager pairedManager = new LinearLayoutManager(this);
    public LinearLayoutManager unpairedManager = new LinearLayoutManager(this);

    private PrinterViewModel mVM;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private ProgressHelper helper;

    private IntentFilter mFilter = new IntentFilter();
    private PrinterReceiver mReceiver;

    private Intent result;

    {
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    }

    @Override
    public int thisLayoutId() {
        return R.layout.activity_connect_printer;
    }

    @Override
    public String thisTitle() {
        return "设置打印机";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PrinterViewModel thisViewModel() {
        return new PrinterViewModel(this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityConnectPrinterBinding binding) {
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
        mReceiver = new PrinterReceiver(mVM, this);
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
        requestPermission();
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVM.start();
    }

    public void searchPrinters() {
        if (mBluetoothAdapter == null) {
            toast("该设备不支持蓝牙");
            return;
        }

        if (mBluetoothAdapter.isEnabled()) {
            mVM.start();
            mBluetoothAdapter.startDiscovery();
        } else {
            requestPermission();
        }
    }

    @Override
    public void changePrinterNavigator(BluetoothDevice device) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        startActForResult(ChangePrinterStateActivity.class, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            result = data;
//            BluetoothDevice device = (BluetoothDevice) result.getParcelableExtra("device");
//            handleDevice(device);
        }
    }

    @Subscribe
    public void handleDevice(BluetoothDevice device) {
        boolean success = result.getBooleanExtra("success", false);
        if (device != null && success) {
            boolean toPair = result.getBooleanExtra("toPair", false);
            if (toPair) {
                mVM.bondDevice(device);
            } else {
                if (result.getBooleanExtra("ignore", false)) {
                    mVM.removePairDevice(device);
                } else {
                    mVM.unbondDevice(device);
                }
            }
        }
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("应用需要连接蓝牙权限,请前往应用信息-权限中开启")
                .setNegativeButton("暂不", (d, i) -> d.dismiss())
                .setPositiveButton("去设置", (d, i) -> {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                    d.dismiss();
                })
                .show();
    }

    private void requestPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(has -> {
                    if (has) {
                        startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                    } else {
                        showPermissionDialog();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mReceiver, mFilter);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (helper.isShow()) {
//                helper.dismiss();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
