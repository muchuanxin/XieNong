package com.xidian.xienong.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText name;
    private EditText telephone;
    private EditText verifyCode;
    private EditText password;
    private EditText pswAgain;
    private Button btnRegister;
    private TextView getVerifyCode;
    private String verifyCode1;
    private String my_name;
    private String passwordFirst;
    private String passwordSecond;
    private String phoneNumber;
    private String tag = "";
    private SharePreferenceUtil sp;
    private Dialog mDialog = null;
    private OKHttp httpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
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
        telephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                tag = getVerifyCode.getHint().toString();
                getVerifyCode.setTextColor(Color.WHITE);
                getVerifyCode.setText(tag);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if(s.length()==0){
                    getVerifyCode.setText("");
                    getVerifyCode.setHint(tag);
                    getVerifyCode.setHintTextColor(Color.parseColor("#dcdcdc"));
                }
            }
        });

        getVerifyCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkTelephoneIsValid()){
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.TIP_TYPE)
                            .setTitleText("确认手机号码")
                            .setContentText("我们将发送验证码到这个手机号：" + telephone.getText().toString())
                            .setCancelText("不，谢谢")
                            .setConfirmText("好，发送")
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
                                    isRegister();
                                }
                            })
                            .show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!judge()){
                    onSignupFailed();
                    return;
                }else{
                    btnRegister.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("认证中...");
                    progressDialog.show();
                    submit(progressDialog);
                }
            }
        });
    }

    private void onSignupFailed() {
        btnRegister.setEnabled(true);
    }

    protected void submit(final ProgressDialog progressDialog) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name.getText().toString());
        map.put("telephone", telephone.getText().toString());
        map.put("password", MD5Util.getMD5String(password.getText().toString().trim()));
        httpUrl.post(Url.UserRegister, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "UserRegister : " + Url.UserRegister);
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
                        progressDialog.dismiss();
                        ToastCustom.makeToast(getApplicationContext(), "恭喜您，注册成功");
                        sp.setphoneNumber(telephone.getText().toString());
                        sp.setPassword(password.getText().toString());
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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


    protected boolean judge() {
        // TODO Auto-generated method stub
        boolean valid = true;
        my_name = name.getText().toString().trim();
        phoneNumber = telephone.getText().toString().trim();
        passwordFirst = password.getText().toString().trim();
        passwordSecond = pswAgain.getText().toString().trim();
        if (my_name.isEmpty()) {
            name.setError("昵称不为空");
            valid = false;
        } else {
            name.setError(null);
        }

        if (phoneNumber.isEmpty()) {
            telephone.setError("请输入手机号");
            valid = false;
        } else if(!CheckPhoneNumber.isPhoneNum(phoneNumber)){
            telephone.setError("手机号不合法");
            valid = false;
        }else{
            telephone.setError(null);
        }

        if (verifyCode.getText().toString().equals("")) {
            ToastCustom.makeToast(RegisterActivity.this, "请获取校验码");
            valid = false;
        }else{
            if (!verifyCode.getText().toString().equals(verifyCode1)) {
                ToastCustom.makeToast(RegisterActivity.this, "验证码错误 ");
                valid = false;
            }
        }

        if (passwordFirst.isEmpty() || !ComfirmPassword.isLegal3(passwordFirst)) {
            password.setError("密码至少6位");
            valid = false;
        } else {
            password.setError(null);
        }

        if (passwordSecond.isEmpty() || !ComfirmPassword.isLegal3(passwordFirst)) {
            pswAgain.setError("密码至少6位");
            valid = false;
        } else {
            pswAgain.setError(null);
        }

        if (!passwordFirst.equals(passwordSecond)) {
            ToastCustom.makeToast(RegisterActivity.this, "确认密码与密码不相符");
            valid = false;
        }
        return valid;
    }

    protected void isRegister(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("telephone", telephone.getText().toString());
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
        map.put("tel", telephone.getText().toString());
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
                        Toast.makeText(RegisterActivity.this, "正在发送验证码",Toast.LENGTH_SHORT).show();
                    }else {
                        Log.i("kmj", "发送失败");
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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


    protected boolean checkTelephoneIsValid() {
        // TODO Auto-generated method stub
        if(telephone.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CheckPhoneNumber.isPhoneNum(telephone.getText().toString())){
            Toast.makeText(RegisterActivity.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
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
        toolbar = (Toolbar)findViewById(R.id.registertoolbar);
        name = (EditText)findViewById(R.id.input_name);
        telephone = (EditText)findViewById(R.id.input_mobile);
        verifyCode =(EditText)findViewById(R.id.et_verify);
        password = (EditText)findViewById(R.id.input_password);
        pswAgain =(EditText)findViewById(R.id.input_reEnterPassword);
        getVerifyCode =(TextView)findViewById(R.id.getVerifyCode);
        btnRegister = (Button)findViewById(R.id.btn_register);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
