package com.example.dahua_demo;

import android.view.View;

public class HomeDeviceAdapter extends BaseSingleTypeAdapter<Device, HomeDeviceAdapter.HomeDeviceViewHolder> {
    public HomeDeviceAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    public HomeDeviceViewHolder buildViewHolder(View itemView) {
        return new HomeDeviceViewHolder(itemView);
    }

    @Override
    public void bindDataToViewHolder(HomeDeviceViewHolder holder, Device device, int position) {

    }

    public static class HomeDeviceViewHolder extends BaseViewHolder {

        public HomeDeviceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
