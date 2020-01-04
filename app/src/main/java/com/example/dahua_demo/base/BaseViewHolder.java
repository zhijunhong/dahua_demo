package com.example.dahua_demo;

import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zyz on 16/12/2.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //view类型
    private int mViewType;
    //数据视图
    public static final int DATA_VIEW = 0;
    //底部视图
    public static final int FOOT_VIEW = -1;
    //头部视图
    public static final int HEAD_VIEW = -2;
    //viewhodler的点击事件监听
    private OnItemClickListener mOnItemClickListener;
    //子视图
    private SparseArray<View.OnClickListener> mSubClickListeners = new SparseArray<>();

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        mOnItemClickListener = listener;
        if(mOnItemClickListener != null) {
            itemView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        View.OnClickListener subOnClickListener = mSubClickListeners.get(v.getId());
        if(subOnClickListener == null) {
            //点击Item
            if(mViewType == HEAD_VIEW) {
                if(mOnItemClickListener != null) {
                    ((OnHeadViewClickListener)mOnItemClickListener).onHeadViewClick(itemView, v);
                }
            } else if(mViewType == FOOT_VIEW) {
                if(mOnItemClickListener != null) {
                    ((OnFootViewClickListener)mOnItemClickListener).onFootViewClick(itemView, v);
                }
            } else {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, getLayoutPosition());
                }
            }
        } else {
            //点击子view
            subOnClickListener.onClick(v);
        }
    }

    /**
     * 如果发生引用导致内存泄露，那就在宿主销毁之前重置下吧
     */
    public void resetAllListeners() {
        this.mOnItemClickListener = null;
        this.mSubClickListeners.clear();
    }

    /**
     * 为ItemView的子控件设置点击监听
     * @param resId
     * @param listener
     */
    public void setSubViewClickListener(int resId, View.OnClickListener listener) {
        View view = itemView.findViewById(resId);
        if(view != null) {
            view.setOnClickListener(listener);
            mSubClickListeners.put(resId, listener);
        }
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        if(mOnItemClickListener != null) {
            itemView.setOnClickListener(this);
        }
    }

    public interface OnFootViewClickListener extends OnItemClickListener{
        void onFootViewClick(View view, View clickView);
    }

    public interface OnHeadViewClickListener extends OnItemClickListener{
        void onHeadViewClick(View view, View clickView);
    }

}
