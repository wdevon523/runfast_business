package com.gxuc.runfast.business.ui.login;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.bean.User;
import com.gxuc.runfast.business.databinding.ActivityLoginBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.ui.MainActivity;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.ProgressHelper;

import io.paperdb.Paper;

/**
 * 登录
 * Created by Berial on 2017/8/24.
 */
public class LoginActivity extends BaseActivity
        implements LayoutProvider, NeedDataBinding<ActivityLoginBinding>,
        LoginNavigator, ProgressHelper.Callback {

    private ProgressHelper helper;
    private LoginViewModel mVM;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected LoginViewModel thisViewModel() {
        return new LoginViewModel(this, this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityLoginBinding binding) {
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        helper = new ProgressHelper(this);
//        User user = Paper.book().read("user");
//        if (user != null) {
//          mVM.username.set( user.accounts);
//        }
    }

    @Override
    public void onLoginSuccess() {
        startAct(MainActivity.class);
        onBackPressed();
    }

    @Override
    public void setLoading(boolean loading) {
        if (loading) helper.show();
        else helper.dismiss();
    }
}
