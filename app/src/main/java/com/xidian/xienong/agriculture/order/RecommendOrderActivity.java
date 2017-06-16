package com.xidian.xienong.agriculture.order;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ViewPagerAdapter;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.model.Resource;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

import static android.support.design.widget.TabLayout.MODE_FIXED;

/**
 * Created by koumiaojuan on 2017/6/12.
 */

public class RecommendOrderActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private SharePreferenceUtil sp;
    private OKHttp httpUrl;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    public static ViewPager mRecommendViewPager;
    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments = new ArrayList<>();
    private List<OrderBean> list = new ArrayList<OrderBean>();
    public static ViewPagerAdapter mViewPagerAdapter;
    public static List<List<OrderBean>>  orders = new ArrayList<>();
    public static List<OrderBean> waitingOrderList = new ArrayList<>();
    public static List<OrderBean> receivedOrderList = new ArrayList<>();
    public static List<OrderBean> operingOrderList = new ArrayList<>();
    public static List<OrderBean> finishedOrderList = new ArrayList<>();
    private int currentItem = 0;
    private LinearLayout ll_no_order;
    private LinearLayout ll_no_find_machine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_order_activity);
        initViews();
        initData();
        configViews();
        initEvents();
      
    }

    private void configViews() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments,"recommend_order");
        mRecommendViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mRecommendViewPager.setOffscreenPageLimit(4);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mRecommendViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_FIXED);

        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mRecommendViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        requestOrderList(Url.RecommendOptimalOrder);
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
        mTitles = getResources().getStringArray(R.array.order_titles);
        orders.add(waitingOrderList);
        orders.add(receivedOrderList);
        orders.add(operingOrderList);
        orders.add(finishedOrderList);
        for(int i=0; i < mTitles.length;i++){
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("data", (Serializable)orders.get(i));
            GrabOrderFragment mFragment = new GrabOrderFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(mFragment);
        }
    }

    private void registerBroadcastReceiver() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constants.GRAB_ORDER_FAIL_ACTION);
        filter.addAction(Constants.GRAB_ORDER_SUCCESS_ACTION);
        filter.addAction(Constants.DISPATCH_ORDER_SUCCESS_ACTION);
        filter.addAction(Constants.IS_OPERATING_STATE_ORDER_ACTION);
        filter.addAction(Constants.OPERATED_ORDER_ACTION);
        filter.addAction(Constants.CANCLE_ORDER_ACTION);
        filter.addAction(Constants.CANCLE_GRABBED_ORDER_ACTION);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals(Constants.GRAB_ORDER_FAIL_ACTION)){
                currentItem = 0;
                requestOrderList(Url.RecommendOptimalOrder);
            }
            if(intent.getAction().equals(Constants.GRAB_ORDER_SUCCESS_ACTION) || intent.getAction().equals(Constants.DISPATCH_ORDER_SUCCESS_ACTION) || intent.getAction().equals(Constants.CANCLE_GRABBED_ORDER_ACTION) ){
                currentItem = 1;
                requestOrderList(Url.GetHaveGrabbedOrder);
            }
            if(intent.getAction().equals(Constants.IS_OPERATING_STATE_ORDER_ACTION)){
                currentItem = 2;
                requestOrderList(Url.GetIsOperatingOrder);
            }
            if(intent.getAction().equals(Constants.OPERATED_ORDER_ACTION)  || intent.getAction().equals(Constants.CANCLE_ORDER_ACTION)){
                currentItem = 3;
                requestOrderList(Url.GetOperatedOrder);
            }

        }
    };

    private void requestOrderList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("worker_id", sp.getWorkerId());
        map.put("worker_id", "41");
        if(url.equals(Url.RecommendOptimalOrder)){
//            map.put("worker_longtitude", sp.getLongtitude());
//            map.put("worker_lantitude", sp.getLantitude());
            map.put("worker_longtitude", "108.93849000");
            map.put("worker_lantitude", "34.34024000");
        }

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

    private void parseResponse(String resultResponse,String url) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                list.clear();
                JSONArray orderlist = jb.getJSONArray("grabOrders");
                for(int i=0; i < orderlist.length(); i++){
                    JSONObject object = orderlist.getJSONObject(i);
                    OrderBean order = new OrderBean();
                    order.setDistance(object.getDouble("distance"));
                    order.setOrder_id(object.getString("order_id"));
                    order.setOrderCode(object.getString("orderCode"));
                    order.setOrderState(object.getString("orderState"));
                    order.setFarmer_id(object.getString("farmer_id"));
                    order.setFarmer_name(object.getString("farmer_name"));
                    order.setTelephone(object.getString("telephone"));
                    order.setHeadphoto(object.getString("head_photo"));

                    order.setCrop_address(object.getString("crop_address"));
                    order.setCrop_lantitude(object.getDouble("crop_lantitude"));
                    order.setCrop_longtitude(object.getDouble("crop_longtitude"));
                    order.setCategory_id(object.getString("category_id"));
                    order.setMachine_category(object.getString("category_name"));
                    order.setCropland_type(object.getString("cropland_type"));
                    order.setReservation_time(object.getString("reservation_time"));
                    order.setAdviceState(object.getString("advice_state"));
                    order.setUpload_time(object.getString("upload_time"));
                    order.setGrabOrderTime(object.getString("grab_order_time"));
                    order.setDispatchTime(object.getString("dispatch_time"));
                    order.setOperatingTime(object.getString("operating_time"));
                    order.setOperatedTime(object.getString("has_operated_time"));
                    order.setEvaluateTime(object.getString("evaluate_time"));

                    order.setCropland_number(object.getDouble("cropland_number"));
                    order.setEvaluate(object.getBoolean("isEvaluate"));
                    order.setDeleteByWorker(object.getBoolean("isDeletedByWorker"));
                    order.setCancleTime(object.getString("cancle_time"));
                    order.setCancleReason(object.getString("cancle_reason"));
                    order.setCancleMethod(object.getString("cancle_method"));
                    JSONArray driverArray = object.getJSONArray("drivers");
                    List<Driver> drivers = new ArrayList<Driver>();
                    drivers.clear();
                    for(int j = 0; j < driverArray.length(); j++){
                        JSONObject jo =  driverArray.getJSONObject(j);
                        Driver driver = new Driver();
                        driver.setDriver_id(jo.getString("driver_id"));
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
                        machine.setIdentification_number(object.getString("category_name") +"-"+jo.getString("identification_number"));
                        machines.add(machine);
                    }
                    order.setMachines(machines);
                    List<Resource> resources = new ArrayList<Resource>();
                    resources.clear();
                    for(int i1 = 0; i1 < machines.size();i1++){
                        Resource re = new Resource();
                        re.setMachine(machines.get(i1));
                        re.setDriver(drivers.get(i1));
                        resources.add(re);
                    }
                    order.setResources(resources);
                    list.add(order);
                }

                Collections.sort(list);
                fillFragmentsWithData(url,list);
            }else{
                Toast.makeText(RecommendOrderActivity.this, "获取可抢单列表失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void fillFragmentsWithData(String url, List<OrderBean> list) {
        if(url.equals(Url.RecommendOptimalOrder)){
            waitingOrderList.clear();
            waitingOrderList.addAll(list);
        }else if(url.equals(Url.GetHaveGrabbedOrder)){
            receivedOrderList.clear();
            receivedOrderList.addAll(list);
        }else if(url.equals(Url.GetIsOperatingOrder)){
            operingOrderList.clear();
            operingOrderList.addAll(list);
        }else{
            finishedOrderList.clear();
            finishedOrderList.addAll(list);
        }
        mViewPagerAdapter.notifyDataSetChanged();
        mRecommendViewPager.setCurrentItem(currentItem);
    }


    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.recommend_order_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.recommend_order_tablayout);
        mRecommendViewPager = (ViewPager) findViewById(R.id.recommend_order_viewpager);
        ll_no_order = (LinearLayout)findViewById(R.id.no_grab_order);
        ll_no_find_machine = (LinearLayout)findViewById(R.id.no_find_machine);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
        if(position == 0){
            currentItem = 0;
            requestOrderList(Url.RecommendOptimalOrder);
        }else if(position == 1){
            currentItem = 1;
            requestOrderList(Url.GetHaveGrabbedOrder);
        }else if(position == 2){
            currentItem = 2;
            requestOrderList(Url.GetIsOperatingOrder);
        }else{
            currentItem = 3;
            requestOrderList(Url.GetOperatedOrder);
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
