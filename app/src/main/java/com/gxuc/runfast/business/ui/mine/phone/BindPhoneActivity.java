package com.gxuc.runfast.business.ui.mine.phone;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityBindPhoneBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;

/**
 * 换绑手机
 * Created by Berial on 2017/8/20.
 */
public class BindPhoneActivity extends BaseActivity
        implements LayoutProvider, WithToolbar, NeedDataBinding<ActivityBindPhoneBinding> {

    @Override
    public int thisLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public String thisTitle() {
        return "换绑手机";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BindPhoneViewModel thisViewModel() {
        return new BindPhoneViewModel(this);
    }

    @Override
    public void onBoundDataBinding(ActivityBindPhoneBinding binding) {
        binding.setViewModel(findOrCreateViewModel());
    }
}
