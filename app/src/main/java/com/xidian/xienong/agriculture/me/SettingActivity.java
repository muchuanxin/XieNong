package com.xidian.xienong.agriculture.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.xidian.xienong.R;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class SettingActivity extends AppCompatActivity{

    private RelativeLayout changePassword;
    private RelativeLayout exit;

    private SharePreferenceUtil sp;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        sp = new SharePreferenceUtil(this, Constants.SAVE_USER);
        initViews();
        initData();
        initEvents();

    }

    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
     /*   back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
           *//*     Intent intent =null;
                if(sp.getisWorker().equals("0")){
                    intent  = new Intent(SettingActivity.this,FarmerMainActivity.class);
                    intent.setType("setting");
                }else{
                    intent  = new Intent(SettingActivity.this,WorkerMainActivity.class);
                    intent.setType("setting");
                }
                startActivity(intent);
                finish();*//*
            }
        });*/
        changePassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent  = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
             /*   // TODO Auto-generated method stub
                Intent intent  = new Intent(SettingActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
            }
        });
    }

    private void initViews() {
        // TODO Auto-generated method stub

        changePassword = (RelativeLayout)findViewById(R.id.rl_changePassword);
        exit = (RelativeLayout)findViewById(R.id.rl_exit_login);
        mToolbar = (Toolbar)findViewById(R.id.setting_toolbar);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

}

