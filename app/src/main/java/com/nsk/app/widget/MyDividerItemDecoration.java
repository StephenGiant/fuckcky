package com.nsk.app.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author qianpeng
 * @Package  com.nsk.cky.ckylibrary.widget
 * @date 2018/8/2.
 * @describe
 */
public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mOrientation;
    private static final int VERTICAL = LinearLayoutManager.VERTICAL;
    private static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    private int dleft = 0;
    private int dright = 0;
    private int dtop = 0;
    private int dbottom = 0;
    private final Rect mBounds = new Rect();

    public MyDividerItemDecoration(int orientation) {
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        //横向的先空着 不要用

    }


    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        //此处要-1，最后一个item不画线,且只有一个item的时候不画
        if (childCount > 1) {
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + dleft, top + dtop, right + dright, bottom + dbottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }


    /**
     * 设置分割线的图
     *
     * @param drawable
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    public Drawable getmDivider() {
        return mDivider;
    }

    public void setmDivider(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    public int getDleft() {
        return dleft;
    }

    public void setDleft(int dleft) {
        this.dleft = dleft;
    }

    public int getDright() {
        return dright;
    }

    public void setDright(int dright) {
        this.dright = dright;
    }

    public int getDtop() {
        return dtop;
    }

    public void setDtop(int dtop) {
        this.dtop = dtop;
    }

    public int getDbottom() {
        return dbottom;
    }

    public void setDbottom(int dbottom) {
        this.dbottom = dbottom;
    }
}
