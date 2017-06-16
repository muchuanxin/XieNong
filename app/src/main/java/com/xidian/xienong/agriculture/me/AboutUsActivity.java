package com.xidian.xienong.agriculture.me;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.tools.SweetAlertDialog;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class AboutUsActivity extends AppCompatActivity {

    private TextView version;
    private String appVersionName;
    private RelativeLayout check_update;
    private RelativeLayout contact_us;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);
        setTitle("关于");
        appVersionName = getAPPVersion();
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
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        check_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btn_version_update();
            }
        });

        contact_us.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(AboutUsActivity.this, SweetAlertDialog.TIP_TYPE)
                        .setTitleText("确认手机号码")
                        .setContentText("我们将向客服拨打电话：18710355634")
                        .setCancelText("不，谢谢")
                        .setConfirmText("好，拨打")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18710355634"));
                                if (ActivityCompat.checkSelfPermission(AboutUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    public void btn_version_update() {
        UpdateManager.getUpdateManager().checkAppUpdate(AboutUsActivity.this, true);
    }

    private void initViews() {
        // TODO Auto-generated method stub
        version = (TextView)findViewById(R.id.version);
        version.setText("携农V"+appVersionName);
        check_update = (RelativeLayout)findViewById(R.id.rl_check_update);
        contact_us = (RelativeLayout)findViewById(R.id.rl_contact_us);
        mToolbar = (Toolbar)findViewById(R.id.about_us_toolbar);
    }



    /**
     * 获取软件版本号
     */
    private String getAPPVersion() {
        String appVersionName = "";
        PackageManager pm = this.getPackageManager();//得到PackageManager对象
        try {
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);//得到PackageInfo对象，封装了一些软件包的信息在里面
            int appVersion = pi.versionCode;//获取清单文件中versionCode节点的值
            appVersionName = pi.versionName;
            Log.i("kmj", "appVersion="+appVersion+",appVersionName="+appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }

}
