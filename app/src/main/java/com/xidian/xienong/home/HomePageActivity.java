package com.xidian.xienong.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xidian.xienong.MainActivity;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.agriculture.find.FindActivity;
import com.xidian.xienong.agriculture.me.AboutUsActivity;
import com.xidian.xienong.agriculture.me.ChangePasswordActivity;
import com.xidian.xienong.agriculture.me.FeedbackActivity;
import com.xidian.xienong.agriculture.me.InformationActivity;
import com.xidian.xienong.agriculture.me.SettingActivity;
import com.xidian.xienong.agriculture.me.UpdateManager;
import com.xidian.xienong.agriculture.resource.StatisticsActivity;
import com.xidian.xienong.model.Advertisement;
import com.xidian.xienong.model.AppImage;
import com.xidian.xienong.model.Headline;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.shoppingmall.ShoppingActivity;
import com.xidian.xienong.util.CircleImageView;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.ListenedScrollView;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.MarqueeView;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ViewUpSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class HomePageActivity extends AppCompatActivity {

    private MarqueeView marquee;
    private ViewPager viewPager;
    private ViewGroup group;
    private AtomicInteger what;
    private ImageAdapter imageadapter;
    private boolean isContinue = true;
    private boolean threadStart = false;
    private ViewUpSearch search;
    private boolean isExpand = false;
    private int[] images = {R.mipmap.homepage_1,R.mipmap.homepage_2,R.mipmap.homepage_3,R.mipmap.homepage_4};;
    private int[] appImageView = {R.mipmap.homepage_agriculture,R.mipmap.homepage_field_work,R.mipmap.homepage_shopping};
    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };
    private ListenedScrollView scrollView;
    private TextView machineEntrance,workerEntrance,shoppingEntrance;
    private Toolbar homepageToolbar;
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private NavigationView mNavigationView;
    private View head_view;
    private LinearLayout information;
    private OKHttp httpUrl;
    private List<Advertisement> list = new ArrayList<>();
    private List<Headline> headlines = new ArrayList<>();
    private List<AppImage> appImages = new ArrayList<>();
    private List<String> items = new ArrayList<>();
    private NetWorkUtil netWorkUtil;
    private ImageView ivFarmerEntrance,ivWorkerEntrance,ivShoppingEntrance;
    private List<ImageView> appImageViews = new ArrayList<>();
    private CircleImageView photo;
    private TextView name,telephone;
    private SharePreferenceUtil sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        initViews();
        configViews();
        initData();
        getAdList();
        getAgricultureHeadlines();
        getAppImages();
        initEvents();
      //  Log.e("sHA1",sHA1(HomePageActivity.this));
    }

    private void getAppImages() {
        if(!netWorkUtil.isNetworkAvailable()){
            for(int i=0;i<appImageViews.size();i++){
                appImageViews.get(i).setBackgroundResource(appImageView[i]);
            }
        }else {
            Map<String, String> map = new HashMap<String, String>();
            httpUrl.post(Url.AppImages, map, new BaseCallback<String>() {
                @Override
                public void onRequestBefore() {
                    Log.i("kmj", "AppImages : " + Url.AppImages);
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    Log.i("kmj", "onFailure : " + e.toString());
                }

                @Override
                public void onSuccess(Response response, String resultResponse) {
                    Log.i("kmj", "result : " + resultResponse);
                    parseImageResponse(resultResponse);
                }

                @Override
                public void onError(Response response, int errorCode, Exception e) {
                    if (e!=null) {
                        Log.i("kmj", "error : " + e.toString());
                    }
                }
            });
        }
    }

    private void parseImageResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                appImages.clear();
                JSONArray imageList = jb.getJSONArray("imageList");
                for(int i=0; i < imageList.length(); i++){
                    JSONObject object = imageList.getJSONObject(i);
                    AppImage image = new AppImage();
                    image.setId(object.getString("image_id"));
                    image.setImageName(object.getString("image_name"));
                    image.setImageUrl(object.getString("image_url"));
                    appImages.add(image);
                }
                for(int i=0;i < appImageViews.size();i++){
                    Glide.with(HomePageActivity.this).load(appImages.get(i).getImageUrl()).centerCrop().into(appImageViews.get(i));
                }
            }else{
                Toast.makeText(HomePageActivity.this, "获取图片失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initData() {
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        netWorkUtil = new NetWorkUtil(HomePageActivity.this);
        httpUrl = OKHttp.getInstance();
        search.setSearchContent("最新上市热销农产品");
    }

    private void getAgricultureHeadlines() {
        Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.AgricultureHeadline,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "AgricultureHeadline : " + Url.AgricultureHeadline);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseHeadlineResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                if (e!=null) {
                    Log.i("kmj", "error : " + e.toString());
                }
            }
        });
    }

    private void parseHeadlineResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                headlines.clear();
                JSONArray headlineList = jb.getJSONArray("headlineList");
                for(int i=0; i < headlineList.length(); i++){
                    JSONObject object = headlineList.getJSONObject(i);
                    Headline headLine = new Headline();
                    headLine.setId(object.getString("headline_id"));
                    headLine.setTitle(object.getString("title"));
                    headLine.setDetail(object.getString("detail"));
                    headLine.setTime(object.getString("time"));
                    headlines.add(headLine);
                }
                items.clear();
                for(Headline hl : headlines){
                    items.add(hl.getTitle());
                }
                marquee.startWithList(items);
            }else{
                Toast.makeText(HomePageActivity.this, "获取头条失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void configViews() {
        // 设置显示Toolbar
        setSupportActionBar(homepageToolbar);
        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, homepageToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        head_view= mNavigationView.inflateHeaderView(R.layout.header_navigation);
        information=(LinearLayout)head_view.findViewById(R.id.rl_information);
        photo = (CircleImageView)head_view.findViewById(R.id.id_header_face);
        name = (TextView)head_view.findViewById(R.id.id_header_authorname);
        telephone = (TextView)head_view.findViewById(R.id.id_header_tele);
//    //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);
        onNavgationViewMenuItemSelected(mNavigationView);
    }

    private void onNavgationViewMenuItemSelected(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()){
                    case R.id.nav_menu_statistics:
                        intent = new Intent(HomePageActivity.this, StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_menu_feedback:
                        intent = new Intent(HomePageActivity.this, MainActivity.class);
                        startActivity(intent);
                        //    checkIsLogin();
                        break;
                    case R.id.nav_menu_aboutus:
                        intent = new Intent(HomePageActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_menu_setting:
                        intent = new Intent(HomePageActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                // Menu item点击后选中，并关闭Drawerlayout
                item.setChecked(false);
//                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void checkIsLogin() {
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
                        Intent intent  = new Intent(HomePageActivity.this,FeedbackActivity.class);
                        startActivity(intent);
                    }else{
                        Intent  intent = new Intent(HomePageActivity.this, LoginActivity.class);
                        intent.putExtra("clickView", "feedback");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                if (e!=null){
                    Log.i("kmj", "error : " + e.toString());
                }
            }
        });
    }

    private void initEvents() {

        scrollView.setOnScrollListener(new ListenedScrollView.OnScrollListener() {
            @Override
            public void onBottomArrived() {

            }

            @Override
            public void onScrollStateChanged(ListenedScrollView view, int scrollState) {

            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (oldt < t && ((t - oldt) > 5)) {// 向上
                    search.updateShow(false);
                    isExpand = false;

                }  else if (oldt > t && (oldt - t) > 5) {// 向下
                    search.updateShow(true);
                    isExpand = true;
                }
            }
        });

        machineEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,FindActivity.class);
                startActivity(intent);
            }
        });
        workerEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this,MachineEntranceActivity.class);
                startActivity(intent);
            }
        });
        shoppingEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });
        information.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(HomePageActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });


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

    private void initAdData(){
        what = new AtomicInteger(0);
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();

        for(int i=0; i < 4;i++){
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            if(!netWorkUtil.isNetworkAvailable()){
                image.setBackgroundResource(images[i]);
            }else {
                if(list.size()>i){
                    Glide.with(HomePageActivity.this).load(list.get(i).getPicture()).centerCrop().into(image);
                }

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

    private void getAdList() {
        if(!netWorkUtil.isNetworkAvailable()){
            initAdData();
        }else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ad_model", "1");
            map.put("ad_page", "1");
            map.put("ad_class", "1");
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
                    if (e!=null){
                        Log.i("kmj", "error : " + e.toString());Log.i("kmj", "error : " + e.toString());
                    }
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
                Toast.makeText(HomePageActivity.this, "获取广告失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initViews() {
        homepageToolbar = (Toolbar)findViewById(R.id.homepage_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_homepage_drawerlayout);
//        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_homepage_coordinatorlayout);
        mNavigationView = (NavigationView) findViewById(R.id.id_homepage_navigationview);
        viewPager = (ViewPager)findViewById(R.id.homepage_viewPager);
        group = (ViewGroup)findViewById(R.id.homepage_viewGroups);
        marquee = (MarqueeView)findViewById(R.id.headline);
        search = (ViewUpSearch)findViewById(R.id.search);
        scrollView = (ListenedScrollView)findViewById(R.id.homepage_scrollView);
        machineEntrance = (TextView)findViewById(R.id.machine_entrance);
        workerEntrance = (TextView)findViewById(R.id.worker_entrance);
        shoppingEntrance = (TextView)findViewById(R.id.shopping_entrance);
        ivFarmerEntrance = (ImageView)findViewById(R.id.iv_farmer_entrance);
        ivWorkerEntrance = (ImageView)findViewById(R.id.iv_worker_entrance);
        ivShoppingEntrance = (ImageView)findViewById(R.id.iv_shopping_entrance);
        appImageViews.clear();
        appImageViews.add(ivFarmerEntrance);
        appImageViews.add(ivWorkerEntrance);
        appImageViews.add(ivShoppingEntrance);
    }

//    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_nav, menu);
//        return true;
//    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Glide.with(HomePageActivity.this).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.author).into(photo);
        name.setText(sp.getUserName());
        telephone.setText(sp.getPhoneNumber());
        boolean isNetworkAvailable = netWorkUtil.isNetworkAvailable();
        if(!isNetworkAvailable){
            netWorkUtil.setNetwork();
        }else{
            //最后打开此句
//            new UpdateManager().checkAppUpdateAuto(HomePageActivity.this);
        }
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
