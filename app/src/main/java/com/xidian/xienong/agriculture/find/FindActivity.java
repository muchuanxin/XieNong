package com.xidian.xienong.agriculture.find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.CategoryAdapter;
import com.xidian.xienong.adapter.DistanceItemAdapter;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.adapter.IntelligenceAdapter;
import com.xidian.xienong.adapter.SortItemAdapter;
import com.xidian.xienong.adapter.WorkerAdapter;
import com.xidian.xienong.agriculture.announcement.NewAnnounceActivity;
import com.xidian.xienong.agriculture.me.MyOrderActivity;
import com.xidian.xienong.application.ConnectUtil;
import com.xidian.xienong.model.MachineCategory;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.model.Worker;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.AdjustmentListViewHeight;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.ListenedScrollView;
import com.xidian.xienong.util.PullToRefreshLayout;
import com.xidian.xienong.util.PullToRefreshLayout.OnRefreshListener;
import com.xidian.xienong.util.PullableListView;
import com.xidian.xienong.util.Utils;
import com.xidian.xienong.util.ViewUpSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class FindActivity extends AppCompatActivity{

    private Toolbar mToolbar;

    //轮播图部分
    private ViewPager viewPager;
    private ViewGroup group;
    private AtomicInteger what;
    private ImageAdapter imageadapter;
    private boolean isContinue = true;
    private boolean threadStart = false;
    private ViewUpSearch search;
    private boolean isExpand = false;
    private int[] images = {R.mipmap.machine_ad1, R.mipmap.machine_ad2, R.mipmap.machine_ad3, R.mipmap.machine_ad4};



    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };
    private ListenedScrollView scrollView;

    private LinearLayout information;
    private View head_view;

    //结果List显示部分
    CategoryAdapter hListViewAdapter;
    ImageView previewImg;

    private double longtitude,lantitude;
    private RequestQueue requestQueue;
    private List<Worker> list = new ArrayList<Worker>();
    private String category_id = "0";
    private String scope = "10000000";

    private PullableListView listview;
    private PullToRefreshLayout pullToRefreshLayout = null;
    private RelativeLayout rlByCategory,rlByDistance,rlByIntelligence;
    private TextView byCategory,byDistance,byIntelligence;
    private ImageView arrow1,arrow2,arrow3;
    private LinearLayout find_no_machine;
    private WorkerAdapter adapter;
    private SortItemAdapter itemAdapter;
    private DistanceItemAdapter distanceAdapter;
    private IntelligenceAdapter intelligenceAdapter;
    boolean isOpen = false;
    private PopupWindow pop;
    private View categoryLayout,distanceLayout,intelligenceLayout;
    private ListView chooseListView,distanceListView,intelligenceListView;
    private AMapLocationClient mlocationClient = null;
    private LinearLayout ll_distance_pop,ll_category_pop,ll_intelligence_top;
    private LatLonPoint point =null;
    private OKHttp httpUrl;
    private NestedScrollView scrollview;
    private RelativeLayout farmerOrder,publishRequirement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_activity);
        initViews();
        initData();
        initEvents();

    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > images.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    private void initEvents() {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager.setAdapter(imageadapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setIndicatorBackground(position);
            }

            private void setIndicatorBackground(int position) {
                // TODO Auto-generated method stub
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    ImageView temp = (ImageView) group.getChildAt(i);
                    if (i == position) {
                        temp.setBackgroundResource(R.drawable.dot_red);
                    } else {
                        temp.setBackgroundResource(R.drawable.dot_grey);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }



        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        if (!threadStart) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        if (isContinue) {
                            viewHandler.sendEmptyMessage(what.get());
                            whatOption();
                        }
                    }
                }

            }).start();
            threadStart = true;
        }

        // result List 相关
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FindActivity.this,DetailActivity.class);
                intent.putExtra("worker", list.get(arg2));
                startActivity(intent);
            }
        });
        pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout refreshLayout) {
                // TODO Auto-generated method stub
                // 下拉刷新操作
                pullToRefreshLayout = refreshLayout;
                if( pullToRefreshLayout != null){
                    getMachineByCategoryAndDistance(category_id,scope);
                }

            }
        });
        rlByCategory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                byCategory.setTextColor(Color.parseColor("#ef8000"));
                arrow1.setBackgroundResource(R.drawable.choose_arrow_down_highlight);
                PopWindow(v,byCategory,arrow1);
            }
        });

        rlByDistance.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                byDistance.setTextColor(Color.parseColor("#ef8000"));
                arrow2.setBackgroundResource(R.drawable.choose_arrow_down_highlight);
                PopWindow(v,byDistance,arrow2);
            }
        });
        rlByIntelligence.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                byIntelligence.setTextColor(Color.parseColor("#ef8000"));
                arrow3.setBackgroundResource(R.drawable.choose_arrow_down_highlight);
                PopWindow(v,byIntelligence,arrow3);
            }
        });

        farmerOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(FindActivity.this, MyOrderActivity.class);
                startActivity(intent);
            }
        });

        publishRequirement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(FindActivity.this, NewAnnounceActivity.class);
                startActivity(intent);
            }
        });

        listview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int localheight = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        localheight = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int sY = (int) event.getY();
                        int scrollY = scrollview.getScrollY();
                        int height = scrollview.getHeight();
                        int scrollViewMeasuredHeight = scrollview.getChildAt(0)
                                .getMeasuredHeight();
                        //这个表示,当滑动到scrollview顶部的时候,
                        if (scrollY == 0) {
                            //检测到在listview里面手势向下滑动的手势,就下拉刷新,反之,则无法触发下拉刷新
                            if (localheight - sY > 10) {
                                // 取消拦截scrollview事件,listview不能刷新
                                scrollview.requestDisallowInterceptTouchEvent(false);
                                break;
                            }
                            // 拦截scrollview事件,listview可以刷新
                            scrollview.requestDisallowInterceptTouchEvent(true);
                        }
                        //这个表示scrollview没恢复到顶部,在listview里面是无法触发下拉刷新的
                        else {
                            // 取消拦截scrollview事件,listview不能刷新
                            scrollview.requestDisallowInterceptTouchEvent(false);
                        }
                        //滑动到底部的时候,自动去加载更多.
                        if ((scrollY + height) == scrollViewMeasuredHeight) {
                            // 滑到底部触发加载更多
//                            onLoadMore();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        localheight = 0;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("附近农机");
        httpUrl = OKHttp.getInstance();
        what = new AtomicInteger(0);
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();
        for(int i=0; i < 4;i++) {
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(images[i]);
            imageviews.add(image);
        }
        imageadapter = new ImageAdapter(imageviews);
        for (int i = 0; i < 4 ; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20); //,
            lp.setMargins(15, 0, 15, 0);
            imageView.setLayoutParams(lp);
            imageView.setPadding(5, 5, 5, 5);
            if (i == 0)
                imageView.setBackgroundResource(R.drawable.dot_red);
            else
                imageView.setBackgroundResource(R.drawable.dot_grey);
            group.addView(imageView);
        }

        // 与result  List有关
        requestQueue = Volley.newRequestQueue(this);
        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        mlocationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        mlocationClient.setLocationListener(locationListener);
        mlocationClient.startLocation();
        if(Constants.isFirstGetAllMachineCategory == true){
            getMachineCategory();
        }
    }



    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        group = (ViewGroup)findViewById(R.id.machine_find_viewGroups);
        viewPager = (ViewPager)findViewById(R.id.machine_find_viewPager);
        scrollview = (NestedScrollView)findViewById(R.id.find_nested_scrollview);
        //与结果List有关
        // TODO Auto-generated method stub
        rlByCategory = (RelativeLayout)findViewById(R.id.rl_by_category);
        rlByDistance = (RelativeLayout)findViewById(R.id.rl_by_distance);
        rlByIntelligence = (RelativeLayout)findViewById(R.id.rl_by_intelligence);
        byCategory = (TextView)findViewById(R.id.by_category);
        byDistance = (TextView)findViewById(R.id.by_distance);
        byIntelligence = (TextView)findViewById(R.id.by_intelligence);
        arrow1 = (ImageView)findViewById(R.id.by_category_arrow);
        arrow2 = (ImageView)findViewById(R.id.by_distance_arrow);
        arrow3 = (ImageView)findViewById(R.id.by_intelligence_arrow);
        listview = (PullableListView)findViewById(R.id.find_machine_listview);
        listview.setLoadmoreVisible(false);
        pullToRefreshLayout = (PullToRefreshLayout)findViewById(R.id.find_machine_refresh_view);
        find_no_machine = (LinearLayout)findViewById(R.id.no_find_machine);
        adapter = new WorkerAdapter(this, list);
        listview.setAdapter(adapter);
        AdjustmentListViewHeight.setListViewHeightBasedOnChildren(listview);
        categoryLayout = this.getLayoutInflater().inflate(R.layout.find_choose_category_listview,null);
        ll_category_pop = (LinearLayout)categoryLayout.findViewById(R.id.ll_category_pop);
        chooseListView = (ListView)categoryLayout.findViewById(R.id.find_choose_category_listview);
        itemAdapter = new SortItemAdapter(this, Constants.machineCategoryList);
        chooseListView.setAdapter(itemAdapter);

        distanceLayout = this.getLayoutInflater().inflate(R.layout.find_choose_distance_listview,null);
        ll_distance_pop = (LinearLayout)distanceLayout.findViewById(R.id.ll_distance_pop);
        distanceListView = (ListView)distanceLayout.findViewById(R.id.find_choose_distance_listview);
        distanceAdapter = new DistanceItemAdapter(this, Constants.distanceList);
        distanceListView.setAdapter(distanceAdapter);

        intelligenceLayout = this.getLayoutInflater().inflate(R.layout.find_choose_intelligence_listview,null);
        ll_intelligence_top = (LinearLayout)intelligenceLayout.findViewById(R.id.ll_intelligence_pop);
        intelligenceListView = (ListView)intelligenceLayout.findViewById(R.id.find_choose_intelligence_listview);
        intelligenceAdapter = new IntelligenceAdapter(this, Constants.intelligenceList);
        intelligenceListView.setAdapter(intelligenceAdapter);

        farmerOrder = (RelativeLayout)findViewById(R.id.rl_farmer_order);
        publishRequirement = (RelativeLayout)findViewById(R.id.rl_publish_requirment);

    }



    protected void PopWindow(View v, final TextView tv,final ImageView iv) {
        // TODO Auto-generated method stub
        if(v == rlByCategory){
            itemAdapter.notifyDataSetChanged();
            pop = new PopupWindow(categoryLayout,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
        }else if(v== rlByDistance){
            distanceAdapter.setList(Constants.distanceList);
            distanceAdapter.notifyDataSetChanged();
            pop = new PopupWindow(distanceLayout,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
        }else{
            intelligenceAdapter.setList(Constants.intelligenceList);
            intelligenceAdapter.notifyDataSetChanged();
            pop = new PopupWindow(intelligenceLayout,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
        }
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.update();
        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.showAsDropDown(v);
        pop.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                pop.dismiss();
                tv.setTextColor(Color.parseColor("#000000"));
                iv.setBackgroundResource(R.drawable.choose_arrow_down);
            }
        });
        chooseListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                pop.dismiss();
                tv.setTextColor(Color.parseColor("#000000"));
                iv.setBackgroundResource(R.drawable.choose_arrow_down);
                itemAdapter.setPosition(arg2);
                tv.setText(Constants.machineCategoryList.get(arg2).getCategory_name());
                scope = getDistanceScope();
                category_id = Constants.machineCategoryList.get(arg2).getCategory_id();
                getMachineByCategoryAndDistance(category_id,scope);
            }
        });
        distanceListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                pop.dismiss();
                tv.setTextColor(Color.parseColor("#000000"));
                iv.setBackgroundResource(R.drawable.choose_arrow_down);
                distanceAdapter.setPosition(arg2);
                tv.setText(Constants.distanceList.get(arg2));
                scope = getDistanceScope();
                getMachineByCategoryAndDistance(category_id,scope);
            }
        });
        intelligenceListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                pop.dismiss();
                tv.setTextColor(Color.parseColor("#000000"));
                iv.setBackgroundResource(R.drawable.choose_arrow_down);
                intelligenceAdapter.setPosition(arg2);
                tv.setText(Constants.intelligenceList.get(arg2));
                setSortStyle(tv.getText().toString());
                getMachineByCategoryAndDistance(category_id,scope);
            }
        });

        ll_distance_pop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
            }
        });
        ll_category_pop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
            }
        });
        ll_intelligence_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
            }
        });


    }

    protected void setSortStyle(String s) {
        // TODO Auto-generated method stub
        if(s.equals("智能排序")){
            Constants.sortStyle = 0;
        }else if(s.equals("离我最近")){
            Constants.sortStyle = 1;
        }else if(s.equals("好评优先")){
            Constants.sortStyle = 2;
        }else{
            Constants.sortStyle = 3;
        }
    }


    protected String getDistanceScope() {
        // TODO Auto-generated method stub
        String distance="";
        String choosedDistance = byDistance.getText().toString();
        if(choosedDistance.equals("附近") ){
            distance =String.valueOf(Integer.parseInt("5")*1000);
        }else if(choosedDistance.equals("不限") ){
            distance =String.valueOf(Integer.parseInt("10000")*1000);
        }else{
            distance = String.valueOf(Integer.parseInt(choosedDistance.substring(0, choosedDistance.length()-2))*1000);
        }
        return distance;
    }


    protected void getMachineCategory() {
        // TODO Auto-generated method stub
        Constants.machineCategory.clear();

        Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.GetAllMachineCategory,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(okhttp3.Request request, Exception e) {

            }

            @Override
            public void onSuccess(okhttp3.Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("SUCCESS")) {
                        JSONArray list = jb.getJSONArray("machineCategory");
                        for(int i=0; i < list.length(); i++){
                            JSONObject object = list.getJSONObject(i);
                            MachineCategory mc = new MachineCategory();
                            mc.setCategory_id(object.getString("category_id"));
                            mc.setCategory_name(object.getString("category_name"));
                            Constants.machineCategory.add(mc);
                        }
                        MachineCategory mc = new MachineCategory();
                        mc.setCategory_id("0");
                        mc.setCategory_name("全部");
                        Constants.machineCategoryList.add(mc);
                        Constants.machineCategoryList.addAll(Constants.machineCategory);
                        Constants.isFirstGetAllMachineCategory = false;
                    }else{
                        Toast.makeText(FindActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(okhttp3.Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });

    }


    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        return mOption;
    }


    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                point = Utils.getLocationResult(loc);
                longtitude = point.getLongitude();
                lantitude = point.getLatitude();
                mlocationClient.stopLocation();///
                getMachineByCategoryAndDistance(category_id,scope);
            }
        }
    };
    private void getMachineByCategoryAndDistance(String category_id,String distanceScope ) {
        // TODO Auto-generated method stub
        final String id = category_id;
        final String scope = distanceScope;
        String url = id.equals("0")? ConnectUtil.GetAllMachineByDistance:ConnectUtil.GetMachineByDistance;


        Map<String, String> map = new HashMap<String, String>();
        map.put("category_id", id);
               // map.put("farmer_longtitude", String.valueOf(longtitude));
        //map.put("farmer_lantitude", String.valueOf(lantitude));

        map.put("farmer_longtitude", String.valueOf(108.93973000));
        map.put("farmer_lantitude", String.valueOf(34.34041000));
        map.put("distance_scope", scope);
        Log.i("lx   ",id);
        Log.i("lx   ",longtitude+"");
        Log.i("lx   ",lantitude+"");
        Log.i("lx   ",scope);
        httpUrl.post(url,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(okhttp3.Request request, Exception e) {

            }

            @Override
            public void onSuccess(okhttp3.Response response, String resultResponse) {
                Log.i("kmj","response:"+response);
                parseResponse(resultResponse);
            }

            @Override
            public void onError(okhttp3.Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });

    }

    protected void parseResponse(String response) {
        // TODO Auto-generated method stub
        try {
            JSONObject jb = new JSONObject(response);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                list.clear();
                JSONArray workerlist = jb.getJSONArray("workerList");
                for(int i=0; i < workerlist.length(); i++){
                    JSONObject object = workerlist.getJSONObject(i);
                    Worker worker = new Worker();
                    worker.setWorkerId(object.getString("worker_id"));
                    worker.setWorkerName(object.getString("worker_name"));
                    worker.setAddress(object.getString("address"));
                    worker.setCategory_id(object.getString("category_id"));
                    worker.setCategory_name(object.getString("category_name"));
                    worker.setLongtitude(object.getDouble("longtitude"));
                    worker.setLantitude(object.getDouble("lantitude"));
                    worker.setWork_price(object.getInt("work_price"));
                    worker.setMachine_number(object.getString("machine_number"));
                    worker.setChecked(object.getBoolean("isChecked"));
                    worker.setMachineDescription(object.getString("machine_description_or_message"));
                    worker.setDistance(object.getDouble("distance"));
                    worker.setEvaluateVaule((float) object.getDouble("comprehensive_evaluation"));
                    worker.setEvaluateCount(object.getInt("CustomerCount"));
                    List<MachineImage> images = new ArrayList<MachineImage>();
                    images.clear();
                    JSONArray imagelist = object.getJSONArray("machine_images");
                    for(int j=0; j < imagelist.length();j++){
                        JSONObject image = imagelist.getJSONObject(j);
                        MachineImage machineImage = new MachineImage();
                        machineImage.setUrl(image.getString("image_url"));
                        images.add(machineImage);
                    }
                    worker.setMachineImages(images);
                    list.add(worker);
                }
                if(list.size()==0){
                    find_no_machine.setVisibility(View.VISIBLE);
                    pullToRefreshLayout.setVisibility(View.GONE);
                }else{
                    find_no_machine.setVisibility(View.GONE);
                    pullToRefreshLayout.setVisibility(View.VISIBLE);
                    Collections.sort(list);
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }
                if(pullToRefreshLayout != null){
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
                AdjustmentListViewHeight.setListViewHeightBasedOnChildren(listview);
            }else{
                if(pullToRefreshLayout != null){
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }else{
                    Toast.makeText(this, "获取农机失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
