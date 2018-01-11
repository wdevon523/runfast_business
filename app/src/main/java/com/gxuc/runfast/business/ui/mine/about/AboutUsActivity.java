package com.gxuc.runfast.business.ui.mine.about;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.databinding.ActivityAboutUsNewBinding;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.NeedDataBinding;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.util.MyAutoUpdate;

/**
 * 关于我们
 * Created by Berial on 2017/8/20.
 */
public class AboutUsActivity extends BaseActivity implements LayoutProvider, WithToolbar, NeedDataBinding<ActivityAboutUsNewBinding>, AboutUsNavigator {

    private AboutUsViewModel mVM;
    private ActivityAboutUsNewBinding mBinding;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_about_us_new;
    }

    @Override
    public String thisTitle() {
        return "关于";
    }

    @Override
    protected AboutUsViewModel thisViewModel() {
        return new AboutUsViewModel(this, this);
    }

    @Override
    public void onBoundDataBinding(ActivityAboutUsNewBinding binding) {
        mBinding = binding;
        binding.setView(this);
        binding.setViewModel(mVM = findOrCreateViewModel());
    }

    @Override
    protected void onInitViews() {
        try {
            String versionName = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mVM.versionName.set(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateApp(String msg, String downloadUrl) {
        showNoticeDialog(msg, downloadUrl);
    }

    /**
     * 显示更新对话框
     *
     * @param version_info
     */
    private void showNoticeDialog(String version_info, String downloadUrl) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示");
        builder.setMessage(version_info);
        // 更新
        builder.setPositiveButton("立即更新", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            MyAutoUpdate autoUpdate = new MyAutoUpdate(this);
            autoUpdate.showDownloadDialog(downloadUrl);


            // 启动后台服务下载apk
        });
        // 稍后更新
        builder.setNegativeButton("以后更新", (dialogInterface, i) ->
                dialogInterface.dismiss()
        );


        AlertDialog noticeDialog = builder.create();
        noticeDialog.show();

        Button nButton = noticeDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        nButton.setTypeface(Typeface.DEFAULT_BOLD);
        Button pButton = noticeDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        pButton.setTypeface(Typeface.DEFAULT_BOLD);
    }
}
