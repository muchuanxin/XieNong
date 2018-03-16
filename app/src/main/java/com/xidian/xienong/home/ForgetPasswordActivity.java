package com.xidian.xienong.home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.CheckPhoneNumber;
import com.xidian.xienong.util.ComfirmPassword;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.MyCountDownTimer;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by koumiaojuan on 2017/6/17.
 */

public class ForgetPasswordActivity extends AppCompatActivity{

    private SharePreferenceUtil sp;
    private Dialog mDialog = null;
    private EditText your_telephone;
    private EditText verifyCode;
    private TextView getVerifyCode;
    private EditText reset_password;
    private EditText reset_pswAgain;
    private Button btnReset;
    private String newPasswordFirst;
    private String newPasswordSecond;
    private String phoneNumber;
    private String tag="",verifyCode1="";
    private OKHttp httpUrl;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);
        initView();
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
        getVerifyCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkTelephoneIsValid()){
                    verifyCode1 = getReCode(6);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("tel", your_telephone.getText().toString());
                    map.put("verification_code", verifyCode1);
                    httpUrl.post(Url.VerificationCode, map, new BaseCallback<String>() {
                        @Override
                        public void onRequestBefore() {
                            Log.i("kmj", "VerificationCode : " + Url.VerificationCode);
                        }

                        @Override
                        public void onFailure(okhttp3.Request request, Exception e) {
                            Log.i("kmj", "onFailure : " + e.toString());
                        }

                        @Override
                        public void onSuccess(okhttp3.Response response, String resultResponse) {
                            Log.i("kmj", "result : " + resultResponse);
                            try {
                                JSONObject jb = new JSONObject(resultResponse);
                                String result = jb.getString("reCode");
                                String message = jb.getString("message");
                                if (result.equals("SUCCESS")) {
                                    Log.i("kmj","发送成功");
                                    MyCountDownTimer countDownTimer = new MyCountDownTimer(61000, 1000, getVerifyCode);
                                    countDownTimer.start();
                                    Toast.makeText(ForgetPasswordActivity.this, "正在发送验证码",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ForgetPasswordActivity.this, message,Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(okhttp3.Response response, int errorCode, Exception e) {
                            Log.i("kmj", "error : " + e.toString());
                        }
                    });
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!judge()){
                    onSignupFailed();
                    return;
                }
                else{
                    btnReset.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("重置密码中...");
                    progressDialog.show();
                    submit(progressDialog);
                }
            }
        });

    }

    protected void submit(ProgressDialog progressDialog) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", your_telephone.getText().toString().trim());
        map.put("new_password", MD5Util.getMD5String(reset_password.getText().toString().trim()));
        httpUrl.post(Url.ResetUserPassword, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "ResetUserPassword : " + Url.ResetUserPassword);
            }

            @Override
            public void onFailure(okhttp3.Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(okhttp3.Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equalsIgnoreCase("SUCCESS")) {
                        Log.i("kmj", "重置成功");
                        Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        sp.setphoneNumber(your_telephone.getText().toString());
                        sp.setPassword(reset_password.getText().toString().trim());
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(okhttp3.Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void onSignupFailed() {
        btnReset.setEnabled(true);
    }

    protected boolean judge() {
        // TODO Auto-generated method stub
        boolean valid = true;
        newPasswordFirst = reset_password.getText().toString().trim();
        newPasswordSecond = reset_pswAgain.getText().toString().trim();
        phoneNumber = your_telephone.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            your_telephone.setError("请输入手机号");
            valid = false;
        } else if(!CheckPhoneNumber.isPhoneNum(phoneNumber)){
            your_telephone.setError("手机号不合法");
            valid = false;
        }else{
            your_telephone.setError(null);
        }

        if (verifyCode.getText().toString().equals("")) {
            ToastCustom.makeToast(ForgetPasswordActivity.this, "请获取校验码");
            valid = false;
        }else{
            if (!verifyCode.getText().toString().equals(verifyCode1)) {
                ToastCustom.makeToast(ForgetPasswordActivity.this, "验证码错误 ");
                valid = false;
            }
        }

        if (newPasswordFirst.isEmpty() || !ComfirmPassword.isLegal3(newPasswordFirst)) {
            reset_password.setError("密码至少6位");
            valid = false;
        } else {
            reset_password.setError(null);
        }

        if (newPasswordSecond.isEmpty() || !ComfirmPassword.isLegal3(newPasswordSecond)) {
            reset_pswAgain.setError("密码至少6位");
            valid = false;
        } else {
            reset_pswAgain.setError(null);
        }

        if (!newPasswordFirst.equals(newPasswordSecond)) {
            ToastCustom.makeToast(ForgetPasswordActivity.this, "确认密码与密码不相符");
            valid = false;
        }
        return valid;
    }

    protected void isRegister(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", your_telephone.getText().toString());
        httpUrl.post(Url.UserIsRegister, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "IsLogin : " + Url.UserIsRegister);
            }

            @Override
            public void onFailure(okhttp3.Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(okhttp3.Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("fail")) {
                        ToastCustom.makeToast(getApplicationContext(), "该手机号已注册，请直接登录");
                    }else{
                        requestVerifyCode();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(okhttp3.Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void requestVerifyCode() {
        verifyCode1 = getReCode(6);
        Map<String, String> map = new HashMap<String, String>();
        map.put("tel", your_telephone.getText().toString());
        map.put("verification_code", verifyCode1);
        httpUrl.post(Url.VerificationCode, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "VerificationCode : " + Url.VerificationCode);
            }

            @Override
            public void onFailure(okhttp3.Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(okhttp3.Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("SUCCESS")) {
                        MyCountDownTimer countDownTimer = new MyCountDownTimer(61000, 1000, getVerifyCode);
                        countDownTimer.start();
                        Toast.makeText(ForgetPasswordActivity.this, "正在发送验证码",Toast.LENGTH_SHORT).show();
                    }else {
                        Log.i("kmj", "发送失败");
                        Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(okhttp3.Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    // 随即生成6位的验证码
    private String getReCode(int num) {
        // TODO Auto-generated method stub
        String reCode = "";
        int x = 0;
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            x = ((r.nextInt() % 10 + 10) % 10);
            reCode += x;
        }
        return reCode;
    }


    private boolean checkTelephoneIsValid() {
        // TODO Auto-generated method stub
        if(your_telephone.getText().toString().equals("")){
            Toast.makeText(ForgetPasswordActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CheckPhoneNumber.isPhoneNum(your_telephone.getText().toString())){
            Toast.makeText(ForgetPasswordActivity.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        httpUrl = OKHttp.getInstance();
    }

    private void initView() {
        // TODO Auto-generated method stub
        toolbar = (Toolbar)findViewById(R.id.forget_password_toolbar);
        your_telephone = (EditText)findViewById(R.id.your_phoneNumber);
        verifyCode =(EditText)findViewById(R.id.et_reset_verify);
        getVerifyCode =(TextView)findViewById(R.id.reset_GetVerifyCode);
        reset_password = (EditText)findViewById(R.id.your_new_psw);
        reset_pswAgain =(EditText)findViewById(R.id.your_new_psw_again);
        btnReset = (Button)findViewById(R.id.btn_reset);
    }

}
