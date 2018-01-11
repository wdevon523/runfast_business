package com.gxuc.runfast.business.ui.order;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ViewGroup;

import com.gxuc.runfast.business.HeaderOrderBindingModel_;
import com.gxuc.runfast.business.ItemOrderBindingModel_;
import com.gxuc.runfast.business.ItemOrderGoodsBindingModel_;
import com.gxuc.runfast.business.data.bean.Order;
import com.gxuc.runfast.business.data.bean.OrderGoods;
import com.gxuc.runfast.business.data.repo.OrderRepo;
import com.gxuc.runfast.business.data.response.BaseResponse;
import com.gxuc.runfast.business.epoxy.Adapter;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.ui.base.BaseViewModel;
import com.gxuc.runfast.business.util.BluetoothHelper;
import com.gxuc.runfast.business.util.PrintUtils;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.ResponseSubscriber;
import com.gxuc.runfast.business.util.RxLifecycle;
import com.gxuc.runfast.business.widget.JustifyTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * Created by Berial on 2017/8/31.
 */
public class OrderViewModel extends BaseViewModel {

    Adapter mAdapter = new Adapter();

    private long currentOrderId = -1L;
    private int currentOrderStatus = -100;

    private int status = OrderState.GOING;

    private OrderRepo mRepo = OrderRepo.get();

    private HeaderOrderBindingModel_ header = new HeaderOrderBindingModel_().id("header");

    private OrderNavigator mNavigator;
    private LoadingCallback mLoadingCallback;
    private ShowDialog mPopup;

    private ProgressHelper.Callback mCallback;

    private boolean isFirstLoad = true;

    private int page;

    {
        BluetoothDevice device = mRepo.getDevice();
        if (device != null) {
            BluetoothHelper.connect(device);
        }
    }

    Adapter getAdapter() {
        mAdapter = new Adapter();
        return mAdapter;
    }

    OrderViewModel(Context context, OrderNavigator navigator, LoadingCallback loadingCallback,
                   ProgressHelper.Callback callback, ShowDialog popup) {
        super(context);
        mNavigator = navigator;
        mLoadingCallback = loadingCallback;
        mCallback = callback;
        mPopup = popup;
    }

    void start() {
        start(status);
    }

    void start(@OrderState int status) {
        if (this.status != status) {
            this.status = status;
            isFirstLoad = true;
        }
        mRepo.resetOrderPages();
        loadOrders(page = 1);
    }

    void loadMoreComments() {
        int maxPage = mRepo.getOrderPages();
        if (page < maxPage) {
            loadOrders(++page);
        } else {
            mLoadingCallback.onLoadMoreFinish(isLastPage(page));
        }
    }

    public void toDoLeft(Order order) {
        currentOrderStatus = order.status;
        currentOrderId = order.id;
        switch (currentOrderStatus) {
            case 1:
                mPopup.onShow("拒单", "输入拒单理由");
                break;
            case -2:
                agree(currentOrderId);
                break;
            case 2:
            case 3:
            case 8:
                print(order);
                break;
        }
    }

    public void toDoRight(Order order) {
        currentOrderStatus = order.status;
        currentOrderId = order.id;
        switch (currentOrderStatus) {
            case 8:
                mPopup.onShow("退款金额", "输入退款金额");
                break;
            case -1:
            case 5:
                if (order.isDeliver) {
                    changeOrderStatus(currentOrderId, 6);
                } else {
                    print(order);
                }
                break;
            case 1:
                receive(currentOrderId);
                break;
            case -2:
                mPopup.onShow("取消订单", "输入不同意理由");
                break;
            case 2:
            case 3:
                changeOrderStatus(currentOrderId, 4);
                break;
            case 4:
                changeOrderStatus(currentOrderId, 5);
                break;
        }
    }

    void next(String text) {
        switch (currentOrderStatus) {
            case 8:
                refund(currentOrderId, text);
                break;
            case 1:
                refuse(currentOrderId, text);
                break;
            case -2:
                disagree(currentOrderId, text);
                break;
        }
    }

    public void call(String phone) {
        mNavigator.toCall(phone);
    }

    private void loadOrders(int page) {
        mRepo.loadOrders(page, status)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> {
                    if (isFirstLoad) {
                        isFirstLoad = false;
//                        mLoadingCallback.onFirstLoadFinish();
                    }
                    mLoadingCallback.onFirstLoadFinish();
                    if (page == 1) {
                        mLoadingCallback.onRefreshFinish();
                        Log.i("devon", "--------->onRefreshFinish");
                    } else {
                        mLoadingCallback.onLoadMoreFinish(isLastPage(page));
                    }
                    if (mAdapter.isEmpty()) {
                        mLoadingCallback.onLoadEmpty("暂时没有相关订单!");
                    }
                })
                .subscribe(new ResponseSubscriber<List<Order>>(mContext) {
                    @Override
                    public void onNext(List<Order> orders) {
                        if (page == 1 && !mAdapter.isEmpty()) {
                            mAdapter.swap(generateOrderModels(orders));
                        } else {
                            mAdapter.addMore(generateOrderModels(orders));
                        }
                        if (!mAdapter.isEmpty()) {
                            mAdapter.addHeader(header.status(status)
                                    .amount(mRepo.getTotalAmount())
                                    .orderCount(mRepo.getCount()));
                        }
                    }
                });
    }

    private List<ItemOrderBindingModel_> generateOrderModels(List<Order> orders) {
        ArrayList<ItemOrderBindingModel_> models = new ArrayList<>();
        for (Order order : orders) {
            models.add(new ItemOrderBindingModel_()
                    .id(order.id)
                    .order(order)
                    .orderNumber(order.orderNumber)
                    .status(order.status)
                    .statusName(order.statusName)
                    .shopperName(order.shopperName)
                    .shopperAddress(order.shopperAddress)
                    .shopperPhone(order.shopperPhone)
                    .remark(order.remark)
                    .courier(order.courier)
                    .courierPhone(order.courierPhone)
                    .deliveryTime(order.deliveryTime)
                    .goodsCount(order.goodsCount)
                    .orderTime(order.orderTime.substring(5, order.orderTime.length()))
                    .orderNo(order.orderNo)
                    .packingCharge(order.packingCharge)
                    .hasPackingCharge(order.hasPackingCharge)
                    .subtotal(order.subtotal)
                    .payAmount(order.payAmount)
                    .serviceCharge(order.serviceCharge)
                    .isDeliver(order.isDeliver)
                    .shopDeliveryCost(order.deliveryCost)
                    .refund(order.refund)
                    .hasRefund(order.hasRefund)
                    .cost(order.cost)
                    .realIncome(order.realIncome)
                    .leftButtonText(order.leftButtonText)
                    .rightButtonText(order.rightButtonText)
                    .showLeftButton(order.showLeftButton)
                    .showRightButton(order.showRightButton)
                    .isWhiteButton(order.isWhiteButton)
                    .expand(order.expand)
                    .manager(new LinearLayoutManager(mContext))
                    .goods(generateOrderGoodsAdapter(order.goods))
                    .viewModel(this));
        }
        return models;
    }

    private Adapter generateOrderGoodsAdapter(List<OrderGoods> goods) {
        Adapter adapter = new Adapter();
        adapter.addMore(generateOrderGoodsModels(goods));
        return adapter;
    }

    private List<ItemOrderGoodsBindingModel_> generateOrderGoodsModels(List<OrderGoods> goods) {
        ArrayList<ItemOrderGoodsBindingModel_> models = new ArrayList<>();
        for (OrderGoods item : goods) {
            models.add(new ItemOrderGoodsBindingModel_()
                    .id(item.id)
                    .name(item.name + item.standard + item.option)
                    .count(item.count)
                    .price(item.price));
        }
        return models;
    }

    private boolean isLastPage(int currentPage) {
        return currentPage == mRepo.getOrderPages();
    }

    private void receive(long orderId) {
        mCallback.setLoading(true);
        mRepo.receive(orderId)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }

    private void refuse(long orderId, String reason) {
        mCallback.setLoading(true);
        mRepo.refuse(orderId, reason)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }

    private void refund(long orderId, String amount) {
        mCallback.setLoading(true);
        mRepo.refund(orderId, amount)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }

    private void agree(long orderId) {
        mCallback.setLoading(true);
        mRepo.applyForCancel(orderId, true, null)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }

    private void disagree(long orderId, String reason) {
        mCallback.setLoading(true);
        mRepo.applyForCancel(orderId, false, reason)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        } else {
                            toast(response.msg);
                        }
                    }
                });
    }

    private void changeOrderStatus(long orderId, int status) {
        mCallback.setLoading(true);
        mRepo.changeOrderStatus(orderId, status)
                .compose(RxLifecycle.bindLifecycle(this))
                .doFinally(() -> mCallback.setLoading(false))
                .subscribe(new ResponseSubscriber<BaseResponse>(mContext) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.success) {
                            start();
                        }
                        toast(response.msg);
                    }
                });
    }

    private void print(Order order) {
        if (PrintUtils.getOutputStream() == null) {
            toast("请先连接打印机");
            mNavigator.toBondDevice();
            return;
        }
        PrintUtils.print(order);
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void setBottomMargin(JustifyTextView view, float margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.bottomMargin = (int) margin;
        view.setLayoutParams(params);
    }
}
