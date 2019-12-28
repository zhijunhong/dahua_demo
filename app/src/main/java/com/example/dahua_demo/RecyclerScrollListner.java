package com.example.dahua_demo;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zyz on 16/12/2.
 *
 * recyclerview的滑动监听器，用来监听列表滑到最底部时触发上拉刷新
 */
public abstract class RecyclerScrollListner extends RecyclerView.OnScrollListener {
    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;
    private int visitCount;
    private int itemCount;
    private boolean isRefresh;
    private boolean isLoading;

    public RecyclerScrollListner() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        itemCount = recyclerView.getLayoutManager().getItemCount();
        visitCount = recyclerView.getLayoutManager().getChildCount();
        if (!isRefresh && firstVisibleItemPosition == 0) {
            isRefresh = true;
            refresh();
        } else {
            if (!isLoading && visitCount > 0 && lastVisibleItemPosition == itemCount - 1) {
                isLoading = true;
                loadMore();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        }
        if (layoutManager.getClass() == GridLayoutManager.class){
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        }
    }

    public int findMinValue(int[] values) {

        int minVal = values[0];
        for (Integer value : values) {
            if (minVal > value)
                minVal = value;
        }

        return minVal;
    }

    public int findMaxValue(int[] values) {

        int maxVal = values[0];
        for (Integer value : values) {
            if (maxVal < value)
                maxVal = value;
        }

        return maxVal;
    }

    public String BuildIntArraysToStr(int[] values) {
        StringBuffer buffer = new StringBuffer("[");
        for (Integer value : values) {
            buffer.append(value + " , ");
        }

        return buffer.deleteCharAt(buffer.length()-1).append("]").toString();
    }

    public abstract void loadMore();

    public abstract void refresh();

    public boolean isRefresh() {
        return isRefresh;
    }

    public void finished() {
        this.isRefresh = false;
        this.isLoading = false;
    }

    public void refreshFinished() {
        this.isRefresh = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadFinished() {
        this.isLoading = false;
    }
}
