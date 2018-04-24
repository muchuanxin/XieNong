package com.xidian.xienong.shoppingmall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.FragmentAdapter;
import com.xidian.xienong.shoppingmall.brand.BrandFragment;
import com.xidian.xienong.shoppingmall.classification.ClassificationFragment;
import com.xidian.xienong.shoppingmall.hotcommodity.HotcommodityFragment;
import com.xidian.xienong.shoppingmall.me.MallOrderFragment;
import com.xidian.xienong.shoppingmall.rank.RankFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class ShoppingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String[] titles = new String[]{"热门","分类", "品牌", "排行", "订单"};
    private int[] mImgs=new int[]{R.drawable.selector_tab_hot,R.drawable.selector_tab_classification, R.drawable.selector_tab_brank, R.drawable.selector_tab_rank,
            R.drawable.selector_tab_mallorder};
    //Tab标题集合
    private List<String> mTitles = new ArrayList<>();
    //ViewPage选项卡页面集合
    private List<Fragment> listFragment = new ArrayList<Fragment>();
    private FragmentAdapter adapter;
    private SearchView mSearchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_activity);
        initViews();
        initData();
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        for (int i = 0; i < titles.length; i++) {
            mTitles.add(titles[i]);
        }
        addFragment();
        initTabLayout();

    }

    private void initTabLayout() {
        adapter = new FragmentAdapter(getSupportFragmentManager(),listFragment,mTitles);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来
        getSupportActionBar().setTitle(titles[0]);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mViewPager.setOnPageChangeListener(this);
        for (int i = 0; i < mTitles.size(); i++) {
            TabLayout.Tab itemTab = mTabLayout.getTabAt(i);
            if (itemTab != null) {
                itemTab.setCustomView(R.layout.item_tab);
                TextView textView = (TextView) itemTab.getCustomView().findViewById(R.id.tv_name);
                textView.setText(mTitles.get(i));
                ImageView imageView= (ImageView) itemTab.getCustomView().findViewById(R.id.iv_img);
                imageView.setImageResource(mImgs[i]);
            }
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    private void addFragment() {
        HotcommodityFragment hotCommodityFragment = new HotcommodityFragment();
        ClassificationFragment classficationFragment = new ClassificationFragment();
        BrandFragment brandFragment = new BrandFragment();
        RankFragment rankFragment = new RankFragment();
        MallOrderFragment mallOrderFragment = new MallOrderFragment();
        listFragment.add(hotCommodityFragment);
        listFragment.add(classficationFragment);
        listFragment.add(brandFragment);
        listFragment.add(rankFragment);
        listFragment.add(mallOrderFragment);
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.shopping_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.shopping_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.shopping_tablayout);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setTitle(titles[position]);
        if(position == 0){
            mSearchView.setVisibility(View.VISIBLE);
        }else{
            mSearchView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//指定Toolbar上的视图文件
        final MenuItem item = menu.findItem(R.id.search_content);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        doSearch();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:break;

        }
        return super.onOptionsItemSelected(item);
    }
}
