package com.xidian.xienong.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.announcement.NewAnnounceActivity;
import com.xidian.xienong.agriculture.me.ChangePasswordActivity;
import com.xidian.xienong.agriculture.me.FeedbackActivity;
import com.xidian.xienong.agriculture.me.MyOrderActivity;
import com.xidian.xienong.agriculture.order.RecommendOrderActivity;
import com.xidian.xienong.agriculture.resource.DriverStateActivity;
import com.xidian.xienong.agriculture.resource.InputResourceActivity;
import com.xidian.xienong.agriculture.resource.MachineStateActivity;
import com.xidian.xienong.model.User;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.shoppingmall.brand.AffirmOrderActivity;
import com.xidian.xienong.shoppingmall.brand.CartActivity;
import com.xidian.xienong.shoppingmall.brand.CommodityDetailActivity;
import com.xidian.xienong.shoppingmall.me.AllMallOrderActivity;
import com.xidian.xienong.shoppingmall.me.AllTheMallOrderActivity;
import com.xidian.xienong.shoppingmall.me.MyAddressActivity;
import com.xidian.xienong.util.CheckPhoneNumber;
import com.xidian.xienong.util.ComfirmPassword;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by koumiaojuan on 2017/6/17.
 */

public class LoginActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText et_phoneNumber;
    private EditText  et_password;
    private Button btn_login;
    private TextView register;
    private SharePreferenceUtil sp;
    private OKHttp httpUrl;
    private User user;
    private static final int REQUEST_SIGNUP = 0;
    private TextView forgetPassword;
    private String clickView = "",login_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!validate()) {
                    onLoginFailed();
                    return;
                }
                btn_login.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("认证中...");
                progressDialog.show();
                submit(progressDialog);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void onLoginFailed() {
        btn_login.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;
        String phoneNumber = et_phoneNumber.getText().toString();
        String password = et_password.getText().toString();
        if (phoneNumber.isEmpty()){
            et_phoneNumber.setError("手机号不能为空");
            valid = false;
        }else if(!CheckPhoneNumber.isPhoneNum(phoneNumber)){
            et_phoneNumber.setError("手机号不合法");
            valid = false;
        }else{
            et_phoneNumber.setError(null);
        }

        if (password.isEmpty()){
            et_password.setError("密码不能为空");
            valid = false;
        }else if(!ComfirmPassword.isLegal3(password)){
            et_password.setError("密码至少6位");
            valid = false;
        }else{
            et_password.setError(null);
        }
        return valid;
    }

    private void submit(final ProgressDialog  progressDialog) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", et_phoneNumber.getText().toString());
        map.put("password", MD5Util.getMD5String(et_password.getText().toString()));
        Log.i("kmj", "telephone : " +et_phoneNumber.getText().toString());
        Log.i("kmj", "password : " + et_password.getText().toString());
        Log.i("kmj", "login_role : " + login_role);
        httpUrl.post(Url.UserLogin, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "UserLogin : " + Url.UserLogin);
            }

            @Override
            public void onFailure(okhttp3.Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(okhttp3.Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                progressDialog.dismiss();
                parseResponse(resultResponse);
            }
            @Override
            public void onError(okhttp3.Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });

    }

    private void parseResponse(String resultResponse) {
        // TODO Auto-generated method stub
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                user = new User();
                user.setUserId(jb.getString("user_id"));
                user.setUserName(jb.getString("user_name"));
                user.setTelephone(jb.getString("telephone"));
                user.setPassword(et_password.getText().toString());
                user.setHead_photo(jb.getString("head_photo"));
                user.setFarmer(jb.getBoolean("isFarmer"));
                user.setWorker(jb.getBoolean("isWorker"));
                user.setBrief(jb.getString("brief"));
                user.setSimple_address(jb.getString("simple_address"));
                user.setAddress(jb.getString("address"));
                user.setLantitude(jb.getDouble("lantitude"));
                user.setLongtitude(jb.getDouble("longtitude"));
                user.setTotal_integral(jb.getDouble("total_integral"));
                user.setToken(jb.getString("token"));
//                user.setLoginRole(jb.getString("login_role"));
                initUserInfo();
                goToActivity();
            }else{
                ToastCustom.makeToast(getApplicationContext(), "用户名或密码错误");

            }
            btn_login.setEnabled(true);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void goToActivity() {
        ToastCustom.makeToast(getApplicationContext(), "欢迎您，"+ sp.getUserName());
        if(clickView.equals("farmerOrder")){
            Intent  intent = new Intent(LoginActivity.this, MyOrderActivity.class);
            startActivity(intent);
        }else if(clickView.equals("publishRequirement")){
            Intent  intent = new Intent(LoginActivity.this, NewAnnounceActivity.class);
            startActivity(intent);
        }else if(clickView.equals("setting")){
            Intent  intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if(clickView.equals("feedback")){
            Intent  intent = new Intent(LoginActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }else if(clickView.equals("receiveOrder")){
            Intent  intent = new Intent(LoginActivity.this, RecommendOrderActivity.class);
            startActivity(intent);
        }else if(clickView.equals("resource")){//resource
            Intent  intent = new Intent(LoginActivity.this, InputResourceActivity.class);
            startActivity(intent);
        }else if(clickView.equals("machine_reservation_statistics")){//resource
            Intent  intent = new Intent(LoginActivity.this, MachineStateActivity.class);
            startActivity(intent);
        }else if(clickView.equals("driver_reservation_statistics")){//resource
            Intent  intent = new Intent(LoginActivity.this, DriverStateActivity.class);
            startActivity(intent);
        }else if(clickView.equals("commodity_detail")){//resource
//            Intent  intent = new Intent(LoginActivity.this, CommodityDetailActivity.class);
//            startActivity(intent);
        }else if(clickView.equals("CartActivity")) {//resource
            Intent intent = new Intent(LoginActivity.this, CartActivity.class);
            startActivity(intent);
        }else if(clickView.equals("AffirmOrderActivity")) {//resource
            Intent intent = new Intent(LoginActivity.this, AffirmOrderActivity.class);
            intent.putExtra("commodity", getIntent().getSerializableExtra("commodity"));
            startActivity(intent);
        }else if(clickView.equals("AllMallOrderActivity")) {//resource
            Intent intent = new Intent(LoginActivity.this, AllTheMallOrderActivity.class);
            intent.putExtra("currentItem", getIntent().getIntExtra("currentItem", 0));
            startActivity(intent);
        }else if(clickView.equals("MyAddressActivity")) {//resource
            Intent intent = new Intent(LoginActivity.this, MyAddressActivity.class);
            startActivity(intent);
        }
        finish();

    }

    private void initUserInfo() {
        sp.setUserId(user.getUserId());
        sp.setUserName(user.getUserName());
        sp.setphoneNumber(user.getTelephone());
        sp.setPassword(user.getPassword());
        sp.setHeadPhoto(user.getHead_photo());
        sp.setFarmer(user.isFarmer());
        sp.setWorker(user.isWorker());
        sp.setBrief(user.getBrief());
        sp.setSimpleAddress(user.getSimple_address());
        sp.setAddress(user.getAddress());
        sp.setLantitude(String.valueOf(user.getLantitude()));
        sp.setLongtitude(String.valueOf(user.getLongtitude()));
        sp.setTotalIntegral(String.valueOf(user.getTotal_integral()));
        sp.setToken(user.getToken());
//        sp.setLoginRole(user.getLoginRole());
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        httpUrl = OKHttp.getInstance();
        if(!sp.getUserId().equals("")){
            et_phoneNumber.setText(sp.getPhoneNumber());
            et_password.setText(sp.getPassword());
        }
        clickView = getIntent().getStringExtra("clickView");
        login_role = getIntent().getStringExtra("login_role");
        if (clickView==null){
            clickView = "";
        }
    }

    private void initViews() {
        // TODO Auto-generated method stub
        toolbar = (Toolbar)findViewById(R.id.login_toolbar);
        et_phoneNumber = (EditText)findViewById(R.id.et_phoneNumber);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.quick_register);
        forgetPassword = (TextView)findViewById(R.id.forget_password);
    }
}
