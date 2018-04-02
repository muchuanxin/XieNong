package com.xidian.xienong;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xidian.xienong.agriculture.me.AboutUsActivity;
import com.xidian.xienong.agriculture.me.UpdateManager;
import com.xidian.xienong.tools.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayout1;
 //   private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initData() {
        // TODO Auto-generated method stub
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        // TODO Auto-generated method stub
        linearLayout1= (LinearLayout) findViewById(R.id.linearlayout_1);
       // linearLayout2= (LinearLayout) findViewById(R.id.linearlayout_2);
        linearLayout3= (LinearLayout) findViewById(R.id.linearlayout_bottom);
        mToolbar = (Toolbar)findViewById(R.id.registertoolbar);
    }
}
