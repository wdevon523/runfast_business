package com.gxuc.runfast.business.ui.operation.goods;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.util.Utils;

public class GoodsSortDecoration extends RecyclerView.ItemDecoration {

    private ColorDrawable mColorDrawable;
    private int mHeight;
    private int mPadding;

    public GoodsSortDecoration(Context context) {
        mColorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.gray6));
        mHeight = (int) Utils.dp2px(context, 1);
        mPadding = (int) Utils.dp2px(context, 8);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position >= 0 && position < parent.getAdapter().getItemCount()) {
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
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position == childCount - 1) continue;

            if (child.isSelected()) continue;

            if (parent.getChildAt(i + 1) != null && parent.getChildAt(i + 1).isSelected()) continue;

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mHeight;
            mColorDrawable.setBounds(position == 0 ? start : mPadding + start, top, mPadding + end, bottom);
            mColorDrawable.draw(c);
        }
    }
}
