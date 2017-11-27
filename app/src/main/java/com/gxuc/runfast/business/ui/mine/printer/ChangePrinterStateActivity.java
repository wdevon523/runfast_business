package com.gxuc.runfast.business.ui.mine.printer;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityChangePrinterStateBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.BluetoothHelper;

/**
 * 设置打印机状态
 * Created by Berial on 2017/8/20.
 */
public class ChangePrinterStateActivity extends BaseActivity
        implements WithToolbar, LayoutProvider,
        NeedDataBinding<ActivityChangePrinterStateBinding> {

    private BluetoothDevice mDevice;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_change_printer_state;
    }

    @Override
    public String thisTitle() {
        return "蓝牙打印机";
    }

    @Override
    public void onBoundDataBinding(ActivityChangePrinterStateBinding binding) {
        binding.setView(this);
        mDevice = getIntent().getParcelableExtra("device");
        binding.setDevice(mDevice);
    }

    public void changeDeviceState() {
        if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            unbond();
        } else {
            bond();
        }
    }

    public void ignoreDevice() {
        Intent intent = new Intent();
        intent.putExtra("toPair", false);
        if (BluetoothHelper.unbond(mDevice)) {
            intent.putExtra("success", true);
            intent.putExtra("ignore", true);
        } else {
            toast("断开失败！");
            intent.putExtra("success", false);
        }
        intent.putExtra("device", mDevice);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    private void bond() {
        Intent intent = new Intent();
        intent.putExtra("toPair", true);
        if (BluetoothHelper.bond(mDevice)) {
            intent.putExtra("success", true);
        } else {
            toast("配对失败！");
            intent.putExtra("success", false);
        }
        intent.putExtra("device", mDevice);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    private void unbond() {
        Intent intent = new Intent();
        intent.putExtra("toPair", false);
        if (BluetoothHelper.unbond(mDevice)) {
            intent.putExtra("success", true);
        } else {
            toast("断开失败！");
            intent.putExtra("success", false);
        }
        intent.putExtra("device", mDevice);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }
}
