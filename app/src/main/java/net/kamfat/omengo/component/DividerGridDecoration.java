package net.kamfat.omengo.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by cjx on 2016/12/21.
 * recyclerview 的分隔线
 */

public class DividerGridDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int columCount;

    public DividerGridDecoration(Context context, int columCount) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        this.columCount = columCount;
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            drawHorizontal(c, child, params);
            drawVertical(c, child, params);
        }
    }

    private void drawHorizontal(Canvas c, View child, RecyclerView.LayoutParams params) {
        final int left = child.getLeft() - params.leftMargin;
        final int right = child.getRight() + params.rightMargin
                + mDivider.getIntrinsicWidth();
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + mDivider.getIntrinsicHeight();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawVertical(Canvas c, View child, RecyclerView.LayoutParams params) {

        final int top = child.getTop() - params.topMargin;
        final int bottom = child.getBottom() + params.bottomMargin;
        final int left = child.getRight() + params.rightMargin;
        final int right = left + mDivider.getIntrinsicWidth();

        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    // 是否最后一列
    private boolean isLastColum(int itemPosition) {
        return itemPosition % columCount == columCount - 1;
    }

    // 是否最后一行
    private boolean isLastLine(int itemPosition, int childCount) {
        return itemPosition >= childCount - childCount % columCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int childCount = parent.getAdapter().getItemCount();
        if (!isLastColum(itemPosition)) {
            outRect.right = mDivider.getIntrinsicHeight();
        }
        if (!isLastLine(itemPosition, childCount)) {
            outRect.bottom = mDivider.getIntrinsicHeight();
        }
    }

}
