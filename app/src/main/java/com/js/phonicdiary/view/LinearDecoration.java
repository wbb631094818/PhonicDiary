package com.js.phonicdiary.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 *  自定义RecyclerView分割线
 */
public class LinearDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int bottom;
    private int left;
    private int right;
    private boolean mIsNeedLastDecoration = true;
    private int hidePosition = -1;
    private Paint mPaint;

    public LinearDecoration setNeedLastDecoration(boolean needLastDecoration) {
        mIsNeedLastDecoration = needLastDecoration;
        return this;
    }

    public void hideDecoration(int position) {
        hidePosition = position;
    }

    public LinearDecoration(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public LinearDecoration(int top, int bottom, int left, int right, int color) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        if (hidePosition >= 0 && parent.getChildAdapterPosition(view) == hidePosition) {
            outRect.bottom = 0;
        } else if (!mIsNeedLastDecoration && parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {// 最后一行
            outRect.bottom = 0;
        } else {
            outRect.bottom = bottom;
        }
        outRect.left = left;
        outRect.right = right;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPaint == null) {
            super.onDraw(c, parent, state);
        } else {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                c.drawRect(parent.getPaddingLeft(), view.getBottom(),
                        parent.getWidth() - parent.getPaddingRight(), view.getBottom() + (bottom - top), mPaint);
            }
        }
    }
}
