package com.xidian.xienong.agriculture.resource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.me.FeedbackActivity;
import com.xidian.xienong.home.HomePageActivity;
import com.xidian.xienong.home.LoginActivity;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/7/23.
 */

public class StatisticsActivity extends AppCompatActivity{

    private RelativeLayout rlMachineReservationState,rlDriverReservationState,rlMyShopping;
    private SharePreferenceUtil sp;
    private NetWorkUtil netWorkUtil;
    private OKHttp httpUrl;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);
        initViews();
        initDatas();
        initEvents();

    }

    private void initDatas() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        netWorkUtil = new NetWorkUtil(StatisticsActivity.this);
        httpUrl = OKHttp.getInstance();
    }

    private void initEvents() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlMachineReservationState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsLogin(v);
            }
        });

        rlDriverReservationState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsLogin(v);
            }
        });

        rlMyShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastCustom.makeToast(getApplicationContext(), "正在开发中...");
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
                        Intent intent = null;
                        if(((RelativeLayout)v) == rlMachineReservationState){
                            intent = new Intent(StatisticsActivity.this,MachineStateActivity.class);
                        }else if(((RelativeLayout)v) == rlDriverReservationState){
                            intent  = new Intent(StatisticsActivity.this,DriverStateActivity.class);
                        }else{
                            //.....
                        }
                        startActivity(intent);
                    }else{
                        Intent  intent = new Intent(StatisticsActivity.this, LoginActivity.class);
                        if(((RelativeLayout)v) == rlMachineReservationState){
                            intent.putExtra("clickView", "machine_reservation_statistics");
                        }else if(((RelativeLayout)v) == rlDriverReservationState){
                            intent.putExtra("clickView", "driver_reservation_statistics");
                        }else{
                            //.....
                        }
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
        toolBar = (Toolbar)findViewById(R.id.statistics_toolbar);
        rlMachineReservationState = (RelativeLayout)findViewById(R.id.rl_machine_reservation);
        rlDriverReservationState = (RelativeLayout)findViewById(R.id.rl_driver_reservation);
        rlMyShopping = (RelativeLayout)findViewById(R.id.rl_my_shopping);
    }

}
