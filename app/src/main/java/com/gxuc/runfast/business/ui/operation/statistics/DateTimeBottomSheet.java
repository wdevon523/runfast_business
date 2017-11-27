package com.gxuc.runfast.business.ui.operation.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;
import com.gxuc.runfast.business.widget.WheelDayPicker;
import com.gxuc.runfast.business.widget.WheelMonthPicker;
import com.gxuc.runfast.business.widget.WheelYearPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * 选择日期类型
 * Created by Berial on 2017/9/1.
 */
class DateTimeBottomSheet extends BottomSheetDialog {

    private Callback mCallback;

    DateTimeBottomSheet(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_time);
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
        final WheelPicker hourPicker = (WheelPicker) findViewById(R.id.wheel_hour_view);
        final WheelPicker minutePicker = (WheelPicker) findViewById(R.id.wheel_minute_view);

        setHours(hourPicker);
        setMinutes(minutePicker);
        setCurrentTime(hourPicker, minutePicker);

        if (yearPicker != null && dayPicker != null) {
            yearPicker.setOnItemSelectedListener((picker, year, position) ->
                    dayPicker.setYear(yearPicker.getCurrentYear()));
        }
        if (monthPicker != null && dayPicker != null) {
            monthPicker.setOnItemSelectedListener((picker, month, position) ->
                    dayPicker.setMonth(monthPicker.getCurrentMonth()));
        }

        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if (tvCancel != null) {
            tvCancel.setOnClickListener(view -> dismiss());
        }

        TextView tvComplete = (TextView) findViewById(R.id.tv_complete);
        if (tvComplete != null) {
            tvComplete.setOnClickListener(view -> {
                if (mCallback != null && yearPicker != null && monthPicker != null && dayPicker != null
                        && hourPicker != null && minutePicker != null) {

                    int month = monthPicker.getCurrentMonth();
                    int day = dayPicker.getCurrentDay();
                    int hour = hourPicker.getCurrentItemPosition();
                    int minute = minutePicker.getCurrentItemPosition();

                    mCallback.onSelectedDate(String.format(Locale.CHINA,
                            "%d.%s.%s\n%s:%s",
                            yearPicker.getCurrentYear(),
                            month < 10 ? "0" + month : month,
                            day < 10 ? "0" + day : day,
                            hour < 10 ? "0" + hour : hour,
                            minute < 10 ? "0" + minute : minute
                    ));
                }
                dismiss();
            });
        }
    }

    private void setHours(WheelPicker picker) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(i + "时");
        }
        picker.setData(list);
    }

    private void setMinutes(WheelPicker picker) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(i + "分");
        }
        picker.setData(list);
    }

    private void setCurrentTime(WheelPicker hour, WheelPicker minute) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        hour.setSelectedItemPosition(calendar.get(Calendar.HOUR_OF_DAY));
        minute.setSelectedItemPosition(calendar.get(Calendar.MINUTE));
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onSelectedDate(String date);
    }
}
