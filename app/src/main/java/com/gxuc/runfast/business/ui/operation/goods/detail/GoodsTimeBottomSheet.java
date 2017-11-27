package com.gxuc.runfast.business.ui.operation.goods.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * 选择日期类型
 * Created by Berial on 2017/9/1.
 */
class GoodsTimeBottomSheet extends BottomSheetDialog {

    private Callback mCallback;

    GoodsTimeBottomSheet(@NonNull Context context) {
        super(context);
    }

    public void show(String time) {
        show();

        if (TextUtils.isEmpty(time)) return;

        String[] times = time.split(":");
        final WheelPicker hourPicker = (WheelPicker) findViewById(R.id.wheel_hour_view);
        final WheelPicker minutePicker = (WheelPicker) findViewById(R.id.wheel_minute_view);

        if (hourPicker == null || minutePicker == null) return;

        hourPicker.setSelectedItemPosition(Integer.valueOf(times[0]));
        minutePicker.setSelectedItemPosition(Integer.valueOf(times[1]));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_shop_time);
        onInitViews();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = Utils.getScreenWidth(getContext());
            window.setAttributes(lp);
        }
    }

    private void onInitViews() {
        final WheelPicker hourPicker = (WheelPicker) findViewById(R.id.wheel_hour_view);
        final WheelPicker minutePicker = (WheelPicker) findViewById(R.id.wheel_minute_view);

        setHours(hourPicker);
        setMinutes(minutePicker);
        setCurrentTime(hourPicker, minutePicker);

        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if (tvCancel != null) {
            tvCancel.setOnClickListener(view -> dismiss());
        }

        TextView tvComplete = (TextView) findViewById(R.id.tv_complete);
        if (tvComplete != null) {
            tvComplete.setOnClickListener(view -> {
                if (mCallback != null && hourPicker != null && minutePicker != null) {

                    int hour = hourPicker.getCurrentItemPosition();
                    int minute = minutePicker.getCurrentItemPosition();

                    mCallback.onSelectedDate(String.format(Locale.CHINA,
                            "%s:%s",
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
