package com.example.dahua_demo;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyz on 16/11/19.
 * <p>
 * recyclerview 的基类适配器
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private BaseViewHolder.OnItemClickListener mOnItemClickListener;
    private BaseViewHolder.OnHeadViewClickListener mOnHeadViewClickListener;
    private BaseViewHolder.OnFootViewClickListener mOnFootViewClickListener;
    //子视图id点击事件
    public SparseArray<View.OnClickListener> mSubDataViewListeners = new SparseArray<>(0);
    public SparseArray<View.OnClickListener> mSubHeadViewListeners = new SparseArray<>(0);
    public SparseArray<View.OnClickListener> mSubFootViewListeners = new SparseArray<>(0);
    public List<T> mData = new ArrayList<>();
    private View mFootView;
    private View mHeadView;
    private boolean mIsEnableLoadMore;
    protected Context mContext;
    //没有下一页时是否显示 没有更多了
    private boolean mShowNoMore;

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder;
        BaseViewHolder.OnItemClickListener onItemClickListener;
        SparseArray<View.OnClickListener> subViewListeners;
        mContext = parent.getContext();

        if (viewType == BaseViewHolder.HEAD_VIEW && mHeadView != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHeadView.setLayoutParams(lp);
            viewHolder = createHeaderViewHolder(mHeadView);
            onItemClickListener = mOnHeadViewClickListener;
            subViewListeners = mSubHeadViewListeners;
        } else if (viewType == BaseViewHolder.FOOT_VIEW && mFootView != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mFootView.setLayoutParams(lp);
            viewHolder = new SimpleRecyViewHolder(mFootView);
            onItemClickListener = mOnFootViewClickListener;
            subViewListeners = mSubFootViewListeners;
        } else {
            viewHolder = createViewTypeHolder(parent, viewType);
            onItemClickListener = mOnItemClickListener;
            subViewListeners = mSubDataViewListeners;
        }
        viewHolder.setViewType(viewType);
        viewHolder.setOnItemClickListener(onItemClickListener);
        updateSubViewClickEvent(viewHolder, subViewListeners);
        return viewHolder;
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            boolean needFull = holder.getViewType() == BaseViewHolder.HEAD_VIEW ||
                    holder.getViewType() == BaseViewHolder.FOOT_VIEW;
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(needFull);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == BaseViewHolder.HEAD_VIEW) {
            bindDataToHeadViewHolder(holder);
        } else if (itemViewType == BaseViewHolder.FOOT_VIEW) {
            bindDataToFootViewHolder(holder);
        } else {
            bindDataToViewHolder(holder, position - (mHeadView == null ? 0 : 1), itemViewType);
        }
    }

    public boolean needMatchParentWidth(int position) {
        if (mFootView != null && position == (mData.size() + (mFootView == null ? 0 : 1))) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mData.size() > 0 && (mIsEnableLoadMore || mShowNoMore)) {
            return mData.size() + (mHeadView == null ? 0 : 1) + (mFootView == null ? 0 : 1);
        } else {
            return mData.size() + (mHeadView == null ? 0 : 1);
        }
    }

    public void updateSubViewClickEvent(BaseViewHolder viewHolder, SparseArray<View.OnClickListener> subViewListeners) {
        for (int index = 0; index < subViewListeners.size(); index++) {
            int key_subResId = subViewListeners.keyAt(index);
            viewHolder.setSubViewClickListener(key_subResId, subViewListeners.get(key_subResId));
        }
    }

    /**
     * 为子视图添加点击事件
     *
     * @param resId
     * @param listener
     */
    public void addSubViewClickListener(int resId, View.OnClickListener listener) {
        mSubDataViewListeners.put(resId, listener);
    }

    public void setHeadView(View headView) {
        mHeadView = headView;
    }

    /**
     * 设置底部视图（加载更多）
     *
     * @param view
     */
    public void setFootView(View view) {
        mFootView = view;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView != null && position == 0) {
            return BaseViewHolder.HEAD_VIEW;
        }
        if (mFootView != null && (mIsEnableLoadMore || mShowNoMore)) {
            if (mHeadView == null) {
                if (position == mData.size()) {
                    return BaseViewHolder.FOOT_VIEW;
                }
            } else {
                //头部为position=0，列表的数据从position=1的item开始，即position=1的item显示Data的第0个数据
                if (position == mData.size() + 1) {
                    return BaseViewHolder.FOOT_VIEW;
                }
            }
        }
        int dataItemViewType = getDataItemViewType(mHeadView == null ? position : position - 1);
        return dataItemViewType;
    }

    /**
     * 如果不想使底部视图更新为 没有数据了 则要在setEnableLoadMore为false前mAdapter.notifyItemRemoved(mAdapter.getDatas().size());
     * 否则使用mAdapter.updateFootView(nodataView); 更新为没有数据View
     *
     * @param enableLoadMore
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        if (!enableLoadMore) {
            notifyItemRemoved(mData.size() + (mHeadView == null ? 0 : 1)); //移除footview
        }
        mIsEnableLoadMore = enableLoadMore;
    }

    /**
     * 设置是否可以加载更多，如果没有下一页，是否显示 没有数据了
     * @param enableLoadMore
     * @param showNoMore
     */
    public void setEnableLoadMore(boolean enableLoadMore, boolean showNoMore) {
        if (!enableLoadMore && !mShowNoMore) {
            notifyItemRemoved(mData.size() + (mHeadView == null ? 0 : 1)); //移除footview
        }
        mIsEnableLoadMore = enableLoadMore;
        mShowNoMore = showNoMore;
    }

    public boolean getEnableLoadMore() {
        return mIsEnableLoadMore;
    }

    public boolean isShowNoMore() {
        return mShowNoMore;
    }

    /**
     * 获取对应的item视图
     *
     * @param position
     * @return
     */
    abstract public int getDataItemViewType(int position);

    public void setOnItemClickListener(BaseViewHolder.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnFootViewClickListener(BaseViewHolder.OnFootViewClickListener listener) {
        mOnFootViewClickListener = listener;
    }

    public void setOnHeadViewClickListener(BaseViewHolder.OnHeadViewClickListener onHeadViewClickListener) {
        mOnHeadViewClickListener = onHeadViewClickListener;
    }

    /**
     * 创建数据ViewHolder
     *
     * @param viewType recycleview child data view type
     * @return
     */
    public abstract BaseViewHolder createViewTypeHolder(ViewGroup parent, int viewType);

    /**
     * 创建头部ViewHolder
     *
     * @param headView recycleview header view (frist child view)
     * @return
     */
    public BaseViewHolder createHeaderViewHolder(View headView) {
        return null;
    }

    /**
     * 创建底部ViewHolder
     *
     * @param footView recycleview footer view (last child view)
     * @return
     */
    public BaseViewHolder createFooterViewHolder(View footView) {
        return null;
    }

    public abstract void bindDataToViewHolder(BaseViewHolder holder, int position, int viewType);

    /**
     * 绑定数据到头部视图
     *
     * @param holder
     */
    public void bindDataToHeadViewHolder(BaseViewHolder holder) {

    }

    /**
     * 绑定数据到底部视图
     *
     * @param holder
     */
    public void bindDataToFootViewHolder(BaseViewHolder holder) {

    }

    /**
     * 获取指定位置的数据
     */
    public T getData(int position) {
        if (position < 0 || position > getItemCount() - (mData == null ? 0 : 1))
            return null;
        else
            return mData.get(position);
    }

    /**
     * 获得列表的数量
     *
     * @return
     */
    public int getDataSize() {
        if (mData == null || mData.size() == 0) {
            return -1;
        } else {
            return mData.size();
        }
    }

    /**
     * 返回数据对应的行号
     * 例如在有headview的情况下，第1个数据其实对应的数据列表的行号是0
     *
     * @param position
     * @return
     */
    public int getPosition(int position) {
        return position - (mHeadView == null ? 0 : 1);
    }

    /**
     * 清楚所有的数据
     */
    public void clear() {
        mData.clear();
        notifyItemRangeRemoved(mHeadView == null ? 0 : 1, mData.size());
    }

    /**
     * 刷新页面的元素
     *
     * @param datas
     */
    public void refreshDatas(List<T> datas) {
        if (datas != null) {
            this.mData.clear();
            this.mData.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取所有的数据集合
     *
     * @return
     */
    public List<T> getDatas() {
        return mData;
    }

    /**
     * 加载更多的元素
     *
     * @param data
     */
    public void appendData(T data) {
        if (data != null) {
            final int oldSize = this.mData.size();
            this.mData.add(data);
            notifyItemInserted(oldSize + (mHeadView == null ? 0 : 1));
        }
    }

    /**
     * 加载更多的元素
     */
    public void appendDatas(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            this.mData.addAll(datas);
            final int offest = (mHeadView == null ? 0 : 1);
            notifyItemRangeInserted(mData.size() - datas.size() + offest, datas.size());

        }
    }

    /**
     * 加载更多的元素
     */
    public void appendDatas(int position, List<T> datas) {
        if (datas != null && datas.size() > 0) {
            this.mData.addAll(position, datas);
            final int offest = (mHeadView == null ? 0 : 1);
            notifyItemRangeInserted(this.mData.size() - datas.size() + offest, datas.size());
        }
    }


    /**
     * 移除指定位置的数据
     */
    public void removeData(int position) {
        if (position >= 0 && position < getItemCount() - (mFootView == null ? 0 : 1)) {
            final int offest = (mHeadView == null ? 0 : 1);
            this.mData.remove(position);
            notifyItemRemoved(position + offest);
        }
    }

    /**
     * 更新底部视图
     *
     * @param newView 新的底部视图
     */
    public void updateFootView(View newView) {
        if (newView == null || newView == mFootView || mIsEnableLoadMore)
            return;

        boolean isEmptyView = this.mFootView == null;
        this.mFootView = newView;
        if (isEmptyView)
            notifyDataSetChanged();
        else
            notifyItemChanged(mData.size() + (mHeadView == null ? 0 : 1));
    }

    /**
     * 更新头部视图
     *
     * @param newView 新的头部视图
     */
    public void updateHeadView(View newView) {

        if (newView == null || newView == mHeadView)
            return;

        boolean isEmptyView = this.mHeadView == null;

        this.mHeadView = newView;

        if (isEmptyView)
            notifyDataSetChanged();
        else
            notifyItemChanged(0);
    }

    public static class SimpleRecyViewHolder extends BaseViewHolder {

        public SimpleRecyViewHolder(View itemView) {
            super(itemView);
        }

        public SimpleRecyViewHolder(View itemView, OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

    }
}
