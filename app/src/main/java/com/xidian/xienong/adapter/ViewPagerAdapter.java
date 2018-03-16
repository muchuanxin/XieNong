package com.xidian.xienong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xidian.xienong.agriculture.me.OrderFragment;
import com.xidian.xienong.agriculture.order.GrabOrderFragment;
import com.xidian.xienong.shoppingmall.me.MallAllOrderFragment;
import com.xidian.xienong.shoppingmall.me.MallMyOrderFragment;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private List<Fragment> mFragments;
    private String mark;

    public ViewPagerAdapter(FragmentManager fm, String[] mTitles, List<Fragment> mFragments,String mark) {
        super(fm);
        this.mTitles = mTitles;
        this.mFragments = mFragments;
        this.mark = mark;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

     @Override
     public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for(Fragment fragment : mFragments){
            if(mark.equals("my_order")){
                ((OrderFragment)fragment).refreshData();
            }else if(mark.equals("my_mall_order")){
        //       ((MallAllOrderFragment)fragment).refreshData();
                ((MallMyOrderFragment)fragment).refreshData();
            }
            else{
                ((GrabOrderFragment)fragment).refreshData();
            }

        }
    }
}
