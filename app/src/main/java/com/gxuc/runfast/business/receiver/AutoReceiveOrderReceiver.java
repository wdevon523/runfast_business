package com.gxuc.runfast.business.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gxuc.runfast.business.data.bean.Order;
import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.data.repo.OrderRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.event.RefreshOrderEvent;
import com.gxuc.runfast.business.ui.mine.printer.ConnectPrinterActivity;
import com.gxuc.runfast.business.ui.order.OrderState;
import com.gxuc.runfast.business.util.BluetoothHelper;
import com.gxuc.runfast.business.util.PrintUtils;
import com.gxuc.runfast.business.util.ResponseSubscriber;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

import static com.gxuc.runfast.business.receiver.JPushReceiver.MESSAGE_RECEIVED_ACTION;

/**
 * 自动接单
 * Created by Berial on 2017/10/16.
 */
public class AutoReceiveOrderReceiver extends BroadcastReceiver {

    private OrderRepo mRepo = OrderRepo.get();

    {
        BluetoothDevice device = mRepo.getDevice();
        if (device != null) {
            BluetoothHelper.connect(device);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!BusinessRepo.get().isAutomatic()) return;
        if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
            autoReceiveOrder(context);
        }
    }

    private void autoReceiveOrder(Context context) {
        mRepo.resetOrderPages();
        mRepo.loadOrders(1, OrderState.PENDING)
                .flatMap(Observable::fromIterable)
                .filter(order -> order.status == 1)
                .take(1)
                .subscribe(new ResponseSubscriber<Order>(context) {
                    @Override
                    public void onNext(Order order) {
                        if (!mRepo.contains(order.id)) {
                            mRepo.saveOrder(order.id);
                            mRepo.receive(order.id)
                                    .subscribe(new ResponseSubscriber<BaseResponse>(context) {
                                        @Override
                                        public void onNext(BaseResponse response) {
                                            if (response.success) {
                                                EventBus.getDefault().post(new RefreshOrderEvent());
                                                print(context, order);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void print(Context context, Order order) {
        if (PrintUtils.getOutputStream() == null) {
            Toast.makeText(context, "请先连接打印机", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, ConnectPrinterActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return;
        }
        PrintUtils.print(order);
    }
}
