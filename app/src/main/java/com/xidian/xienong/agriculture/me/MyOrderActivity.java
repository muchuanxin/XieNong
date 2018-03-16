package com.xidian.xienong.agriculture.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ViewPagerAdapter;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class MyOrderActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private SharePreferenceUtil sp;
    private  OKHttp httpUrl;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    public static ViewPager mViewPager;
    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private  List<Fragment> mFragments = new ArrayList<>();
    private List<OrderBean> list = new ArrayList<OrderBean>();
    public static ViewPagerAdapter mViewPagerAdapter;
    public static List<List<OrderBean>>  orders = new ArrayList<>();
    public static List<OrderBean> allOrderList = new ArrayList<>();
    public static List<OrderBean> waitingOrderList = new ArrayList<>();
    public static List<OrderBean> receivedOrderList = new ArrayList<>();
    public static List<OrderBean> operingOrderList = new ArrayList<>();
    public static List<OrderBean> finishedOrderList = new ArrayList<>();
    private int currentItem = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_activity);
        initViews();
        initData();
        configViews();
        initEvents();
    }

    private void configViews() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments,"my_order");
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        requestAnnounceList(Url.GetAllAnnouncement);
    }

    private void initEvents() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        httpUrl = OKHttp.getInstance();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerBroadcastReceiver();
        mTitles = getResources().getStringArray(R.array.tab_titles);
        orders.add(allOrderList);
        orders.add(waitingOrderList);
        orders.add(receivedOrderList);
        orders.add(operingOrderList);
        orders.add(finishedOrderList);
        for(int i=0; i < mTitles.length;i++){
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("data", (Serializable)orders.get(i));
            OrderFragment mFragment = new OrderFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(mFragment);
        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constants.HAS_EVALUATED_BY_FARMER);
        filter.addAction(Constants.HAS_AGREE_OR_REFUSE_BY_FARMER);
        filter.addAction(Constants.HAS_CANCLED_BY_FARMER);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals(Constants.HAS_EVALUATED_BY_FARMER) || intent.getAction().equals(Constants.HAS_CANCLED_BY_FARMER)
                    || intent.getAction().equals(Constants.HAS_AGREE_OR_REFUSE_BY_FARMER)){
                currentItem = 4;
                requestAnnounceList(Url.GetOperatedAnnouncement);
            }else {
                Log.i("kmj","---agree----");
                currentItem = 2;
                requestAnnounceList(Url.GetHaveReceivedAnnouncement);
            }

        }
    };

    public void requestAnnounceList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("farmer_id", sp.getUserId());
        httpUrl.post(url,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "url : " + url);
            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseResponse(resultResponse,url);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });

    }

    private void parseResponse(String response,String url) {
        try {
            JSONObject jb = new JSONObject(response);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                list.clear();
                JSONArray orderlist = jb.getJSONArray("AnnouncementList");
                for(int i=0; i < orderlist.length(); i++){
                    JSONObject object = orderlist.getJSONObject(i);
                    OrderBean order = new OrderBean();
                    order.setOrder_id(object.getString("order_id"));
                    order.setOrderCode(object.getString("orderCode"));
                    order.setFarmer_id(object.getString("farmer_id"));
                    order.setFarmer_name(object.getString("farmer_name"));
                    order.setWorker_id(object.getString("worker_id"));
                    order.setWorker_name(object.getString("worker_name"));
                    order.setTelephone(object.getString("farmer_telephone"));
                    order.setWorker_telephone(object.getString("worker_telephone"));
                    order.setFarmerHeadphoto(object.getString("farmer_head_photo"));
                    order.setWorkerHeadphoto(object.getString("worker_head_photo"));
                    order.setUpload_time(object.getString("upload_time"));
                    order.setCrop_address(object.getString("crop_address"));
                    order.setCrop_lantitude(object.getDouble("crop_lantitude"));
                    order.setCrop_longtitude(object.getDouble("crop_longtitude"));
                    order.setMachine_category(object.getString("category_name"));
                    order.setCropland_type(object.getString("cropland_type"));
                    order.setReservation_time(object.getString("reservation_time"));
                    order.setCropland_number(object.getDouble("cropland_number"));
                    order.setEvaluate(object.getBoolean("isEvaluate"));
                    order.setDeletedByFarmer(object.getBoolean("isDeletedByFarmer"));
                    order.setOrderState(object.getString("orderState"));
                    order.setAdviceState(object.getString("advice_state"));
                    order.setCancleTime(object.getString("cancle_time"));
                    order.setCancleReason(object.getString("cancle_reason"));
                    order.setApplyCancleReason(object.getString("apply_cancle_reason"));
                    order.setApplyCancleReasonId(object.getString("apply_cancle_reason_id"));
                    order.setPrice(object.getInt("work_price"));
                    JSONArray driverArray = object.getJSONArray("drivers");
                    List<Driver> drivers = new ArrayList<Driver>();
                    drivers.clear();
                    for(int j = 0; j < driverArray.length(); j++){
                        JSONObject jo =  driverArray.getJSONObject(j);
                        Driver driver = new Driver();
                        driver.setDriver_name(jo.getString("driver_name"));
                        driver.setDriver_telephone(jo.getString("driver_telephone"));
                        drivers.add(driver);
                    }
                    order.setDrivers(drivers);
                    JSONArray machineArray = object.getJSONArray("machines");
                    List<Machine> machines = new ArrayList<Machine>();
                    machines.clear();
                    for(int j = 0; j < machineArray.length(); j++){
                        JSONObject jo =  machineArray.getJSONObject(j);
                        Machine machine = new Machine();
                        machine.setMachine_id(jo.getString("machine_id"));
                        machine.setIdentification_number(jo.getString("identification_number"));
                        machines.add(machine);
                    }
                    order.setMachines(machines);

                    JSONArray machineImageArray = object.getJSONArray("machine_url");
                    List<MachineImage> machineImages = new ArrayList<MachineImage>();
                    machineImages.clear();
                    for(int j = 0; j < machineImageArray.length(); j++){
                        JSONObject jo =  machineImageArray.getJSONObject(j);
                        MachineImage image = new MachineImage();
                        image.setUrl(jo.getString("image_url"));
                        machineImages.add(image);
                    }

                    order.setMachineImages(machineImages);
                    list.add(order);
                }
                Log.i("kmj","--list size----" + list.size());
                fillFragmentsWithData(list,url);

            }else {
                Toast.makeText(MyOrderActivity.this, "获取订单失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    private void fillFragmentsWithData(List<OrderBean> list,String url) {

        if(url.equals(Url.GetAllAnnouncement)){
            allOrderList.clear();
            allOrderList.addAll(list);
        }else if(url.equals(Url.GetWaitingToReceiveAnnouncement)){
            waitingOrderList.clear();
            waitingOrderList.addAll(list);
        }else if(url.equals(Url.GetHaveReceivedAnnouncement)){
            receivedOrderList.clear();
            receivedOrderList.addAll(list);
        }else if(url.equals(Url.GetIsOperatingAnnouncement)){
            operingOrderList.clear();
            operingOrderList.addAll(list);
        }else{
            finishedOrderList.clear();
            finishedOrderList.addAll(list);
        }
        mViewPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(currentItem);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.my_order_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.order_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.order_viewpager);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
        if(position == 0){
            currentItem = 0;
            requestAnnounceList(Url.GetAllAnnouncement);
        }else if(position == 1){
            currentItem = 1;
            requestAnnounceList(Url.GetWaitingToReceiveAnnouncement);
        }else if(position == 2){
            currentItem = 2;
            requestAnnounceList(Url.GetHaveReceivedAnnouncement);
        }else if(position == 3){
            currentItem = 3;
            requestAnnounceList(Url.GetIsOperatingAnnouncement);
        }else{
            currentItem = 4;
            requestAnnounceList(Url.GetOperatedAnnouncement);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
