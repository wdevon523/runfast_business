package com.gxuc.runfast.business.ui.operation.statistics.monthly;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;
import com.gxuc.runfast.business.widget.WheelMonthPicker;
import com.gxuc.runfast.business.widget.WheelYearPicker;

import java.util.Locale;

/**
 * 选择日期类型
 * Created by Berial on 2017/9/1.
 */
class DateBottomSheet extends BottomSheetDialog {

    private Callback mCallback;

    DateBottomSheet(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_monthly);
        onInitViews();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = Utils.getScreenWidth(getContext());
            window.setAttributes(lp);
        }
    }

    private void onInitViews() {
        final WheelYearPicker yearPicker = (WheelYearPicker) findViewById(R.id.wheel_year_view);
        final WheelMonthPicker monthPicker = (WheelMonthPicker) findViewById(R.id.wheel_month_view);

        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if (tvCancel != null) {
            tvCancel.setOnClickListener(view -> dismiss());
        }

        TextView tvComplete = (TextView) findViewById(R.id.tv_complete);
        if (tvComplete != null) {
            tvComplete.setOnClickListener(view -> {
                if (mCallback != null && yearPicker != null && monthPicker != null) {

                    int month = monthPicker.getCurrentMonth();

                    mCallback.onSelectedDate(String.format(Locale.CHINA,
                            "%d.%s",
                            yearPicker.getCurrentYear(),
                            month < 10 ? "0" + month : month
                    ));
                }
                dismiss();
            });
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onSelectedDate(String date);
    }
}
