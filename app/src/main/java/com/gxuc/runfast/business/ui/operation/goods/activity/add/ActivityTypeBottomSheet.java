package com.gxuc.runfast.business.ui.operation.goods.activity.add;

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

import java.util.ArrayList;

/**
 * 选择活动类型
 * Created by Berial on 2017/9/1.
 */
class ActivityTypeBottomSheet extends BottomSheetDialog {

    private ArrayList<String> items = new ArrayList<>();
    private Callback mCallback;

    {
        items.add("满减活动");
        items.add("打折活动");
        items.add("赠品活动");
        items.add("特价活动");
        items.add("满减免运费");
    }

    ActivityTypeBottomSheet(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet_activity_type);
        onInitViews();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = Utils.getScreenWidth(getContext());
            window.setAttributes(lp);
        }
    }

    private void onInitViews() {
        final WheelPicker picker = (WheelPicker) findViewById(R.id.wheel_view);
        if (picker != null) {
            picker.setData(items);
        }

        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if (tvCancel != null) {
            tvCancel.setOnClickListener(view -> dismiss());
        }

        TextView tvComplete = (TextView) findViewById(R.id.tv_complete);
        if (tvComplete != null) {
            tvComplete.setOnClickListener(view -> {
                if (mCallback != null && picker != null) {
                    int position = picker.getCurrentItemPosition();
                    mCallback.onActivityType(position + 1, items.get(position % items.size()));
                }
                dismiss();
            });
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onActivityType(int type, String typeName);
    }
}
