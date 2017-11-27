package com.gxuc.runfast.business.ui.operation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.FragmentOperationBinding;
import com.gxuc.runfast.business.ui.base.BaseFragment;
import com.gxuc.runfast.business.ui.operation.cash.CashApplicationActivity;
import com.gxuc.runfast.business.ui.operation.comment.UserCommentActivity;
import com.gxuc.runfast.business.ui.operation.goods.GoodsManageActivity;
import com.gxuc.runfast.business.ui.operation.message.MessageCenterActivity;
import com.gxuc.runfast.business.ui.operation.statistics.BusinessStatisticsActivity;

/**
 * 门店运营
 * Created by Berial on 2017/8/19.
 */
public class OperationFragment extends BaseFragment<FragmentOperationBinding> {

    private OperationViewModel mVM;

    @Override
    public int thisLayoutId() {
        return R.layout.fragment_operation;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected OperationViewModel thisViewModel() {
        return new OperationViewModel(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setView(this);
        mBinding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    public void onResume() {
        super.onResume();
        mVM.start();
    }

    public void toGoodsManage() {
        startAct(GoodsManageActivity.class);
    }

    public void toBusinessStatistics() {
        startAct(BusinessStatisticsActivity.class);
    }

    public void toCashApplication() {
        startAct(CashApplicationActivity.class);
    }

    public void toUserEvaluate() {
        startAct(UserCommentActivity.class);
    }

    public void toMessageCenter() {
        startAct(MessageCenterActivity.class);
    }
}
