package com.xidian.xienong.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ConsultAdapter;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.agriculture.order.RecommendOrderActivity;
import com.xidian.xienong.agriculture.resource.InputResourceActivity;
import com.xidian.xienong.model.Advertisement;
import com.xidian.xienong.model.Consult;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.RecyclerDecoration;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/11.
 */

public class MachineEntranceActivity extends AppCompatActivity{
    
    private Toolbar machineEntranceToolbar;
    private ViewPager viewPager;
    private ViewGroup group;
    private AtomicInteger what;
    private ImageAdapter imageadapter;
    private int[] images = {R.drawable.machine_ad_1,R.drawable.machine_ad_2,R.drawable.machine_ad_3,R.drawable.machine_ad_4};;
    private boolean isContinue = true;
    private boolean threadStart = false;
    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };
    private RelativeLayout receiveOrder,resource;
    private RecyclerView recyclerView;
    private ConsultAdapter adapter;
    private List<Consult> list = new ArrayList<Consult>();
    private RecyclerView.LayoutManager mLayoutManager;
    private NetWorkUtil netWorkUtil;
    private OKHttp httpUrl;
    private List<Advertisement> adLists = new ArrayList<>();
    private List<Consult> consultLists = new ArrayList<>();
    private String clickView = "";
    private SharePreferenceUtil sp;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_entrance_activity);
        initViews();
        initData();
        getMachineEntranceAdList();
        getConsultList();
        initEvents();
    }

    private void getConsultList() {
        Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.GetConsults, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetConsults : " + Url.GetConsults);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseConsultResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseConsultResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                consultLists.clear();
                JSONArray consultList = jb.getJSONArray("consultList");
                for(int i=0; i < consultList.length(); i++){
                    JSONObject object = consultList.getJSONObject(i);
                    Consult c = new Consult();
                    c.setId(object.getString("consult_id"));
                    c.setTitle(object.getString("title"));
                    c.setSubTitle(object.getString("subTitle"));
                    c.setContentUrl(object.getString("content_url"));
                    c.setImageUrl(object.getString("image_url"));
                    consultLists.add(c);
                }
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(MachineEntranceActivity.this, "获取咨讯失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getMachineEntranceAdList() {
        if(!netWorkUtil.isNetworkAvailable()){
            initMachineEntranceAdData();
            netWorkUtil.setNetwork();
        }else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ad_model", "2");
            map.put("ad_page", "3");
            map.put("ad_class", "3");
            httpUrl.post(Url.GetHomepageAd, map, new BaseCallback<String>() {
                @Override
                public void onRequestBefore() {
                    Log.i("kmj", "GetHomepageAd : " + Url.GetHomepageAd);
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    Log.i("kmj", "onFailure : " + e.toString());
                }

                @Override
                public void onSuccess(Response response, String resultResponse) {
                    Log.i("kmj", "result : " + resultResponse);
                    parseMachineEntranceAdResponse(resultResponse);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.i("kmj", "error : " + e.toString());
                }
            });
        }
    }

    private void initMachineEntranceAdData() {
        what = new AtomicInteger(0);
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();
        for(int i=0; i < 4;i++){
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            if(!netWorkUtil.isNetworkAvailable()){
                image.setBackgroundResource(images[i]);
            }else {
                Glide.with(MachineEntranceActivity.this).load(adLists.get(i).getPicture()).centerCrop().into(image);
            }
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



    }

    private void parseMachineEntranceAdResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                adLists.clear();
                JSONArray adlist = jb.getJSONArray("adList");
                for(int i=0; i < adlist.length(); i++){
                    JSONObject object = adlist.getJSONObject(i);
                    Advertisement ad = new Advertisement();
                    ad.setId(object.getString("ad_id"));
                    ad.setPicture(object.getString("ad_picture"));
                    ad.setUrl(object.getString("ad_url"));
                    adLists.add(ad);
                }
                initMachineEntranceAdData();
            }else{
                Toast.makeText(MachineEntranceActivity.this, "获取广告失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initEvents() {
        machineEntranceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        receiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickView = "receiveOrder";
                checkIsLogin(v);
            }
        });

        resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickView = "resource";
                checkIsLogin(v);
            }
        });
    }

    private void checkIsLogin(final View v) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", sp.getPhoneNumber());
        map.put("password", MD5Util.getMD5String(sp.getPassword()));
        map.put("token", sp.getToken());
        httpUrl.post(Url.IsLogin, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "IsLogin : " + Url.IsLogin);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String msg = jb.getString("message");
                    if(result.equals("SUCCESS")){
                        if(v == receiveOrder){
                            Intent  intent = new Intent(MachineEntranceActivity.this, RecommendOrderActivity.class);
                            startActivity(intent);
                        }else{
                            Intent  intent = new Intent(MachineEntranceActivity.this, InputResourceActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Intent  intent = new Intent(MachineEntranceActivity.this, LoginActivity.class);
                        intent.putExtra("clickView", clickView);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
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



    private void initData() {
        setSupportActionBar(machineEntranceToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        netWorkUtil = new NetWorkUtil(MachineEntranceActivity.this);
        httpUrl = OKHttp.getInstance();
        getSupportActionBar().setTitle("咨讯");
        mLayoutManager =  new LinearLayoutManager(MachineEntranceActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new ConsultAdapter(MachineEntranceActivity.this,consultLists);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerDecoration(
                MachineEntranceActivity.this, LinearLayoutManager.VERTICAL, R.drawable.divider));
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initViews() {
        machineEntranceToolbar = (Toolbar)findViewById(R.id.machine_entrance_toolbar);
        viewPager = (ViewPager)findViewById(R.id.machine_viewPager);
        group = (ViewGroup)findViewById(R.id.machine_viewGroups);
        receiveOrder = (RelativeLayout)findViewById(R.id.rl_receive_order);
        resource = (RelativeLayout)findViewById(R.id.rl_resource);
        recyclerView = (RecyclerView)findViewById(R.id.consult_recyclerview);
    }

}
