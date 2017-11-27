package com.gxuc.runfast.business.data.repo;

import android.bluetooth.BluetoothDevice;
import android.util.LongSparseArray;

import com.gxuc.runfast.business.data.ApiService;
import com.gxuc.runfast.business.data.ApiServiceFactory;
import com.gxuc.runfast.business.data.bean.Order;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.util.Utils;

import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 订单
 * Created by Berial on 2017/8/31.
 */
public class OrderRepo {

    private LongSparseArray<Object> autoReceiveOrders = new LongSparseArray<>();

    private int orderPages = -1;

    private int count = -1;
    private String totalAmount = "";

    private OrderRepo() {}

    public static OrderRepo get() {
        return OrderRepoHolder.sInstance;
    }

    private static class OrderRepoHolder {
        private static final OrderRepo sInstance = new OrderRepo();
    }

    private ApiService getApi() {
        return ApiServiceFactory.getApi();
    }

    private long getId() {
        return Paper.book().read("id", 0L);
    }

    private long getBusinessId() {
        return Paper.book().read("businessId", 0L);
    }

    public Observable<List<Order>> loadOrders(int page, int status) {
        if (orderPages != -1 && page > orderPages) {
            return Observable.just(Collections.<Order>emptyList());
        }
        return getApi().loadOrders(getId(), getBusinessId(), page, status)
                .map(response -> {
                    orderPages = response.totalPage;
                    count = response.total;
                    totalAmount = Utils.formatFloorNumber(response.totalAmount, 2);
                    return response.map();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void resetOrderPages() {
        orderPages = -1;
    }

    public int getOrderPages() {
        return orderPages;
    }

    public Observable<BaseResponse> receive(long orderId) {
        return getApi().receive(getId(), orderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> refuse(long orderId, String reason) {
        return getApi().refuse(getId(), orderId, reason)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> applyForCancel(long orderId, boolean isAgreed, String reason) {
        return getApi().applyForCancel(getId(), orderId, isAgreed ? 2 : 3, reason)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> refund(long orderId, String amount) {
        return getApi().refund(getId(), orderId, amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BaseResponse> changeOrderStatus(long orderId, int status) {
        return getApi().changeOrderStatus(getId(), orderId, status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public int getCount() {
        return count;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void resetCount() {
        count = -1;
    }

    public void resetTotalAmount() {
        totalAmount = "";
    }

    public void saveDevice(BluetoothDevice device) {
        Paper.book().write("device", device);
    }

    public void removeDevice() {
        Paper.book().delete("device");
    }

    public void saveOrder(long orderId) {
        autoReceiveOrders.put(orderId, null);
    }

    public boolean contains(long orderId) {
        return autoReceiveOrders.indexOfKey(orderId) >= 0;
    }

    public BluetoothDevice getDevice() {
        return Paper.book().read("device");
    }
}
