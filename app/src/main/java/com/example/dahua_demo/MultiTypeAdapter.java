package com.example.dahua_demo;

import android.view.ViewGroup;


/**
 * Created by zengyazhi on 17/6/14.
 */

public class MultiTypeAdapter extends BaseAdapter<HolderTypeData> {

    @Override
    public int getDataItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public BaseViewHolder createViewTypeHolder(ViewGroup parent, int viewType) {
        for(HolderTypeData typeData:mData) {
            if(typeData.getType() == viewType) {
                return typeData.buildHolder(parent);
            }
        }
        return null;
    }

    @Override
    public void bindDataToViewHolder(BaseViewHolder holder, int position, int viewType) {
        mData.get(position).bindDataToHolder(holder, position, viewType);
    }
}
