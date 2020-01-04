package com.example.dahua_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyz on 16/12/2.
 */
public abstract class BaseSingleTypeAdapter<T, H extends BaseViewHolder> extends BaseAdapter<T> {

    private int mLayoutId;

    public BaseSingleTypeAdapter(int layoutId) {
        this.mLayoutId = layoutId;
    }

    @Override
    public int getDataItemViewType(int position) {
        return BaseViewHolder.DATA_VIEW;
    }

    @Override
    public BaseViewHolder createHeaderViewHolder(View headView) {
        return buildHeadViewHolder(headView);
    }

    @Override
    public BaseViewHolder createViewTypeHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return buildViewHolder(view);
    }

    @Override
    public void bindDataToViewHolder(BaseViewHolder holder, int position, int viewType) {
        bindDataToViewHolder((H)holder, mData.get(position), position);
    }

    public abstract H buildViewHolder(View itemView);

    public SimpleRecyViewHolder buildHeadViewHolder(View itemview) {
        return new SimpleRecyViewHolder(itemview);
    }

    public abstract void bindDataToViewHolder(H holder,T t,int position);

}
