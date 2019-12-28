package com.example.dahua_demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclViewWrap extends RecyclerView {
    public RecyclViewWrap(@NonNull Context context) {
        this(context, null);
    }

    public RecyclViewWrap(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclViewWrap(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOnActionDownListener.onActionDown();
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    public void setOnActionDownListener(OnActionDownListener onActionDownListener) {
        mOnActionDownListener = onActionDownListener;
    }

    private OnActionDownListener mOnActionDownListener;

    public interface OnActionDownListener {
        void onActionDown();
    }

    /**
     * 还能向下滑动多少
     */
    public int getDownDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibItem = getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recycleViewHeight = getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (itemCount - firstItemPosition - 1) * itemHeight - recycleViewHeight + firstItemBottom;
    }

    /**
     * 已滑动的距离
     */
    public int getUpDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibItem = getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recycleViewHeight = getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }

    /**
     * 获取RecyclerView滚动距离
     */
    public int getDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        View firstVisibItem = getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();
        int recycleViewHeight = getHeight();
        int itemHeight = firstVisibItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibItem);
        return (itemCount - firstItemPosition - 1) * itemHeight - recycleViewHeight;
    }

}
