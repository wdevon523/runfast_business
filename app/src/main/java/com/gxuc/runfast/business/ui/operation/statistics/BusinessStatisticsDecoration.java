package com.gxuc.runfast.business.ui.operation.statistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;

public class BusinessStatisticsDecoration extends RecyclerView.ItemDecoration {

    private ColorDrawable mColorDrawable;
    private int mHeight;
    private int mPaddingLeft;
    private boolean mBreakFooter;

    public BusinessStatisticsDecoration(Context context) {
        mColorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.gray6));
        mHeight = (int) Utils.dp2px(context, 1);
        mPaddingLeft = (int) Utils.dp2px(context, 20);
    }

    public BusinessStatisticsDecoration(Context context, boolean breakFooter) {
        mColorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.gray6));
        mHeight = (int) Utils.dp2px(context, 1);
        mPaddingLeft = (int) Utils.dp2px(context, 20);
        mBreakFooter = breakFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position >= 0 && position < parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mHeight;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (parent.getAdapter() == null) {
            return;
        }

        int start = parent.getPaddingLeft();
        int end = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        if (childCount == 1) {
            View child = parent.getChildAt(0);
            child.setBackgroundResource(R.drawable.shape_bg_message_item);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (i == 0) {
                child.setBackgroundResource(R.drawable.shape_bg_header_day_order);
            }
            if (position == childCount - 1) continue;
            if (position == childCount - 2 && mBreakFooter) {
                child.setBackgroundResource(R.drawable.shape_bg_footer_day_order);
                continue;
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mHeight;
            mColorDrawable.setBounds(position == 0 ? start : mPaddingLeft + start, top, end, bottom);
            mColorDrawable.draw(c);
        }
    }
}
