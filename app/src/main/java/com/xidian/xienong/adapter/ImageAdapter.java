package com.xidian.xienong.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class ImageAdapter extends PagerAdapter{

    private ArrayList<ImageView> viewlist;

    public ImageAdapter(ArrayList<ImageView> viewlist) {
        this.viewlist = viewlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewlist.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewlist.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 对ViewPager页号求模取出View列表中要显示的项
        Log.i("kmj", "viewlist.size():" + viewlist.size());
        ((ViewPager) container).addView(viewlist.get(position), 0);

        return viewlist.get(position);
    }

}
