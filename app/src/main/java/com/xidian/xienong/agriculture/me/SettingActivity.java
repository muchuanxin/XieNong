package com.xidian.xienong.agriculture.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.announcement.NewAnnounceActivity;
import com.xidian.xienong.agriculture.find.FindActivity;
import com.xidian.xienong.home.HomePageActivity;
import com.xidian.xienong.home.LoginActivity;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class SettingActivity extends AppCompatActivity{

    private RelativeLayout changePassword;
    private RelativeLayout exit;
    private SharePreferenceUtil sp;
    private Toolbar mToolbar;
    private OKHttp httpUrl;

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
        httpUrl = OKHttp.getInstance();
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changePassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                checkIsLogin(changePassword);
            }
        });
        exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
           // TODO Auto-generated method stub
                sp.setUserId("");
                sp.setToken("");
                sp.setphoneNumber("");
                sp.setUserName("");
                Intent intent  = new Intent(SettingActivity.this,HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkIsLogin(final View v) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", sp.getPhoneNumber());
        map.put("password", MD5Util.getMD5String(sp.getPassword()));
        map.put("token", sp.getToken());
        httpUrl.post(Url.IsLogin, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "IsLogin : " + Url.IsLogin);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String msg = jb.getString("message");
                    if(result.equals("SUCCESS")){
                        Intent intent  = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                        startActivity(intent);
                    }else{
                        Intent  intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.putExtra("clickView", "setting");
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
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

