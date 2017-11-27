package com.gxuc.runfast.business.ui.mine.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.databinding.ObservableBoolean;

import com.airbnb.epoxy.EpoxyModel;
import com.gxuc.runfast.business.ItemPrinterBindingModel_;
import com.gxuc.runfast.business.data.repo.OrderRepo;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.BluetoothHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrinterViewModel extends BaseViewModel {

    public final ObservableBoolean isSupportedBluetooth = new ObservableBoolean(true);

    public Adapter pairedAdapter = new Adapter();
    public Adapter unpairedAdapter = new Adapter();

    private OrderRepo mRepo = OrderRepo.get();

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private PrinterNavigator mNavigator;

    private boolean isBonded;

    PrinterViewModel(Context context, PrinterNavigator navigator) {
        super(context);
        mNavigator = navigator;
        isSupportedBluetooth.set(mBluetoothAdapter != null);
    }

    public void changePrinterState(BluetoothDevice device) {
        mNavigator.changePrinterNavigator(device);
    }

    void start() {
        if (mBluetoothAdapter == null) {
            toast("该设备不支持蓝牙");
            return;
        }
        isBonded = false;
        pairedAdapter.clear();
        unpairedAdapter.clear();
        if (mBluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            pairedAdapter.swap(generatePrinterModels(devices));
        }

    }

    void addDevice(BluetoothDevice device) {
        if (device.getBluetoothClass().getMajorDeviceClass() != BluetoothClass.Device.Major.IMAGING) {
            return;
        }

        ItemPrinterBindingModel_ model = new ItemPrinterBindingModel_()
                .id(device.getAddress())
                .device(device)
                .viewModel(this);

        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            List<EpoxyModel<?>> models = pairedAdapter.getModels();
            boolean result = true;
            for (EpoxyModel<?> item : models) {
                result &= !model.device().getAddress().equals(((ItemPrinterBindingModel_) item).device().getAddress());
            }
            if (result) {
                pairedAdapter.addModel(model);
                mRepo.saveDevice(device);
                BluetoothHelper.connect(device);
            }
        } else {
            List<EpoxyModel<?>> models = unpairedAdapter.getModels();
            boolean result = true;
            for (EpoxyModel<?> item : models) {
                result &= !model.device().getAddress().equals(((ItemPrinterBindingModel_) item).device().getAddress());
            }
            if (result) {
                unpairedAdapter.addModel(model);
            }
        }
    }

    void bondDevice(BluetoothDevice device) {
        List<EpoxyModel<?>> models = unpairedAdapter.getModels();
        boolean result = false;
        EpoxyModel<?> model = null;
        for (EpoxyModel<?> m : models) {
            if (device.getAddress().equals(((ItemPrinterBindingModel_) m).device().getAddress())) {
                result = true;
                model = m;
                break;
            }
        }
        if (result) {
            unpairedAdapter.removeModel(model);
            pairedAdapter.addModel(model);
            mRepo.saveDevice(device);
            BluetoothHelper.connect(device);
        }
    }

    void unbondDevice(BluetoothDevice device) {
        List<EpoxyModel<?>> models = pairedAdapter.getModels();
        boolean result = false;
        EpoxyModel<?> model = null;
        for (EpoxyModel<?> m : models) {
            if (device.getAddress().equals(((ItemPrinterBindingModel_) m).device().getAddress())) {
                result = true;
                model = m;
                break;
            }
        }
        if (result) {
            pairedAdapter.removeModel(model);
            unpairedAdapter.addModel(model);
            mRepo.removeDevice();
        }
    }

    void removePairDevice(BluetoothDevice device) {
        List<EpoxyModel<?>> models = pairedAdapter.getModels();
        boolean result = false;
        EpoxyModel<?> model = null;
        for (EpoxyModel<?> m : models) {
            if (device.getAddress().equals(((ItemPrinterBindingModel_) m).device().getAddress())) {
                result = true;
                model = m;
                break;
            }
        }
        if (result){
            pairedAdapter.removeModel(model);
            mRepo.removeDevice();
        }
    }

    private List<ItemPrinterBindingModel_> generatePrinterModels(Set<BluetoothDevice> devices) {
        ArrayList<ItemPrinterBindingModel_> models = new ArrayList<>();
        if (devices != null && !devices.isEmpty()) {
            for (BluetoothDevice device : devices) {
                if (device.getBluetoothClass().getMajorDeviceClass() != BluetoothClass.Device.Major.IMAGING) continue;

                if (!isBonded) {
                    BluetoothHelper.connect(device);
                    mRepo.saveDevice(device);
                    isBonded = true;
                }

                models.add(new ItemPrinterBindingModel_()
                        .id(device.getAddress())
                        .device(device)
                        .viewModel(this));
            }
        }
        return models;
    }
}
