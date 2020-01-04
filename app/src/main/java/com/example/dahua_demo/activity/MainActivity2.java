package com.example.dahua_demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahua_demo.Device;
import com.example.dahua_demo.HomeDeviceAdapter;
import com.example.dahua_demo.NewMenuItem;
import com.example.dahua_demo.R;
import com.example.dahua_demo.adapter.HomeMenuAdapter;
import com.example.dahua_demo.callback.SimpleItemTouchHelperCallback;
import com.example.dahua_demo.widget.RecyclViewWrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private HomeMenuAdapter mHomeMenuAdapter;
    private RecyclerView mRvMenus;
    private RelativeLayout mLlHoveringPop;
    private View mLlHoveringExpand;
    private String[] mHomeMenuNames;
    //是否强制显示头部menu滑块
    private boolean mShowHeadMenuView;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclViewWrap mRvDevicesList;
    private HomeDeviceAdapter mDeviceAdapter;
    //存放item宽或高
    private Map<Integer, Integer> mMapList = new HashMap<>();
    //item的宽/高
    private int itemW;
    private int itemH;
    private int iResult;
    private int iposition;

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

    //竖着不同
    private static final int MANAGER_LINEAR_VERTICAL_ = 4;
    private LinearLayoutManager linearLayoutManager;
    //形态变量
    private int intType = MANAGER_LINEAR_VERTICAL_;


    private void bindEvent() {
        mDeviceAdapter = new HomeDeviceAdapter(R.layout.item_home_device);
        //设备列表内容
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvDevicesList.setLayoutManager(linearLayoutManager);
        mDeviceAdapter.setHasStableIds(true);
        mRvDevicesList.setAdapter(mDeviceAdapter);

        mRvDevicesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mHandler.sendEmptyMessage(dy);
            }
        });
        mRvDevicesList.setOnActionDownListener(new RecyclViewWrap.OnActionDownListener() {
            @Override
            public void onActionDown() {
                mShowHeadMenuView = false;                                                 //重置滑块显示逻辑
            }
        });
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
//            Log.d("blue", "getScollYDistance: " + getScollYDistance());
//            if (message.what < 0) {
//                ViewGroup.LayoutParams layoutParams = mRvMenus.getLayoutParams();
//                layoutParams.height = (int) (getScollYDistance() * 0.02);
//                if (mRvMenus.getHeight() < 200) {
//                    mRvMenus.setLayoutParams(layoutParams);
//                }
//            } else {

            if (mShowHeadMenuView) {
                ViewGroup.LayoutParams layoutParams = mRvMenus.getLayoutParams();
                if (layoutParams.height == 0) {
                    layoutParams.height = 178;
                    mRvMenus.setLayoutParams(layoutParams);
                }
            }else{

                ViewGroup.LayoutParams layoutParams = mRvMenus.getLayoutParams();
                layoutParams.height = 178 - getScollYDistance();
                if (layoutParams.height <= 0) {
                    layoutParams.height = 0;
                }
                if (layoutParams.height > 178) {
                    layoutParams.height = 178;
                }

                Log.d("blue", "getScollYDistance: " + getScollYDistance() + "   height: " + layoutParams.height);


                mRvMenus.setLayoutParams(layoutParams);

            }

            return true;
        }
    });

    public int getScollYDistance() {
        return unlikeVertical();
    }

    /**
     * 不同Item VERTICAL
     */
    public int unlikeVertical() {
        int itemWH = 0;
        int itemTR = 0;
        int distance = 0;
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        //判断是横着还是竖着，得出宽或高
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            itemWH = firstVisiableChildView.getHeight();
        }
        //一层判断mMapList是否为空，若不为空则根据键判断保证不会重复存入
        if (mMapList.size() == 0) {
            mMapList.put(position, itemWH);
        } else {
            if (!mMapList.containsKey(position)) {
                mMapList.put(position, itemWH);
                Log.d("poi", mMapList + "");
            }
        }
        //判断是横着还是竖着，得出未滑出屏幕的距离
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            itemTR = firstVisiableChildView.getTop();
        }
        //position为动态获取，目前屏幕Item位置
        for (int i = 0; i < position; i++) {
            if (mMapList != null && mMapList.get(i) != null) {
                //iposition移出屏幕的距离
                iposition = iposition + mMapList.get(i);
            }
        }
        //根据类型拿iposition减未移出Item部分距离，最后得出滑动距离
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            distance = iposition - itemTR;
        }
        //item宽高
        itemW = firstVisiableChildView.getWidth();
        itemH = firstVisiableChildView.getHeight();
        //归零
        iposition = 0;
        return distance;
    }

    private void initLayoutView() {
        mRvDevicesList = findViewById(R.id.rv_devices_list);
        mRvMenus = findViewById(R.id.rv_menus);
        mLlHoveringPop = findViewById(R.id.ll_hovering_pop);
        mLlHoveringPop.setOnClickListener(this);
        mLlHoveringExpand = findViewById(R.id.ll_hovering_expand);
        mLlHoveringExpand.setOnClickListener(this);
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
        if (view.getId() == R.id.ll_hovering_expand) {
            ViewGroup.LayoutParams layoutParams = mRvMenus.getLayoutParams();
            if (layoutParams.height == 0) {
                mShowHeadMenuView = true;
                layoutParams.height = 178;
                mRvMenus.setLayoutParams(layoutParams);
            }
        }
    }
}
