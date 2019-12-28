package com.example.dahua_demo;

import android.view.ViewGroup;

/**
 * Created by zengyazhi on 17/6/14.
 */

public interface HolderTypeData<VH extends BaseViewHolder>{

    int getType();

    BaseViewHolder buildHolder(ViewGroup parent);

    void bindDataToHolder(VH vh, int position, int type);
}
