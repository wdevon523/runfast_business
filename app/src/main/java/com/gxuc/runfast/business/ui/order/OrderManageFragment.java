package com.gxuc.runfast.business.ui.order;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;

import com.gxuc.runfast.business.BuildConfig;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.FragmentOrderManageBinding;
import com.gxuc.runfast.business.extension.LoadingCallback;
import com.gxuc.runfast.business.extension.OnTabSelectedAdapter;
import com.gxuc.runfast.business.extension.recyclerview.SpaceDecoration;
import com.gxuc.runfast.business.ui.EditTextDialog;
import com.gxuc.runfast.business.ui.base.BaseFragment;
import com.gxuc.runfast.business.ui.mine.printer.ConnectPrinterActivity;
import com.gxuc.runfast.business.util.ProgressHelper;
import com.gxuc.runfast.business.util.TabLayoutHelper;
import com.gxuc.runfast.business.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * 订单管理
 * Created by Berial on 2017/8/19.
 */
public class OrderManageFragment extends BaseFragment<FragmentOrderManageBinding>
        implements LoadingCallback, ShowDialog, ProgressHelper.Callback,
        OrderNavigator {

    private OrderViewModel mVM;

    private EditTextDialog mDialog;

    private ProgressHelper helper;
    private String selectTabStr = "进行中";
    private boolean isResume;
    FragmentOrderManageBinding binding;


    @Override
    public int thisLayoutId() {
        return R.layout.fragment_order_manage;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected OrderViewModel thisViewModel() {
        return new OrderViewModel(getActivity(), this, this, this, this);
    }

    @Override
    protected void onBoundBinding(FragmentOrderManageBinding binding) {
        this.binding = binding;
        binding.setAdapter((mVM = findOrCreateViewModel()).getAdapter());
    }

    public void refreshNewOrder() {
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("devon", "msg.what= " + msg.what);
            switch (msg.what) {
                case 1:
                    if (TextUtils.equals("进行中", selectTabStr)) {
                        onLoadData();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);

        configTabLayout();

        final SmartRefreshLayout refresh = mBinding.refreshRoot.refresh;
        refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mVM.loadMoreComments();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mVM.start();
            }
        });

        final RecyclerView recyclerView = mBinding.refreshRoot.recyclerView;
        recyclerView.setAnimation(null);
        recyclerView.addItemDecoration(new SpaceDecoration(
                (int) Utils.dp2px(getActivity(), 10)
        ));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDialog = EditTextDialog.create(getActivity())
                .setPositiveButton("确定", text -> mVM.next(text));
    }

    private void configTabLayout() {
        final TabLayout tabs = mBinding.tabs;

        TabLayoutHelper.setIndicatorWidth(tabs, 32, 32);

        tabs.addTab(tabs.newTab().setText("进行中"));
        tabs.addTab(tabs.newTab().setText("已完成"));
        tabs.addTab(tabs.newTab().setText("已取消"));

        tabs.addOnTabSelectedListener(new OnTabSelectedAdapter() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.setAdapter((mVM = findOrCreateViewModel()).getAdapter());
                isResume = false;
                selectTabStr = tab.getText().toString();
                mBinding.refreshRoot.progress.showLoading();
                mVM.start(getStatus(selectTabStr));
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && isResume && !TextUtils.isEmpty(selectTabStr)) {
            mBinding.refreshRoot.progress.showLoading();
            mVM.start(getStatus(selectTabStr));
        }
        if (hidden) {
            isResume = true;
        }
    }

    @Override
    protected void onLoadData() {
        mBinding.refreshRoot.progress.showLoading();
        mVM.start(getStatus(selectTabStr));
    }

    @OrderState
    private int getStatus(CharSequence text) {
        return "进行中".equals(text) ? OrderState.GOING : // @formatter:off
                "已完成".equals(text) ? OrderState.COMPLETED
                        : OrderState.CANCELED; // @formatter:on
    }

    @Override
    public void onShow(String title, String hint) {
        if (mDialog != null) {
            mDialog.setHint(hint).setTitle(title);
            mDialog.show();
        }
    }

    @Override
    public void onFirstLoadFinish() {
        mBinding.refreshRoot.progress.showContent();
    }

    @Override
    public void onRefreshFinish() {
        mBinding.refreshRoot.progress.showContent();
        mBinding.refreshRoot.refresh.finishRefresh();
        mBinding.refreshRoot.refresh.setEnableLoadmore(true);
    }

    @Override
    public void onLoadMoreFinish(boolean isLastPage) {
        mBinding.refreshRoot.refresh.finishLoadmore();
        mBinding.refreshRoot.refresh.setEnableLoadmore(!isLastPage);
    }

    @Override
    public void onLoadEmpty(String text) {
        mBinding.refreshRoot.progress.showEmpty(R.drawable.empty_order, text, "");
    }

    @Override
    public void onDestroyView() {
        if (mDialog != null) mDialog.dismiss();
        super.onDestroyView();
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }

    @Override
    public void toCall(String phone) {
        new RxPermissions(getActivity())
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(hasPermission -> {
                    if (hasPermission) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(phoneIntent);
                    } else {
                        showPermissionDialog("应用需要获取拨打电话权限,请前往应用信息-权限中开启");
                    }
                });
    }

    @Override
    public void toBondDevice() {
        startAct(ConnectPrinterActivity.class);
    }

    private void showPermissionDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("权限申请")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("暂不", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("去设置", (dialogInterface, which) -> {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                    dialogInterface.dismiss();
                })
                .show();
        dialog.setCanceledOnTouchOutside(false);
        Button negativeButton = dialog.getButton(BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray19));
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
        Button positiveButton = dialog.getButton(BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
    }
}
