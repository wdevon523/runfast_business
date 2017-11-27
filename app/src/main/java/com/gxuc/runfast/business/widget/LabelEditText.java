package com.gxuc.runfast.business.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.gxuc.runfast.business.R;


/**
 * 左侧有固定文字的 EditText
 * Created by Berial on 16/8/20.
 */
public class LabelEditText extends android.support.v7.widget.AppCompatEditText {

    private float mLabelPadding;
    private String mLabelText;
    private int mLabelTextColor;
    private float mLabelTextSize;

    private Paint mPaint;

    public LabelEditText(Context context) {
        this(context, null);
    }

    public LabelEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public LabelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float dp8 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);

        int defaultColor = new TextView(context).getCurrentTextColor();
        float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, dm);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelEditText);
        mLabelText = ta.getString(R.styleable.LabelEditText_labelText);
        mLabelPadding = ta.getDimension(R.styleable.LabelEditText_labelPadding, dp8);
        mLabelTextColor = ta.getColor(R.styleable.LabelEditText_labelTextColor, defaultColor);
        mLabelTextSize = ta.getDimension(R.styleable.LabelEditText_labelTextSize, defaultTextSize);
        ta.recycle();

        mPaint = getPaint();
    }

    public void setFixedText(String text) {
        mLabelText = text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!TextUtils.isEmpty(mLabelText)) {
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            float baseline = (getMeasuredHeight() - fm.ascent - fm.descent) / 2;
            mPaint.setColor(mLabelTextColor);
            mPaint.setTextSize(mLabelTextSize);
            canvas.drawText(mLabelText, getPaddingLeft(), baseline, mPaint);
        }
    }

    private int getLabelTextWidth() {
        int textWidth = 0;
        if (!TextUtils.isEmpty(mLabelText)) {
            TextPaint paint = getPaint();
            textWidth = (int) paint.measureText(mLabelText);
        }
        return textWidth;
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + getLabelTextWidth() + (int) mLabelPadding;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left + getLabelTextWidth() + (int) mLabelPadding, top, right, bottom);
    }
}
