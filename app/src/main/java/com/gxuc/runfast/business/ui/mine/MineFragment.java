package com.gxuc.runfast.business.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.FragmentMineBinding;
import com.gxuc.runfast.business.event.ChangeBusinessStatusEvent;
import com.gxuc.runfast.business.event.UpdateShopInfoSuccessEvent;
import com.gxuc.runfast.business.ui.EditTextDialog;
import com.gxuc.runfast.business.ui.base.BaseFragment;
import com.gxuc.runfast.business.ui.login.LoginActivity;
import com.gxuc.runfast.business.ui.mine.about.AboutUsActivity;
import com.gxuc.runfast.business.ui.mine.archive.ArchiveActivity;
import com.gxuc.runfast.business.ui.mine.feedback.FeedbackActivity;
import com.gxuc.runfast.business.ui.mine.password.ChangePasswordActivity;
import com.gxuc.runfast.business.ui.mine.phone.BindPhoneActivity;
import com.gxuc.runfast.business.ui.mine.printer.ConnectPrinterActivity;
import com.gxuc.runfast.business.ui.mine.shop.ShopInfoActivity;
import com.gxuc.runfast.business.ui.mine.state.ChangeBusinessStateActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 我的
 * Created by Berial on 2017/8/19.
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> implements MineNavigator {

    private MineViewModel mVM;

    private EditTextDialog mDialog;

    @Override
    public int thisLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MineViewModel thisViewModel() {
        return new MineViewModel(getActivity(), this);
    }

    @Override
    protected void onBoundBinding(FragmentMineBinding binding) {
        mBinding.setView(this);
        mBinding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        mDialog = EditTextDialog.create(getActivity(), "修改名称", "请输入商店名称")
                .setPositiveButton("确定", text -> mVM.changeShopName(text));
    }

//    @Override
//    protected void onLoadData() {
//        mVM.start();
//    }

    @Subscribe
    public void changeBusinessState(ChangeBusinessStatusEvent event) {
        mVM.status.set(event.status);
        mVM.statusName.set(event.statusName);
    }

    @Subscribe
    public void updateShopInfoSuccess(UpdateShopInfoSuccessEvent event) {
        mVM.start();
    }

    public void showChangeShopNameDialog() {
        mDialog.setText(mVM.name.get());
        mDialog.show();
    }

    public void toChangeBusinessState() {
        Bundle bundle = new Bundle();
        bundle.putInt("status", mVM.status.get());
        startAct(ChangeBusinessStateActivity.class, bundle);
    }

    public void toChangePassword() {
        startAct(ChangePasswordActivity.class);
    }

    public void toBindPhone() {
        startAct(BindPhoneActivity.class);
    }

    public void toViewShopInfo() {
        startAct(ShopInfoActivity.class);
    }

    public void toFeedback() {
        startAct(FeedbackActivity.class);
    }

    public void toAboutUs() {
        startAct(AboutUsActivity.class);
    }

    public void toConnectPrinter() {
        startAct(ConnectPrinterActivity.class);
    }

    public void toArchive() {
        startAct(ArchiveActivity.class);
    }

    @Override
    public void onLogoutSuccess() {
        startAct(LoginActivity.class);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mVM.start();
        }
    }
}
