package com.xidian.xienong.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ImageAdapter;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AtomicInteger what;
    private ViewGroup group;
    private SharePreferenceUtil sp;
    private ImageAdapter imageadapter;
    private boolean isContinue = true;
    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.guide_activity);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        group = (ViewGroup)findViewById(R.id.viewGroups);
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        if(sp.getisFirst()==true){
            sp.setIsFirst(false);
            initData();
            initEvents();
        }else{
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {
        // TODO Auto-generated method stub
        what = new AtomicInteger(0);
        int[] images = {R.drawable.start_page};
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();
        for(int i=0; i < 1;i++){
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(images[i]);
            imageviews.add(image);
        }
        imageadapter = new ImageAdapter(imageviews);
        viewPager.setAdapter(imageadapter);
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                Intent intent = new Intent(GuideActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    private void initEvents() {
        // TODO Auto-generated method stub
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
                if(position== count-1){
                    new Handler()
                    {
                        @Override
                        public void handleMessage(Message msg)
                        {
                            Intent intent = new Intent(GuideActivity.this,HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }.sendEmptyMessageDelayed(0, 2000);
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
    }

    private void initData() {
        // TODO Auto-generated method stub
        what = new AtomicInteger(0);
        int[] images = {R.drawable.guide_first,R.drawable.guide_second,R.drawable.guide_third};
        ArrayList<ImageView> imageviews = new ArrayList<ImageView>();
        for(int i=0; i < 3;i++){
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(images[i]);
            imageviews.add(image);
        }

        imageadapter = new ImageAdapter(imageviews);
        for (int i = 0; i < 3; i++) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
