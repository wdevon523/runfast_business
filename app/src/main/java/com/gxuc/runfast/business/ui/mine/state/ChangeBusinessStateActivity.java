package com.gxuc.runfast.business.ui.mine.state;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.event.ChangeBusinessStatusEvent;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.WithMenu;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import static com.gxuc.runfast.business.R.id.group;

/**
 * 设置营业状态
 * Created by Berial on 2017/8/20.
 */
public class ChangeBusinessStateActivity extends BaseActivity
        implements WithToolbar, WithMenu, LayoutProvider, StateNavigator {

    private StateViewModel mVM;

    private RadioGroup mRadioGroup;

    private ChangeBusinessStatusEvent mEvent = new ChangeBusinessStatusEvent();

    private Toolbar.OnMenuItemClickListener mListener = item -> {
        RadioButton rb = (RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId());
        mVM.changeBusinessState((String) rb.getTag());
        return true;
    };

    @Override
    public int thisLayoutId() {
        return R.layout.activity_change_business_state;
    }

    @Override
    public int thisMenu() {
        return R.menu.menu_change_bussiness_state;
    }

    @Override
    public Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener() {
        return mListener;
    }

    @Override
    public String thisTitle() {
        return "营业状态";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected StateViewModel thisViewModel() {
        return new StateViewModel(this, this);
    }

    @Override
    protected void onInitViews() {
        mRadioGroup = (RadioGroup) findViewById(group);
        mVM = findOrCreateViewModel();
    }

    @Override
    protected void onLoadData() {
        Intent intent = getIntent();
        int status = intent.getIntExtra("status", 0);
        mRadioGroup.check(status == 0 ? R.id.rb_state_business : R.id.rb_state_rest);
    }

    @Override
    public void onChangeStateSuccess() {
        RadioButton rb = (RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId());
        mEvent.status = Integer.parseInt((String) rb.getTag());
        mEvent.statusName = mEvent.status == 0 ? "营业" : "打烊";
        EventBus.getDefault().post(mEvent);
        finish();
    }
}
