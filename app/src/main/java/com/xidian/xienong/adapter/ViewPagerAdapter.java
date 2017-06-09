package com.xidian.xienong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xidian.xienong.agriculture.me.OrderFragment;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, String[] mTitles, List<Fragment> mFragments) {
        super(fm);
        this.mTitles = mTitles;
        this.mFragments = mFragments;
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
            ((OrderFragment)fragment).refreshData();
        }
    }
}
