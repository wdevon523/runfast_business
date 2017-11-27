package com.gxuc.runfast.business.ui.operation.statistics;

import android.content.Context;
import android.databinding.ObservableField;

public abstract class SearchViewModel {

    public final ObservableField<String> startTime = new ObservableField<>();
    public final ObservableField<String> endTime = new ObservableField<>();

    private DateTimeBottomSheet mStartTimePicker;
    private DateTimeBottomSheet mEndTimePicker;

    protected SearchViewModel(Context context) {
        mStartTimePicker = new DateTimeBottomSheet(context);
        mStartTimePicker.setCallback(startTime::set);

        mEndTimePicker = new DateTimeBottomSheet(context);
        mEndTimePicker.setCallback(endTime::set);
    }

    public abstract void search();

    public void showStartTimePicker() {
        mStartTimePicker.show();
    }

    public void showEndTimePicker() {
        mEndTimePicker.show();
    }

    public void onDestroy() {
        if (mStartTimePicker != null) mStartTimePicker.dismiss();
        if (mEndTimePicker != null) mEndTimePicker.dismiss();
    }
}
