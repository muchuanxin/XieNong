package com.xidian.xienong.main.mainpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ConsultAdapter;
import com.xidian.xienong.adapter.FragmentAdapter;
import com.xidian.xienong.adapter.HotCommodityAdapter;
import com.xidian.xienong.adapter.HotSecondCommodityAdapter;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.agriculture.find.FindActivity;
import com.xidian.xienong.home.MachineEntranceActivity;
import com.xidian.xienong.main.PageActivity;
import com.xidian.xienong.main.agricultualtechnique.AgricutualTechniqueFragment;
import com.xidian.xienong.model.Advertisement;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.CommodityComment;
import com.xidian.xienong.model.CommodityImage;
import com.xidian.xienong.model.Consult;
import com.xidian.xienong.model.ProduceParameter;
import com.xidian.xienong.model.SecondContent;
import com.xidian.xienong.model.User;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.shoppingmall.brand.CommodityDetailActivity;
import com.xidian.xienong.shoppingmall.brand.CommodityShowActivity;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.NoScrollGridView;
import com.xidian.xienong.util.RecyclerDecoration;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;
import okhttp3.Response;

import static com.xidian.xienong.main.PageActivity.mTabLayout;
import static com.xidian.xienong.main.PageActivity.titles;

/**
 * Created by koumiaojuan on 2017/6/28.
 *              首页
 */

public class MainPageFragment extends Fragment {

    private View view;

    private AtomicInteger what;
    private ImageAdapter imageadapter;
    private boolean isContinue = true;
    private boolean threadStart = false;
    private Context context;
    private SharePreferenceUtil sp;
    private NetWorkUtil netWorkUtil;
    private OKHttp httpUrl;
    private int[] images = {R.drawable.mall1,R.drawable.mall2,R.drawable.mall3,R.drawable.mall4};
    private List<Advertisement> list = new ArrayList<>();
    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };

    private Toolbar machineEntranceToolbar;
    private ViewPager viewPager;
    private ViewGroup group;
    private RelativeLayout receiveOrder,resource;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConsultAdapter adapter;
    private List<Consult> consultLists = new ArrayList<>();
    private RelativeLayout nongji_btn;
    private RelativeLayout nonghuo_btn;
    private RelativeLayout nongjishu_btn;
    private RelativeLayout nongzi_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_main_page, null);
//        getActivity().getSupportFragmentManager();
        initViews();
        initDatas();
        getShoppingAd();
        getConsultList();
        initEvents();
        return view;
    }

    private void initEvents() {

  nongji_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FindActivity.class);
                startActivity(intent);
            }
        });

        nonghuo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MachineEntranceActivity.class);
                startActivity(intent);
            }
        });
        nongjishu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragment的跳转
                PageActivity.mViewPager.setCurrentItem(2);
            }
        });



    }






    private void getShoppingAd() {
        if(!netWorkUtil.isNetworkAvailable()){
            initAdData();
        }else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ad_model", "4");
            map.put("ad_page", "4");
            map.put("ad_class", "4");
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
                    parseResponse(resultResponse);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    Log.i("kmj", "error : " + e.toString());
                }
            });
        }
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
                Toast.makeText(context, "获取咨讯失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void parseResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                list.clear();
                JSONArray adlist = jb.getJSONArray("adList");
                for(int i=0; i < adlist.length(); i++){
                    JSONObject object = adlist.getJSONObject(i);
                    Advertisement ad = new Advertisement();
                    ad.setId(object.getString("ad_id"));
                    ad.setPicture(object.getString("ad_picture"));
                    ad.setUrl(object.getString("ad_url"));
                    list.add(ad);
                }
                initAdData();
            }else{
                Toast.makeText(context, "获取广告失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initAdData(){
        what = new AtomicInteger(0);
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();
        imageviews.clear();
        for(int i=0; i < 4;i++){
            ImageView image = new ImageView(context);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            if(!netWorkUtil.isNetworkAvailable()){
                image.setBackgroundResource(images[i]);
            }else {
                if(list.size()>i){
                    Glide.with(context).load(list.get(i).getPicture()).centerCrop().into(image);
                }

            }
            imageviews.add(image);
        }
        imageadapter = new ImageAdapter(imageviews);
        group.removeAllViews();
        for (int i = 0; i < 4 ; i++) {
            ImageView imageView = new ImageView(context);
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

    private void initDatas() {
        context = view.getContext();//getActivity();
        sp = new SharePreferenceUtil(context, Constants.SAVE_USER);

        netWorkUtil = new NetWorkUtil(context);
        httpUrl = OKHttp.getInstance();
        mLayoutManager =  new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adapter = new ConsultAdapter(context,consultLists);
        System.out.println("out----------->"+adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerDecoration(
                context, LinearLayoutManager.VERTICAL, R.drawable.divider));
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void initViews() {
        viewPager = (ViewPager)view.findViewById(R.id.hot_commodity_viewPager);
        group = (ViewGroup)view.findViewById(R.id.hot_commodity_viewGroups_2);
/*        machineEntranceToolbar = (Toolbar)view.findViewById(R.id.machine_entrance_toolbar);*/
       /* viewPager = (ViewPager)view.findViewById(R.id.machine_viewPager);
        group = (ViewGroup)view.findViewById(R.id.machine_viewGroups);*/
    /*    receiveOrder = (RelativeLayout)view.findViewById(R.id.rl_receive_order);
        resource = (RelativeLayout)view.findViewById(R.id.rl_resource);*/
        recyclerView = (RecyclerView)view.findViewById(R.id.consult_recyclerview_2);
        nongji_btn=(RelativeLayout)view.findViewById(R.id.rl_nongji);
        nonghuo_btn=(RelativeLayout)view.findViewById(R.id.rl_nonghuo);
        nongjishu_btn=(RelativeLayout)view.findViewById(R.id.rl_nongjishu);
        nongzi_btn=(RelativeLayout)view.findViewById(R.id.rl_nongzi);
    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > list.size() - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }


    public void onResume(){
        super.onResume();
        getShoppingAd();

    }


}
