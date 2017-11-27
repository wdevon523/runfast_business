package com.gxuc.runfast.business.ui.mine.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gxuc.runfast.business.util.ProgressHelper;

import org.greenrobot.eventbus.EventBus;

class PrinterReceiver extends BroadcastReceiver {

    private PrinterViewModel mVM;
    private ProgressHelper.Callback mCallback;

    PrinterReceiver(PrinterViewModel vm, ProgressHelper.Callback callback) {
        mVM = vm;
        mCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.Major.IMAGING) {
                mVM.addDevice(device);
            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            mCallback.setLoading(true);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            mCallback.setLoading(false);
        }

        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                mCallback.setLoading(true);
            } else {
                EventBus.getDefault().post(device);
                mCallback.setLoading(false);
            }
        }
    }
}
