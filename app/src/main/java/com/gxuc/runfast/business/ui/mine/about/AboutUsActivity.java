package com.gxuc.runfast.business.ui.mine.about;

import android.support.v7.app.AppCompatActivity;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.WithToolbar;

/**
 * 关于我们
 * Created by Berial on 2017/8/20.
 */
public class AboutUsActivity extends AppCompatActivity implements LayoutProvider, WithToolbar {

    @Override
    public int thisLayoutId() {
        return R.layout.activity_about_us_new;
    }


    @Override
    public String thisTitle() {
        return "关于我们";
    }
}
