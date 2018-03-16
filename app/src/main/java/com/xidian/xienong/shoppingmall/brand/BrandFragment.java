package com.xidian.xienong.shoppingmall.brand;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.BrandAdapter;
import com.xidian.xienong.adapter.BrandContentAdapter;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.model.Advertisement;
import com.xidian.xienong.model.Brand;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.CommodityComment;
import com.xidian.xienong.model.CommodityImage;
import com.xidian.xienong.model.FirstContent;
import com.xidian.xienong.model.FirstInfo;
import com.xidian.xienong.model.ProduceParameter;
import com.xidian.xienong.model.SecondContent;
import com.xidian.xienong.model.User;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.RecyclerViewHeader;
import com.xidian.xienong.util.Time;

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
 * Created by koumiaojuan on 2017/6/28.
 */

public class BrandFragment extends Fragment implements View.OnClickListener {

    private View view;
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout brandLayout;
    private List<Brand> brands = new ArrayList<>();
    private String[] names = {"水果蔬菜","畜牧水产","粮油米面","农副加工","亩木花草","农资农机","中草药"};
    private LayoutInflater mInflater;
    private Context context;
    private ImageButton brandButton;
    private PopupWindow pop;
    private View brandLayoutWindow;
    private BrandContentAdapter adapter;
    private GridView brandGrideView;
    private int position = 0;
    private ViewPager viewPager;
    private ViewGroup group;
    private AtomicInteger what;
    private ImageAdapter imageadapter;
    private boolean isContinue = true;
    private boolean threadStart = false;
    private NetWorkUtil netWorkUtil;
    private OKHttp httpUrl;
    private List<Advertisement> list = new ArrayList<>();
    private int[] images = {R.drawable.putao,R.drawable.li};
    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };
    private RecyclerView brandRecyclerView;
    private RecyclerViewHeader header;
    private RecyclerView.LayoutManager mLayoutManager;
    private BrandAdapter brandAdapter;
    private List<View> lines = new ArrayList<>();
    private List<FirstContent> contents = new ArrayList<>();
    List<FirstInfo> fis = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_brand, null);
        initViews();
        initDatas();
        initEvents();
        getBrandAd();
        getCommodityByVariety();
        return view;
    }

    private void getCommodityByVariety() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("begin_date", Time.getAmonthAgoDay());
        map.put("end_date", Time.getToday());

        httpUrl.post(Url.GetCommodityByVariety, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetCommodityByVariety : " + Url.GetCommodityByVariety);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseCommodityVarietyResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseCommodityVarietyResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                contents.clear();
                fis.clear();

                JSONArray typesList = jb.getJSONArray("types");
                for(int i=0; i < typesList.length(); i++){
                    JSONObject object = typesList.getJSONObject(i);
                    FirstInfo fi = new FirstInfo();
                    fi.setFirstId(object.getString("first_id"));
                    fi.setFirstName(object.getString("first_name"));
                    fi.setFirstPic(object.getString("first_pic"));
                    fis.add(fi);
                }

                JSONArray contentsList = jb.getJSONArray("categories");

                for(int i=0; i < contentsList.length(); i++){
                    JSONObject object = contentsList.getJSONObject(i);
                    FirstContent fc = new FirstContent();
                    fc.setFirstCategoryId(object.getString("first_cateory"));
                    fc.setFirstCategoryName(object.getString("first_cateory_name"));
                    fc.setFirstUrl(object.getString("first_cateory_pic"));

                    List<SecondContent> secondContents = new ArrayList<>();
                    JSONArray jsc = object.getJSONArray("commodity_varietyList");
                    for(int j=0;j< jsc.length();j++){
                        JSONObject jso = jsc.getJSONObject(j);
                        SecondContent sc = new SecondContent();
                        sc.setSecondCategory(jso.getString("variety"));
                        sc.setSaleCount(jso.getInt("saleCount"));
                        List<Commodity> secondCommodities = new ArrayList<>();
                        JSONArray commoditieslist = jso.getJSONArray("commodityList");
                        for(int k = 0 ; k <commoditieslist.length();k++ ){
                            JSONObject cl = commoditieslist.getJSONObject(k);
                            Commodity c = new Commodity();
                            c.setCommodityId(cl.getString("commodity_id"));
                            c.setCommodityName(cl.getString("commodity_name"));
                            c.setDealVolume(cl.getString("deal_volume"));

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
                            for(int m=0; m< images.length();m++){
                                JSONObject jo = images.getJSONObject(m);
                                CommodityImage ci = new CommodityImage();
                                ci.setCommodityImgUrl(jo.getString("commodity_pic_url"));
                                ciList.add(ci);
                            }
                            c.setCommodityImageList(ciList);

                            List<CommodityComment> ccList = new ArrayList<>();
                            JSONArray ccs = cl.getJSONArray("commodity_comments");
                            for(int p=0; p< ccs.length();p++){
                                JSONObject jo = ccs.getJSONObject(p);
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
                    fc.setSecondContentList(secondContents);
                    contents.add(fc);
                }
                brandAdapter.notifyDataSetChanged();
                initHorizontalFirstContent();

            }else{
                Toast.makeText(context, "获取商品失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initHorizontalFirstContent() {
        lines.clear();
        if(brandLayout.getChildCount() == 0) {
            for (int i = 0; i < fis.size(); i++) {
                View view = mInflater.inflate(R.layout.horizontal_brand_list_item,
                        brandLayout, false);
                TextView name = (TextView) view.findViewById(R.id.tv_brand_name);
                View line = (View) view.findViewById(R.id.tv_brand_line);
                view.setId(Integer.parseInt(fis.get(i).getFirstId()));
                line.setId(Integer.parseInt(fis.get(i).getFirstId()));

                name.setText(fis.get(i).getFirstName());
                if (line.getId() == Integer.parseInt(fis.get(0).getFirstId())) {
                    line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                } else {
                    line.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }

                lines.add(line);
                brandLayout.addView(view);
                view.setOnClickListener(BrandFragment.this);
            }
            Log.i("kmj","lines :" + lines.size());
        }
    }



    private void getBrandAd() {
        if(!netWorkUtil.isNetworkAvailable()){
            initAdData();
        }else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ad_model", "4");
            map.put("ad_page", "5");
            map.put("ad_class", "5");
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
        for(int i=0; i < 2;i++){
            ImageView image = new ImageView(context);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            if(!netWorkUtil.isNetworkAvailable()){
                image.setBackgroundResource(images[i]);
            }else {
                Glide.with(context).load(list.get(i).getPicture()).centerCrop().into(image);
            }
            imageviews.add(image);
        }
        imageadapter = new ImageAdapter(imageviews);
        group.removeAllViews();
        for (int i = 0; i < 2 ; i++) {
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


    private void initEvents() {
        brandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopWindow(v);
            }
        });
        brandGrideView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelected(position);
                smoothMoveToPosition(position);
                pop.dismiss();
            }
        });
    }

    private void PopWindow(final View v) {
        ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.brand_arrow));
        adapter.notifyDataSetChanged();
        pop = new PopupWindow(brandLayoutWindow,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.update();
        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.showAsDropDown(v);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                pop.dismiss();
                ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.brand_arrow_down));
            }
        });

    }

    private void initDatas() {
        context = getActivity();
        netWorkUtil = new NetWorkUtil(context);
        httpUrl = OKHttp.getInstance();
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        brands.clear();
//        for(int i=0; i < 7;i++){
//            Brand brand = new Brand();
//            brand.setBrandId(""+i);
//            brand.setBrandName(names[i]);
//            List<SecondContent> scList = new ArrayList<>();
//            for(int j=0;j<4;j++){
//                SecondContent sc = new SecondContent();
//                sc.setContent("苹果");
//                sc.setDetail("美容养颜的最好选择");
//                List<Commodity> comlist = new ArrayList<>();
//                for(int k =0;k < 8;k++){
//                    Commodity com = new Commodity();
//                    com.setCommodityName("新采摘的水果，保质期1个月，低温保存");
//                    com.setPrice("10元/斤");
//                    com.setImgHeight( (k % 2)*100 + 400);
//                    comlist.add(com);
//                }
//                sc.setCommodities(comlist);
//                scList.add(sc);
//            }
//            brand.setContents(scList);
//            brands.add(brand);
//        }

        adapter = new BrandContentAdapter(fis,context);
        brandGrideView.setAdapter(adapter);
        mLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        brandAdapter = new BrandAdapter(getActivity(),fis,contents);
        brandRecyclerView.setLayoutManager(mLayoutManager);
        header.attachTo(brandRecyclerView);
        brandRecyclerView.setAdapter(brandAdapter);


    }

    private void initViews() {
        mHorizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.brand_scrollView);
        brandLayout = (LinearLayout)view.findViewById(R.id.id_brand_layout);
        brandButton = (ImageButton)view.findViewById(R.id.brand_arrow);
        brandLayoutWindow = getActivity().getLayoutInflater().inflate(R.layout.brand_gridview_layout,null);
        brandGrideView = (GridView) brandLayoutWindow.findViewById(R.id.brand_gridview);
        brandRecyclerView = (RecyclerView)view.findViewById(R.id.brand_recyclerview);
        header = (RecyclerViewHeader)view.findViewById(R.id.recyclerViewHeader);
        viewPager = (ViewPager)view.findViewById(R.id.brand_viewPager);
        group = (ViewGroup)view.findViewById(R.id.brand_viewGroups);

    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > list.size() - 1) {
            what.getAndAdd(-2);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        for(int i= 0 ; i < lines.size();i++){
            if(lines.get(i).getId() == id){
                lines.get(i).setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                smoothMoveToPosition(id-Integer.parseInt(fis.get(0).getFirstId()));
            }else{
                lines.get(i).setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }
        }

    }

    private void smoothMoveToPosition(int n) {

        int firstItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
        int lastItem = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        if (n <= firstItem){
            brandRecyclerView.smoothScrollToPosition(n);
        }else if ( n <= lastItem){
            int top = brandRecyclerView.getChildAt(n - firstItem).getTop();
            brandRecyclerView.smoothScrollBy(0, top);
        }else{
            brandRecyclerView.smoothScrollToPosition(n);
        }

    }

}
