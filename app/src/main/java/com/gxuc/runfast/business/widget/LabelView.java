package com.gxuc.runfast.business.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;

import com.gxuc.runfast.business.R;


/**
 * Created by drakeet(http://drakeet.me)
 * Date: 15/10/25 19:25
 */
public class LabelView extends android.support.v7.widget.AppCompatTextView {

    private CharSequence mLeftText, mTopText, mRightText, mBottomText;
    private int mLeftTextAppearance, mTopTextAppearance, mRightTextAppearance,
            mBottomTextAppearance;
    private CharSequence mText;


    public LabelView(Context context) {
        this(context, null);
    }


    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelView);
        mLeftText = a.getText(R.styleable.LabelView_leftText);
        mTopText = a.getText(R.styleable.LabelView_topText);
        mRightText = a.getText(R.styleable.LabelView_rightText);
        mBottomText = a.getText(R.styleable.LabelView_bottomText);

        final int defaultAppearance = R.style.LabelView_14dp_gray19;

        mLeftTextAppearance = a.getResourceId(R.styleable.LabelView_leftTextAppearance, defaultAppearance);
        mTopTextAppearance = a.getResourceId(R.styleable.LabelView_topTextAppearance, defaultAppearance);
        mRightTextAppearance = a.getResourceId(R.styleable.LabelView_rightTextAppearance, defaultAppearance);
        mBottomTextAppearance = a.getResourceId(R.styleable.LabelView_bottomTextAppearance, defaultAppearance);

        a.recycle();

        setText(super.getText());
    }


    @Override
    public void setText(CharSequence mainText, BufferType type) {
        super.setText(mainText, type);
        mText = mainText;
        CharSequence text = mainText;
        if (!TextUtils.isEmpty(mLeftText)) {
            text = buildTextLeft(mLeftText.toString(), text, mLeftTextAppearance);
        }
        if (!TextUtils.isEmpty(mRightText)) {
            text = buildTextRight(text, mRightText.toString(), mRightTextAppearance);
        }
        if (!TextUtils.isEmpty(mTopText)) {
            text = new SpannableStringBuilder("\n").append(text);
            text = buildTextLeft(mTopText.toString(), text, mTopTextAppearance);
        }
        if (!TextUtils.isEmpty(mBottomText)) {
            text = new SpannableStringBuilder(text).append("\n");
            text = buildTextRight(text, mBottomText.toString(), mBottomTextAppearance);
        }
        if (!TextUtils.isEmpty(text)) {
            super.setText(text, type);
        }
    }

    private CharSequence buildTextLeft(CharSequence head, CharSequence foot, int style) {
        SpannableString leftText = format(getContext(), head, style);
        SpannableStringBuilder builder = new SpannableStringBuilder(leftText).append(foot);
        return builder.subSequence(0, builder.length());
    }


    private CharSequence buildTextRight(CharSequence head, CharSequence foot, int style) {
        SpannableString rightText = format(getContext(), foot, style);
        SpannableStringBuilder builder = new SpannableStringBuilder(head).append(rightText);
        return builder.subSequence(0, builder.length());
    }


    public SpannableString format(Context context, CharSequence text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(), 0);
        return spannableString;
    }

    @Override
    public CharSequence getText() {
        return mText;
    }

    public void setLeftTextAppearance(@StyleRes int textAppearance) {
        mLeftTextAppearance = textAppearance;
    }

    public void setLeftText(String text) {
        mLeftText = text;
        setText(getText());
    }

    public void setTopTextAppearance(@StyleRes int textAppearance) {
        mTopTextAppearance = textAppearance;
    }

    public void setTopText(String text) {
        mTopText = text;
        setText(getText());
    }

    public void setBottomTextAppearance(@StyleRes int textAppearance) {
        mBottomTextAppearance = textAppearance;
    }

    public void setBottomText(String text) {
        mBottomText = text;
        setText(getText());
    }

    public void setRightTextAppearance(@StyleRes int textAppearance) {
        mRightTextAppearance = textAppearance;
    }

    public void setRightText(String text) {
        mRightText = text;
        setText(getText());
    }
}
