package com.bandonleon.mvprealm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import com.bandonleon.mvprealm.R;

/**
 * Created by dom on 11/4/16.
 */

public class DividerItemDecoration extends ItemDecoration {

    private int mDividerPadding;
    private int mDividerHeight;
    private Paint mPaint;

    public DividerItemDecoration(@NonNull Context context) {
        mDividerPadding = context.getResources().getDimensionPixelOffset(R.dimen.item_divider_padding);
        mDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.item_divider_height);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) > 0) {
            outRect.top = mDividerHeight + (mDividerPadding * 2);
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, State state) {
        float dividerLeft = parent.getPaddingLeft();
        float dividerRight = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0, numDividers = parent.getChildCount() - 1; i < numDividers; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            float translationY = child.getTranslationY();   // Account for default animation
            float dividerTop = child.getBottom() + params.bottomMargin + mDividerPadding + translationY;
            float dividerBottom = dividerTop + mDividerHeight;
            canvas.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint);
        }
    }
}
