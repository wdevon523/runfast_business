package com.gxuc.runfast.business.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.gxuc.runfast.business.R;


/**
 * 两端对齐的TextView, 如:
 * ---------------------------------
 * |图片1 文字1            文字2 图片2|
 * ---------------------------------
 * Created by Berial on 15/10/22.
 */
public class JustifyTextView extends View {

    private static final String SUPER_STATE = "superState";
    private static final String LEFT_TEXT = "leftText";
    private static final String RIGHT_TEXT = "rightText";
    private static final String LEFT_TEXT_COLOR = "left_textColor";
    private static final String RIGHT_TEXT_COLOR = "right_textColor";
    private static final String LEFT_TEXT_SIZE = "leftTextSize";
    private static final String RIGHT_TEXT_SIZE = "rightTextSize";
    private static final String LEFT_ICON = "leftIcon";
    private static final String LEFT_ICON_PADDING = "leftIconPadding";
    private static final String RIGHT_ICON = "rightIcon";
    private static final String RIGHT_ICON_PADDING = "rightIconPadding";

    private CharSequence mLeftText, mRightText;
    private float mLeftTextSize, mRightTextSize;
    private int mLeftIconPadding, mRightIconPadding;
    private int mLeftTextColor, mRightTextColor;
    private int mLeftIconRes, mRightIconRes;
    private Drawable mLeftIcon, mRightIcon;
    private Resources mRes;
    private Paint mPaint;

    public JustifyTextView(Context context) {
        this(context, null, 0);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.JustifyTextView);
        readAttrs(t);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setClickable(true);
    }

    private void readAttrs(TypedArray t) {
        try {
            mLeftText = t.getText(R.styleable.JustifyTextView_leftText);
            mRightText = t.getText(R.styleable.JustifyTextView_rightText);

            mLeftIconRes = t.getResourceId(R.styleable.JustifyTextView_leftIcon, 0);
            mRightIconRes = t.getResourceId(R.styleable.JustifyTextView_rightIcon, 0);

            final int defaultColor = new TextView(getContext()).getCurrentTextColor();

            mLeftTextColor = t.getColor(R.styleable.JustifyTextView_leftTextColor, defaultColor);
            mRightTextColor = t.getColor(R.styleable.JustifyTextView_rightTextColor, defaultColor);

            mRes = getResources();
            final DisplayMetrics dm = mRes.getDisplayMetrics();
            final float d14 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, dm);

            mLeftTextSize = t.getDimension(R.styleable.JustifyTextView_leftTextSize, d14);
            mRightTextSize = t.getDimension(R.styleable.JustifyTextView_rightTextSize, d14);

            mLeftIconPadding = t.getDimensionPixelSize(R.styleable.JustifyTextView_leftIconPadding, 0);
            mRightIconPadding = t.getDimensionPixelSize(R.styleable.JustifyTextView_rightIconPadding, 0);
        } finally {
            t.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();

        int leftIconWidth = 0;
        int rightIconWidth = 0;

        if (mLeftIconRes != 0) {
            mLeftIcon = ContextCompat.getDrawable(getContext(), mLeftIconRes);
        }

        if (mLeftIcon != null) {
            leftIconWidth = mLeftIcon.getIntrinsicWidth();
            int h = mLeftIcon.getIntrinsicHeight();

            mLeftIcon.setBounds(leftPadding, getHeight() / 2 - h / 2, leftIconWidth + leftPadding,
                    getHeight() / 2 + h / 2);
            mLeftIcon.draw(canvas);
        }

        if (mRightIconRes != 0) {
            mRightIcon = ContextCompat.getDrawable(getContext(), mRightIconRes);
        }

        if (mRightIcon != null) {
            rightIconWidth = mRightIcon.getIntrinsicWidth();
            int h = mRightIcon.getIntrinsicHeight();

            mRightIcon.setBounds(getWidth() - rightIconWidth - rightPadding, getHeight() / 2 - h / 2,
                    getWidth() - rightPadding, getHeight() / 2 + h / 2);
            mRightIcon.draw(canvas);
        }

        if (!TextUtils.isEmpty(mLeftText)) {
            int left = leftPadding + mLeftIconPadding + leftIconWidth;

            mPaint.setColor(mLeftTextColor);
            mPaint.setTextSize(mLeftTextSize);
            mPaint.setTextAlign(Paint.Align.LEFT);
            Paint.FontMetrics fm = mPaint.getFontMetrics();

            float baseline = (getHeight() - fm.ascent - fm.descent) / 2;

            canvas.drawText(mLeftText, 0, mLeftText.length(), left, baseline, mPaint);
        }

        if (!TextUtils.isEmpty(mRightText)) {
            int right = rightPadding + mRightIconPadding + rightIconWidth;

            mPaint.setColor(mRightTextColor);
            mPaint.setTextSize(mRightTextSize);
            mPaint.setTextAlign(Paint.Align.RIGHT);
            Paint.FontMetrics fm = mPaint.getFontMetrics();

            float baseline = (getHeight() - fm.ascent - fm.descent) / 2;

            canvas.drawText(mRightText, 0, mRightText.length(), getWidth() - right, baseline, mPaint);
        }
    }

    public void setLeftText(CharSequence text) {
        mLeftText = text;
        invalidate();
    }

    public void setLeftText(int stringRes) {
        setLeftText(mRes.getString(stringRes));
    }

    public void setRightText(CharSequence text) {
        mRightText = text;
        invalidate();
    }

    public void setRightText(int stringRes) {
        setRightText(mRes.getString(stringRes));
    }

    /**
     * 设置左侧图标资源
     *
     * @param leftIconRes 图标资源id
     */
    public void setLeftIcon(@DrawableRes int leftIconRes) {
        mLeftIconRes = leftIconRes;
        invalidate();
    }

    public void setRightIcon(@DrawableRes int rightIconRes) {
        mRightIconRes = rightIconRes;
        invalidate();
    }

    public void setRightTextColor(@ColorRes int colorId) {
        mRightTextColor = ContextCompat.getColor(getContext(), colorId);
    }

    public void setRightTextColorValue(@ColorInt int color) {
        mRightTextColor = color;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE, superState);

        bundle.putCharSequence(LEFT_TEXT, mLeftText);
        bundle.putInt(LEFT_TEXT_COLOR, mLeftTextColor);
        bundle.putFloat(LEFT_TEXT_SIZE, mLeftTextSize);
        bundle.putInt(LEFT_ICON, mLeftIconRes);
        bundle.putInt(LEFT_ICON_PADDING, mLeftIconPadding);

        bundle.putCharSequence(RIGHT_TEXT, mRightText);
        bundle.putInt(RIGHT_TEXT_COLOR, mRightTextColor);
        bundle.putFloat(RIGHT_TEXT_SIZE, mRightTextSize);
        bundle.putInt(RIGHT_ICON, mRightIconRes);
        bundle.putInt(RIGHT_ICON_PADDING, mRightIconPadding);

        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            mLeftText = bundle.getString(LEFT_TEXT);
            mLeftIconRes = bundle.getInt(LEFT_ICON);
            mLeftIconPadding = bundle.getInt(LEFT_ICON_PADDING);
            mLeftTextSize = bundle.getFloat(LEFT_TEXT_SIZE);
            mLeftTextColor = bundle.getInt(LEFT_TEXT_COLOR);


            mRightText = bundle.getString(RIGHT_TEXT);
            mRightIconRes = bundle.getInt(RIGHT_ICON);
            mRightIconPadding = bundle.getInt(RIGHT_ICON_PADDING);
            mRightTextSize = bundle.getFloat(RIGHT_TEXT_SIZE);
            mRightTextColor = bundle.getInt(RIGHT_TEXT_COLOR);

            super.onRestoreInstanceState(bundle.getParcelable(SUPER_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
