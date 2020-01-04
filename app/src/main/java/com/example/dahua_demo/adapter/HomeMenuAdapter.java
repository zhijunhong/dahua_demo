package com.example.dahua_demo.adapter;

import android.view.View;
import android.widget.TextView;

import com.example.dahua_demo.BaseSingleTypeAdapter;
import com.example.dahua_demo.ItemTouchHelperAdapter;
import com.example.dahua_demo.NewMenuItem;
import com.example.dahua_demo.R;

import java.util.Collections;

public class HomeMenuAdapter extends BaseSingleTypeAdapter<NewMenuItem, HomeMenuAdapter.HomeMenuViewHolder> implements ItemTouchHelperAdapter {

    public HomeMenuAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    public HomeMenuViewHolder buildViewHolder(View itemView) {
        return new HomeMenuViewHolder(itemView);
    }

    @Override
    public void bindDataToViewHolder(HomeMenuViewHolder holder, com.example.dahua_demo.NewMenuItem foo, int position) {
        holder.mValue.setText(foo.getValue());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(getDatas(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        getDatas().remove(position);
        notifyItemRemoved(position);
    }

    public static class HomeMenuViewHolder extends com.example.dahua_demo.BaseViewHolder {
        private TextView mValue;

        public HomeMenuViewHolder(View itemView) {
            super(itemView);
            mValue = itemView.findViewById(R.id.tv_text);
        }
    }
}
