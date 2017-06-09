package com.xidian.xienong.agriculture.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.me.AboutUsActivity;
import com.xidian.xienong.agriculture.me.FeedbackActivity;
import com.xidian.xienong.agriculture.me.MyOrderActivity;
import com.xidian.xienong.agriculture.me.SettingActivity;
import com.xidian.xienong.util.SnackbarUtil;


/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class FindActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private FloatingActionButton mFloatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_activity);
        initViews();
        configViews();
        initEvents();

    }

    private void initEvents() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtil.show(FindActivity.this,v, getString(R.string.confirm_publish_announce), 1);
            }
        });
    }

    private void configViews() {
        // 设置显示Toolbar
        setSupportActionBar(mToolbar);
        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        mNavigationView.inflateHeaderView(R.layout.header_navigation);
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
                    case R.id.nav_menu_order :
                        intent = new Intent(FindActivity.this, MyOrderActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_menu_feedback:
                        intent = new Intent(FindActivity.this, FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_menu_aboutus:
                        intent = new Intent(FindActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_menu_setting:
                        intent = new Intent(FindActivity.this, SettingActivity.class);
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

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_coordinatorlayout);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);
        mNavigationView = (NavigationView) findViewById(R.id.id_navigationview);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
    }

}
