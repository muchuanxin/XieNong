package com.xidian.xienong.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.FragmentAdapter;
import com.xidian.xienong.main.Agriculturalproduct.AgricutualProductFragment;
import com.xidian.xienong.main.agricultualtechnique.AgricutualTechniqueFragment;
import com.xidian.xienong.main.agriculturalmanage.AgricultualManageFragment;
import com.xidian.xienong.main.mainpage.MainPageFragment;
import com.xidian.xienong.shoppingmall.brand.BrandFragment;
import com.xidian.xienong.shoppingmall.classification.ClassificationFragment;
import com.xidian.xienong.shoppingmall.me.MallOrderFragment;
import com.xidian.xienong.shoppingmall.rank.RankFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class PageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    public  static ViewPager mViewPager;
//    private TabLayout mTabLayout;
    public static  TabLayout mTabLayout;
//    private String[] titles = new String[]{"首页","农务", "农技", "农产品", "我的"};
    public static String[] titles = new String[]{"首页","农务", "农技", "农产品", "我的"};
    public static  int[] mImgs=new int[]{R.drawable.selector_tab_shouye,R.drawable.selector_tab_nongwu, R.drawable.selector_tab_nongji, R.drawable.selector_tab_nongchanpin,
            R.drawable.selector_tab_wode};
    //Tab标题集合
    private List<String> mTitles = new ArrayList<>();
    //ViewPage选项卡页面集合
//    private List<Fragment> listFragment = new ArrayList<Fragment>();
    public  static  FragmentAdapter adapter;
    public static  List<Fragment> listFragment = new ArrayList<Fragment>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        initData();
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//这句代码使启用Activity回退功能，并显示Toolbar上的左侧不显示回退图标
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
       // getSupportActionBar().setTitle(titles[0]);
        setCustomActionBar(titles[0]);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mViewPager.setOnPageChangeListener(this);
        for (int i = 0; i < mTitles.size(); i++) {
            TabLayout.Tab itemTab = mTabLayout.getTabAt(i);
            if (itemTab != null) {
                itemTab.setCustomView(R.layout.item_tab_2);
              //  TextView textView = (TextView) itemTab.getCustomView().findViewById(R.id.tv_name);
                //textView.setText(mTitles.get(i));
                ImageView imageView= (ImageView) itemTab.getCustomView().findViewById(R.id.iv_img);
                imageView.setImageResource(mImgs[i]);
            }
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    public void setCustomActionBar(String title) {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView tv=(TextView)mActionBarView.findViewById(R.id.bar_title);
        tv.setText(title);
    }
    private void addFragment() {
        // 首页
        MainPageFragment mainPageFragment = new MainPageFragment();
      /*  ClassificationFragment classficationFragment = new ClassificationFragment();*/

       //农务
        AgricultualManageFragment agricultualManageFragment= new AgricultualManageFragment();


        //这两个现在为空，有待后面改变：农技 农产品
        AgricutualTechniqueFragment agricutualTechniqueFragment = new AgricutualTechniqueFragment();
        AgricutualProductFragment agricutualProductFragment = new AgricutualProductFragment();

        //我的订单部分，保持不变
        MallOrderFragment mallOrderFragment = new MallOrderFragment();

        listFragment.add(mainPageFragment);
        listFragment.add(agricultualManageFragment);
        listFragment.add(agricutualProductFragment);
        listFragment.add(agricutualTechniqueFragment);
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
      //  getSupportActionBar().setTitle(titles[position]);
        setCustomActionBar(titles[position]);
  /*      if(position == 0){
            mSearchView.setVisibility(View.VISIBLE);
        }else{
            mSearchView.setVisibility(View.GONE);
        }*/

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     /*   getMenuInflater().inflate(R.menu.menu_main, menu);//指定Toolbar上的视图文件*/
      /*  final MenuItem item = menu.findItem(R.id.search_content);*/
    /*    mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setOnQueryTextListener(this);*/
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
