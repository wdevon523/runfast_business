package com.gxuc.runfast.business.ui.mine.password;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityChangePasswordBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;

/**
 * 修改密码
 * Created by Berial on 2017/8/20.
 */
public class ChangePasswordActivity extends BaseActivity
        implements LayoutProvider, WithToolbar, ChangePasswordNavigator, ProgressHelper.Callback,
        NeedDataBinding<ActivityChangePasswordBinding> {

    private ProgressHelper helper;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public String thisTitle() {
        return "修改密码";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ChangePasswordViewModel thisViewModel() {
        return new ChangePasswordViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityChangePasswordBinding binding) {
        binding.setViewModel(findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
    }

    @Override
    public void onChangePasswordSuccess() {
        onBackPressed();
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }
}
