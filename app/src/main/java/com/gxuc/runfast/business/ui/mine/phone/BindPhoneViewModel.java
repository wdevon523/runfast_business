package com.gxuc.runfast.business.ui.mine.phone;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.ui.base.BaseViewModel;

/**
 * 换绑手机
 * Created by Berial on 2017/9/1.
 */
public class BindPhoneViewModel extends BaseViewModel {

    public final ObservableBoolean hasSent = new ObservableBoolean(false);
    public final ObservableField<String> phone = new ObservableField<>();
    public final ObservableField<String> hiddenPhone = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();

    private BusinessRepo mRepo = BusinessRepo.get();

    BindPhoneViewModel(Context context) {
        super(context);
    }

    public void toDo() {
        if (hasSent.get()) bindPhone();
        else sendVerifyCode();
    }

    private void sendVerifyCode() {

    }

    private void bindPhone() {

    }
}
