package com.xidian.xienong.agriculture.resource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.CheckPhoneNumber;
import com.xidian.xienong.util.Constants;
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
 * Created by koumiaojuan on 2017/6/15.
 */

public class ModifyDriverActivity extends AppCompatActivity{

    private EditText driverName;
    private EditText driverSex;
    private EditText driverTele;
    private EditText driverIdentification;
    private Button modifyDriverBtn,deleteDriverBtn;
    private NetWorkUtil netWorkUtil;
    private RequestQueue requestQueue;
    private SharePreferenceUtil sp;
    private Driver driver;
    private Toolbar mToolbar;
    private OKHttp httpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_driver_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        driverSex.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ToastCustom.makeToast(ModifyDriverActivity.this, "性别不可修改");
            }
        });

        driverIdentification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ToastCustom.makeToast(ModifyDriverActivity.this, "身份证号不可修改");
            }
        });


        modifyDriverBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkInfoIsValid()){
                    new SweetAlertDialog(ModifyDriverActivity.this, SweetAlertDialog.TIP_TYPE)
                            .setTitleText("确认修改")
                            .setContentText("您确定要修改该司机吗")
                            .setCancelText("不，放弃")
                            .setConfirmText("好，修改")
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
                                    modifyDriver();
                                }
                            })
                            .show();
                }
            }
        });


        deleteDriverBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(ModifyDriverActivity.this, SweetAlertDialog.TIP_TYPE)
                        .setTitleText("删除司机")
                        .setContentText("您是否删除该类型的司机？")
                        .setCancelText("不，谢谢")
                        .setConfirmText("好，删除")
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
                                deleteTheDriver(driver);
                            }
                        })
                        .show();
            }
        });



    }

    private void deleteTheDriver(Driver driver) {
        // TODO Auto-generated method stub
        final Driver theDriver = driver;

        Map<String, String> map = new HashMap<String, String>();
        map.put("driver_id", theDriver.getDriver_id());
        map.put("worker_id", sp.getUserId());
        httpUrl.post(Url.DeleteSomeDriver,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    if (result.equals("SUCCESS")) {
                        Toast.makeText(ModifyDriverActivity.this, "删除成功！",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        Toast.makeText(ModifyDriverActivity.this, "删除失败，该司机有未完成订单",Toast.LENGTH_SHORT).show();
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


    private void modifyDriver() {
        // TODO Auto-generated method stub
        if (!netWorkUtil.isNetworkAvailable()) {
            netWorkUtil.setNetwork();
        } else{
            Map<String, String> map = new HashMap<String, String>();
            map.put("driver_id", driver.getDriver_id());
            if(driverSex.getText().toString().equals("男")){
                map.put("sex", "0");
            }else if(driverSex.getText().toString().equals("女")){
                map.put("sex", "1");
            }
            map.put("driver_name", driverName.getText().toString());
            map.put("driver_telephone", driverTele.getText().toString());
            map.put("driver_identification", driverIdentification.getText().toString());
            httpUrl.post(Url.ModifyDriver,map,new BaseCallback<String>(){
                @Override
                public void onRequestBefore() {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onSuccess(Response response, String resultResponse) {
                    Log.i("kmj","response:"+resultResponse);
                    JSONObject jb;
                    try {
                        jb = new JSONObject(resultResponse);
                        String result = jb.getString("reCode");
                        String message = jb.getString("message");
                        if(result.equals("SUCCESS")){
                            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
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

    }

    protected boolean checkInfoIsValid() {
        // TODO Auto-generated method stub
        if(driverName.getText().toString().equals("")){
            ToastCustom.makeToast(ModifyDriverActivity.this, "请填写司机姓名");
            return false;
        }
        if(driverSex.getText().toString().equals("")){
            ToastCustom.makeToast(ModifyDriverActivity.this, "请填写司机性别");
            return false;
        }
        if(driverTele.getText().toString().equals("")){
            ToastCustom.makeToast(ModifyDriverActivity.this, "请填写联系电话");
            return false;
        }
        if(driverIdentification.getText().toString().equals("")){
            ToastCustom.makeToast(ModifyDriverActivity.this, "请填写身份证号");
            return false;
        }
        if(!(driverSex.getText().toString().equals("女")||driverSex.getText().toString().equals("男"))){
            ToastCustom.makeToast(ModifyDriverActivity.this, "输入的性别有误");
            return false;
        }
        if(!CheckPhoneNumber.isPhoneNum(driverTele.getText().toString())){
            Toast.makeText(ModifyDriverActivity.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initData() {
        // TODO Auto-generated method stub
        httpUrl = OKHttp.getInstance();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        netWorkUtil = new NetWorkUtil(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        driver = (Driver) getIntent().getSerializableExtra("driver");
        driverName.setText(driver.getDriver_name());
        driverSex.setText(driver.getDriver_sex());
        driverTele.setText(driver.getDriver_telephone());
        driverIdentification.setText(driver.getDriver_identification());
    }

    private void initViews() {
        // TODO Auto-generated method stub
        driverName = (EditText)findViewById(R.id.tv_modify_driver_name);
        driverSex=(EditText)findViewById(R.id.tv_modify_driver_sex);
        driverTele = (EditText)findViewById(R.id.tv_modify_driver_tele);
        driverIdentification=(EditText)findViewById(R.id.tv_modify_driver_identification);
        modifyDriverBtn = (Button)findViewById(R.id.btn_modify_driver);
        mToolbar = (Toolbar)findViewById(R.id.modify_driver_toolbar);
        deleteDriverBtn = (Button)findViewById(R.id.btn_delete_driver);
    }






}
