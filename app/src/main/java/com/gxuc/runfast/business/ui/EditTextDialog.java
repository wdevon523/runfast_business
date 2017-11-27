package com.gxuc.runfast.business.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;


/**
 * 带一个EditText的对话框
 * Created by Berial on 2016/11/12.
 */

public class EditTextDialog extends AlertDialog {

    private EditText mEditText;

    private String mDialogTitle;
    private String mDialogHintText;

    private EditTextDialog(Context context, String title, String hint) {
        super(context);
        mDialogTitle = title;
        mDialogHintText = hint;
        init(context);
    }

    private void init(Context context) {
        mEditText = new EditText(context);
        setContentView(mEditText);

        setTitle(mDialogTitle);

        mEditText.setHint(mDialogHintText);
        mEditText.setHintTextColor(ContextCompat.getColor(context, R.color.iron));
        mEditText.setTextColor(ContextCompat.getColor(context, R.color.mine_shaft));

        int margin = (int) Utils.dp2px(context, 20);
        int marginTop = (int) Utils.dp2px(context, 10);
        setView(mEditText, margin, marginTop, margin, 0);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        setNegativeButton("取消");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button negativeButton = getButton(BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.gray19));
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
        Button positiveButton = getButton(BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
    }

    public static EditTextDialog create(Context context) {
        return create(context, "", "");
    }

    public static EditTextDialog create(Context context, String title, String hint) {
        EditTextDialog dialog = new EditTextDialog(context, title, hint);
        dialog.mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        return dialog;
    }

    public EditTextDialog setPositiveButton(String text, final Callback listener) {
        setButton(AlertDialog.BUTTON_POSITIVE, text, (dialog, which) -> {
            if (listener != null) listener.call(mEditText.getText().toString());
            hide();
        });
        return this;
    }

    private EditTextDialog setNegativeButton(String text) {
        setButton(AlertDialog.BUTTON_NEGATIVE, text, (dialog, which) -> hide());
        return this;
    }

    public EditTextDialog setHint(String hint) {
        mEditText.setHint(hint);
        return this;
    }

    public EditTextDialog setText(String text) {
        mEditText.setText(text);
        mEditText.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
        return this;
    }

    @Override
    public void hide() {
        mEditText.setText("");
        super.hide();
    }

    public interface Callback {
        void call(String text);
    }
}
