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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ConsultAdapter;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.adapter.MachineAdapter;
import com.xidian.xienong.agriculture.order.RecommendOrderActivity;
import com.xidian.xienong.agriculture.resource.InputResourceActivity;
import com.xidian.xienong.model.Consult;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.util.RecyclerDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_entrance_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        machineEntranceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

        receiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachineEntranceActivity.this,RecommendOrderActivity.class);
                startActivity(intent);
            }
        });

        resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachineEntranceActivity.this,InputResourceActivity.class);
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
        setSupportActionBar(machineEntranceToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("咨讯");
        for(int i = 0;i < 10; i++){
            Consult c = new Consult();
            c.setTitle("农业文化遗产成为农村发展新动能");
            c.setSubTitle("6月5-7日，第四届全球重要农业文化遗产工作（中国）交流会在山东夏津召开。会议总结了我国全球重要农业文化遗…");
            list.add(c);
        }
        mLayoutManager =  new LinearLayoutManager(MachineEntranceActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new ConsultAdapter(MachineEntranceActivity.this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerDecoration(
                MachineEntranceActivity.this, LinearLayoutManager.VERTICAL, R.drawable.divider));
        recyclerView.setNestedScrollingEnabled(false);
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
