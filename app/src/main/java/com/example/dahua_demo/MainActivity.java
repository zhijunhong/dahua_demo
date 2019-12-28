package com.example.dahua_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private HomeMenuAdapter mHomeMenuAdapter;
    private RecyclerView mRvMenus;
    private LinearLayout mLlHoveringPop;
    private View mLlHoveringExpand;
    private String[] mHomeMenuNames;
    //是否强制显示头部menu滑块
    private boolean mShowHeadMenuView;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclViewWrap mRvDevicesList;
    private HomeDeviceAdapter mDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayoutView();
        initMenu();
        bindEvent();

        initialize();
    }

    private void initialize() {
        List<Device> devices = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Device device = new Device();
            device.setDeviceName("Nihao" + i);
            devices.add(device);
        }
        mDeviceAdapter.refreshDatas(devices);
    }

    private void bindEvent() {
        mDeviceAdapter = new HomeDeviceAdapter(R.layout.item_home_device);
        //设备列表内容
        mRvDevicesList.setLayoutManager(new LinearLayoutManager(this));
        mDeviceAdapter.setHasStableIds(true);
        mRvDevicesList.setAdapter(mDeviceAdapter);

        mRvDevicesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRvDevicesList.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (mShowHeadMenuView) {
                            mLlHoveringExpand.setVisibility(View.GONE);
                            mRvMenus.setVisibility(View.VISIBLE);
                            return;
                        }

                        int offset = mRvMenus.getHeight() - mLlHoveringExpand.getHeight();

                        if (mRvMenus.getVisibility() == View.GONE) {
                            offset = mLlHoveringExpand.getHeight();
                        }

                        if (mLlHoveringExpand.getVisibility() == View.GONE) {
                            offset = mRvMenus.getHeight();
                        }

                        if (mRvDevicesList.getUpDistance() >= offset) {
                            mLlHoveringExpand.setVisibility(View.VISIBLE);
                            mRvMenus.setVisibility(View.GONE);
                        } else {
                            mLlHoveringExpand.setVisibility(View.GONE);
                            mRvMenus.setVisibility(View.VISIBLE);
                        }

                        Log.d("blue-offset", "offset:" + offset + ",getUpDistance:" + mRvDevicesList.getUpDistance());
                    }
                }, 200);
            }
        });
        mRvDevicesList.setOnActionDownListener(new RecyclViewWrap.OnActionDownListener() {
            @Override
            public void onActionDown() {
                mShowHeadMenuView = false;                                                 //重置滑块显示逻辑
            }
        });
    }


    private void initLayoutView() {
        mRvDevicesList = findViewById(R.id.rv_devices_list);
        mRvMenus = findViewById(R.id.rv_menus);
        mLlHoveringPop = findViewById(R.id.ll_hovering_pop);
        mLlHoveringPop.setOnClickListener(this);
        mLlHoveringExpand = findViewById(R.id.ll_hovering_expand);
    }

    private void initMenu() {
        mHomeMenuNames = getResources().getStringArray(R.array.index_menu_items);
        mHomeMenuAdapter = new HomeMenuAdapter(R.layout.item_home_model);
        mRvMenus.setHasFixedSize(true);
        mRvMenus.setAdapter(mHomeMenuAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvMenus.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mHomeMenuAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRvMenus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<NewMenuItem> menuItems = loadHomeMenu(mHomeMenuNames);
        mHomeMenuAdapter.refreshDatas(menuItems);
    }

    private List<NewMenuItem> loadHomeMenu(String[] mHomeMenuNames) {
        List<NewMenuItem> homeMenuNames = new ArrayList<>();
        for (String str : mHomeMenuNames) {
            NewMenuItem item = new NewMenuItem();
            item.setValue(str);
            homeMenuNames.add(item);
        }
        return homeMenuNames;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_hovering_pop) {
            if (mLlHoveringExpand.getVisibility() == View.VISIBLE && mRvMenus.getVisibility() == View.GONE) {                //显示头部menu滑块
                mShowHeadMenuView = true;
                mLlHoveringExpand.setVisibility(View.GONE);
                mRvMenus.setVisibility(View.VISIBLE);
            }

        }
    }
}
