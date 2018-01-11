package com.gxuc.runfast.business.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;
import com.gxuc.runfast.business.widget.WheelDayPicker;
import com.gxuc.runfast.business.widget.WheelMonthPicker;
import com.gxuc.runfast.business.widget.WheelYearPicker;

import java.util.Locale;

/**
 * 选择日期类型
 * Created by Berial on 2017/9/1.
 */
public class DateBottomSheet extends BottomSheetDialog {

    private Callback mCallback;

    public DateBottomSheet(@NonNull Context context) {
        super(context);
    }

    public void show(String time) {
        show();

        if (TextUtils.isEmpty(time)) return;

//        String[] times = time.split("-");
        String[] times = time.split("\\.");
        final WheelYearPicker yearPicker = (WheelYearPicker) findViewById(R.id.wheel_year_view);
        final WheelMonthPicker monthPicker = (WheelMonthPicker) findViewById(R.id.wheel_month_view);
        final WheelDayPicker dayPicker = (WheelDayPicker) findViewById(R.id.wheel_day_view);

        if (yearPicker == null || monthPicker == null || dayPicker == null) return;

        yearPicker.setSelectedYear(Integer.valueOf(times[0]));
        monthPicker.setSelectedMonth(Integer.valueOf(times[1]));
        dayPicker.setSelectedDay(Integer.valueOf(times[2]));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_date);
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
        final WheelDayPicker dayPicker = (WheelDayPicker) findViewById(R.id.wheel_day_view);
        if (yearPicker != null && dayPicker != null) {
            yearPicker.setOnItemSelectedListener((picker, year, position) ->
                    dayPicker.setYear(Integer.parseInt(((String) year).replace("年", ""))));
        }
        if (monthPicker != null && dayPicker != null) {
            monthPicker.setOnItemSelectedListener((picker, month, position) ->
                    dayPicker.setMonth(Integer.parseInt(((String) month).replace("月", ""))));
        }

        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if (tvCancel != null) {
            tvCancel.setOnClickListener(view -> dismiss());
        }

        TextView tvComplete = (TextView) findViewById(R.id.tv_complete);
        if (tvComplete != null) {
            tvComplete.setOnClickListener(view -> {
                if (mCallback != null && yearPicker != null && monthPicker != null && dayPicker != null) {
                    int month = monthPicker.getCurrentMonth();
                    int day = dayPicker.getCurrentDay();

                    mCallback.onSelectedDate(String.format(Locale.CHINA,
                            "%d.%s.%s",
                            yearPicker.getCurrentYear(),
                            month < 10 ? "0" + month : month,
                            day < 10 ? "0" + day : day
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
