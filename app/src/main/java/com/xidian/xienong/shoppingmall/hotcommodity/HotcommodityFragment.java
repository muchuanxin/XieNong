package com.xidian.xienong.shoppingmall.hotcommodity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.HotCommodityAdapter;
import com.xidian.xienong.adapter.HotSecondCommodityAdapter;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.model.Advertisement;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.CommodityComment;
import com.xidian.xienong.model.CommodityImage;
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

/**
 * Created by koumiaojuan on 2017/6/28.
 */

public class HotcommodityFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private ViewGroup group;
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
    private List<Commodity> hotCommodities = new ArrayList<>();
    private GridView hotGridView;
    private NoScrollGridView hotSecondGridView;
    private HotCommodityAdapter hotCommodityAdapter;
    private HotSecondCommodityAdapter hotSecondCommodityAdapter;
    private List<SecondContent> secondContents = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_hot_commodity, null);
        getActivity().getSupportFragmentManager();
        initViews();
        initDatas();
        getShoppingAd();
        getHotCommodity();
        getHotSecondCategory();
        initEvents();
        return view;
    }

    private void initEvents() {
        hotGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity c = hotCommodities.get(position);
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                intent.putExtra("commodity",c);
                startActivity(intent);
            }
        });

        hotSecondGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SecondContent sc = secondContents.get(position);
                if(sc.getSecondCategory().equals("暂无")){
                    Intent intent = new Intent(context, CommodityDetailActivity.class);
                    intent.putExtra("commodity",sc.getCommodities().get(0));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(context, CommodityShowActivity.class);
                    intent.putExtra("secondContent", (Serializable) sc);
                    startActivity(intent);
                }
            }
        });
    }

    private void getHotSecondCategory() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("begin_date", Time.getAmonthAgoDay());
        map.put("end_date", Time.getToday());

        httpUrl.post(Url.GetHotSecondCategory, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetHotSecondCategory : " + Url.GetHotSecondCategory);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseHotSecondCommodityResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseHotSecondCommodityResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                secondContents.clear();

                JSONArray hotSecondCommoditieslist = jb.getJSONArray("commodities");
                for(int i=0; i < hotSecondCommoditieslist.length(); i++){

                    JSONObject object = hotSecondCommoditieslist.getJSONObject(i);
                    SecondContent sc = new SecondContent();
                    sc.setSecondCategory(object.getString("second_cateory"));
                    sc.setSaleCount(Integer.parseInt(object.getString("saleCount")));

                    List<Commodity> secondCommodities = new ArrayList<>();
                    JSONArray commoditieslist = object.getJSONArray("commodityList");
                    for(int j = 0 ; j <commoditieslist.length();j++ ){
                        JSONObject cl = commoditieslist.getJSONObject(j);
                        Commodity c = new Commodity();
                        c.setCommodityId(cl.getString("commodity_id"));
                        c.setCommodityName(cl.getString("commodity_name"));
                        c.setDealVolume(cl.getString("deal_volume"));
                        c.setSale_quantity(cl.getString("sale_quantity"));
                        c.setPrice(cl.getString("on_shelve_price"));

                        c.setCurrentCount(cl.getString("current_count"));
                        c.setDiscount(cl.getString("discount"));
                        c.setTransportExpense(cl.getString("trans_expense"));
                        c.setCommodityDescription(cl.getString("commodity_description"));
                        c.setSpecification(cl.getString("specification"));

                        JSONObject  pp = cl.getJSONObject("produce_parameter");
                        ProduceParameter pro = new ProduceParameter();
                        pro.setWarranty(pp.getString("warranty"));
                        pro.setNetWeight(pp.getString("net_weight"));
                        pro.setPackagingManner(pp.getString("packaging_manner"));
                        pro.setProducingArea(pp.getString("producing_area"));
                        c.setProduceParameter(pro);

                        List<CommodityImage> ciList = new ArrayList<>();
                        JSONArray images = cl.getJSONArray("commodity_image");
                        for(int k=0; k< images.length();k++){
                            JSONObject jo = images.getJSONObject(k);
                            CommodityImage ci = new CommodityImage();
                            ci.setCommodityImgUrl(jo.getString("commodity_pic_url"));
                            ciList.add(ci);
                        }
                        c.setCommodityImageList(ciList);

                        List<CommodityComment> ccList = new ArrayList<>();
                        JSONArray ccs = cl.getJSONArray("commodity_comments");
                        for(int k=0; k< ccs.length();k++){

                            JSONObject jo = ccs.getJSONObject(k);
                            CommodityComment cc = new CommodityComment();
                            cc.setCommentId(jo.getString("comment_id"));
                            cc.setCommentContent(jo.getString("comment_content"));
                            cc.setCommentTime(jo.getString("comment_time"));
                            cc.setCommentLevel((float) jo.getDouble("comment_level"));
                            User user = new User();
                            user.setUserName(jo.getString("user_name"));
                            user.setHead_photo(jo.getString("head_photo"));
                            cc.setUser(user);
                            ccList.add(cc);
                        }
                        c.setCommodityCommentsList(ccList);
                        secondCommodities.add(c);
                    }
                    sc.setCommodities(secondCommodities);
                    secondContents.add(sc);
                }
                hotSecondGridView.setAdapter(hotSecondCommodityAdapter);
                hotSecondCommodityAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(context, "获取二级热门商品失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getHotCommodity() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("begin_date", Time.getAmonthAgoDay());
        map.put("end_date", Time.getToday());

        httpUrl.post(Url.GetHotCommodity, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetHotCommodity : " + Url.GetHotCommodity);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseHotCommodityResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseHotCommodityResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                hotCommodities.clear();
                JSONArray hotCommoditieslist = jb.getJSONArray("commodities");
                for(int i=0; i < hotCommoditieslist.length(); i++){
                    JSONObject object = hotCommoditieslist.getJSONObject(i);
                    Commodity c = new Commodity();
                    c.setCommodityId(object.getString("commodity_id"));
                    c.setCommodityName(object.getString("commodity_name"));
                    c.setDealVolume(object.getString("deal_volume"));
                    c.setSale_quantity(object.getString("sale_quantity"));
                    c.setPrice(object.getString("on_shelve_price"));
                    c.setCurrentCount(object.getString("current_count"));
                    c.setDiscount(object.getString("discount"));
                    c.setTransportExpense(object.getString("trans_expense"));
                    c.setCommodityDescription(object.getString("commodity_description"));
                    c.setSpecification(object.getString("specification"));

                    JSONObject  pp = object.getJSONObject("produce_parameter");
                    ProduceParameter pro = new ProduceParameter();
                    pro.setWarranty(pp.getString("warranty"));
                    pro.setNetWeight(pp.getString("net_weight"));
                    pro.setPackagingManner(pp.getString("packaging_manner"));
                    pro.setProducingArea(pp.getString("producing_area"));
                    c.setProduceParameter(pro);

                    List<CommodityImage> ciList = new ArrayList<>();
                    JSONArray images = object.getJSONArray("commodity_image");
                    for(int j=0; j< images.length();j++){
                        JSONObject jo = images.getJSONObject(j);
                        CommodityImage ci = new CommodityImage();
                        ci.setCommodityImgUrl(jo.getString("commodity_pic_url"));
                        ciList.add(ci);
                    }
                    c.setCommodityImageList(ciList);

                    List<CommodityComment> ccList = new ArrayList<>();
                    JSONArray ccs = object.getJSONArray("commodity_comments");
                    for(int k=0; k< ccs.length();k++){
                        JSONObject jo = ccs.getJSONObject(k);
                        CommodityComment cc = new CommodityComment();
                        cc.setCommentId(jo.getString("comment_id"));
                        cc.setCommentContent(jo.getString("comment_content"));
                        cc.setCommentTime(jo.getString("comment_time"));
                        cc.setCommentLevel((float) jo.getDouble("comment_level"));
                        User user = new User();
                        user.setUserName(jo.getString("user_name"));
                        user.setHead_photo(jo.getString("head_photo"));
                        cc.setUser(user);
                        ccList.add(cc);
                    }
                    c.setCommodityCommentsList(ccList);
                    hotCommodities.add(c);
                }

                hotGridView.setAdapter(hotCommodityAdapter);
                hotCommodityAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(context, "获取热门商品失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
        context = getActivity();
        sp = new SharePreferenceUtil(context, Constants.SAVE_USER);
        netWorkUtil = new NetWorkUtil(context);
        httpUrl = OKHttp.getInstance();
        hotCommodityAdapter = new HotCommodityAdapter(hotCommodities,context);
        hotGridView.setAdapter(hotCommodityAdapter);
        hotSecondCommodityAdapter = new HotSecondCommodityAdapter(secondContents,context);
        hotSecondGridView.setAdapter(hotSecondCommodityAdapter);
    }

    private void initViews() {
        viewPager = (ViewPager)view.findViewById(R.id.hot_commodity_viewPager);
        group = (ViewGroup)view.findViewById(R.id.hot_commodity_viewGroups);
        hotGridView = (GridView)view.findViewById(R.id.hot_gridview);
        hotSecondGridView = (NoScrollGridView)view.findViewById(R.id.hot_second_gridview);
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
        getHotCommodity();
        getHotSecondCategory();
    }


}
