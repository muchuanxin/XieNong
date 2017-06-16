package com.xidian.xienong.home;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.agriculture.find.FindActivity;
import com.xidian.xienong.agriculture.me.AboutUsActivity;
import com.xidian.xienong.agriculture.me.FeedbackActivity;
import com.xidian.xienong.agriculture.me.InformationActivity;
import com.xidian.xienong.agriculture.me.SettingActivity;
import com.xidian.xienong.shoppingmall.ShoppingActivity;
import com.xidian.xienong.util.ListenedScrollView;
import com.xidian.xienong.util.MarqueeView;
import com.xidian.xienong.util.ViewUpSearch;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        initViews();
        configViews();
        initData();
        initEvents();
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
                    case R.id.nav_menu_feedback:
                        intent = new Intent(HomePageActivity.this, FeedbackActivity.class);
                        startActivity(intent);
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

    private void initEvents() {
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
        if (what.get() > images.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    private void initData() {
        what = new AtomicInteger(0);
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();
        for(int i=0; i < 4;i++){
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
        List<String> items = new ArrayList<>();
        items.add("发挥农机主力军作用");
        items.add("第六届世界农机峰会推介交流会在京召开");
        items.add("中国农机企业走出去");
        items.add("用大数据为农机植入“智慧芯”");
        marquee.startWithList(items);
        search.setSearchContent("最新上市热销农产品");
    }
    private void initViews() {
        homepageToolbar = (Toolbar)findViewById(R.id.homepage_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_homepage_drawerlayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_homepage_coordinatorlayout);
        mNavigationView = (NavigationView) findViewById(R.id.id_homepage_navigationview);
        viewPager = (ViewPager)findViewById(R.id.homepage_viewPager);
        group = (ViewGroup)findViewById(R.id.homepage_viewGroups);
        marquee = (MarqueeView)findViewById(R.id.headline);
        search = (ViewUpSearch)findViewById(R.id.search);
        scrollView = (ListenedScrollView)findViewById(R.id.homepage_scrollView);
        machineEntrance = (TextView)findViewById(R.id.machine_entrance);
        workerEntrance = (TextView)findViewById(R.id.worker_entrance);
        shoppingEntrance = (TextView)findViewById(R.id.shopping_entrance);
    }

//    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_nav, menu);
//        return true;
//    }
}
