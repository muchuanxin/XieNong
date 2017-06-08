package com.xidian.xienong.agriculture.announcement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.util.Constants;

/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class MapChoosePointActivity extends AppCompatActivity{
    private WebView webView;
    private Button confirm_location;
    private ImageButton back;
    private TextView title;
    private String whichActivity="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location_point);
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        confirm_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(Constants.haveConfirmed){
                    Constants.haveConfirmed = false;
                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(MapChoosePointActivity.this,"请先选择农田地址",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        whichActivity = getIntent().getStringExtra("activity");
        if(whichActivity.equals("new_announce_activity")){
            title.setText("农田位置");
        }else{
            title.setText("农机位置");
        }
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl("file:///android_asset/map_choose_point.html");
        //设置Web视图
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    private void initViews() {
        // TODO Auto-generated method stub
        webView = (WebView)findViewById(R.id.crop_webview);
        confirm_location = (Button)findViewById(R.id.btn_confirm_location);
        back = (ImageButton)findViewById(R.id.btn_back_announce);
        title = (TextView)findViewById(R.id.tv_login_title);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }


}
