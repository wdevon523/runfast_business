package com.gxuc.runfast.business.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.orhanobut.logger.Logger;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothHelper {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static boolean bond(BluetoothDevice device) {
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            return (Boolean) createBondMethod.invoke(device);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean unbond(BluetoothDevice device) {
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("removeBond");
            return (Boolean) createBondMethod.invoke(device);
        } catch (Exception e) {
            return false;
        }
    }

    public static void connect(BluetoothDevice device) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            adapter.enable();
        }
        try {
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            if (socket != null) {
                if (!socket.isConnected()) {
                    OutputStream os = PrintUtils.getOutputStream();
                    if (os != null) return;
                    socket.connect();
                    OutputStream stream = socket.getOutputStream();
                    PrintUtils.setOutputStream(stream);
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }
}
