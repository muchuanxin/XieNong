package com.xidian.xienong.agriculture.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.find.FindActivity;
import com.xidian.xienong.home.LoginActivity;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.ComfirmPassword;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.MD5Util;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

	private Button btnSubmit;
	private EditText etOld, etNew, etAgain;
	private SharePreferenceUtil sp;
	private String newPassword;
	private Toolbar mToolbar;
	private OKHttp httpUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_activity);
		initViews();
		initDatas();
		initEvents();
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		mToolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submit();
			}
		});
	}



	private void initDatas() {
		httpUrl = OKHttp.getInstance();
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		sp = new SharePreferenceUtil(this, Constants.SAVE_USER);
		if(sp.getPassword().equals("")){
			etOld.setEnabled(true);
		}else{
			etOld.setText(sp.getPassword());
			etOld.setEnabled(false);
		}


	}

	private void initViews() {
		// TODO Auto-generated method stub
		btnSubmit = (Button) findViewById(R.id.btn_change_pass_submit);
		etOld = (EditText) findViewById(R.id.et_change_pass_old);
		etNew = (EditText) findViewById(R.id.et_change_pass_new);
		etAgain = (EditText) findViewById(R.id.et_change_pass_again);
		mToolbar = (Toolbar)findViewById(R.id.change_psw_toolbar);
	}

	private void submit() {
		if(judge()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", sp.getUserId());
			map.put("old_password", MD5Util.getMD5String(sp.getPassword()));
			map.put("new_password", MD5Util.getMD5String(newPassword));
			httpUrl.post(Url.ResetPassword,map,new BaseCallback<String>(){
				@Override
				public void onRequestBefore() {
				}

				@Override
				public void onFailure(okhttp3.Request request, Exception e) {
				}

				@Override
				public void onSuccess(Response response, String resultResponse) {
					Log.i("kmj", "result : " + resultResponse);
					try {
						JSONObject jb = new JSONObject(resultResponse);
						String result = jb.getString("reCode");
						String message = jb.getString("message");
						if (result.equals("SUCCESS")) {
							//设置新密码
							sp.setPassword(newPassword);
							Toast.makeText(ChangePasswordActivity.this, "密码修改成功",Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(ChangePasswordActivity.this, message,Toast.LENGTH_SHORT).show();
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


	private boolean judge() {
		String curr_pwd = etOld.getText().toString().trim();
		newPassword = etNew.getText().toString().trim();
		String again_pwd = etAgain.getText().toString().trim();
		String v_password = sp.getPassword();
		if (curr_pwd.equals("")) {
			Toast.makeText(ChangePasswordActivity.this, "请输入密码",Toast.LENGTH_SHORT).show();
			return false;
		}
		if (newPassword.equals("")) {
			Toast.makeText(ChangePasswordActivity.this, "请输入新密码",Toast.LENGTH_SHORT).show();
			return false;
		}
		if (again_pwd.equals("")) {
			Toast.makeText(ChangePasswordActivity.this, "请输入确认密码",Toast.LENGTH_SHORT).show();
			return false;
		}
		// 密码必须是9为以上字母与数字的组合
		if (!ComfirmPassword.isLegal3(newPassword)) {
			Toast.makeText(ChangePasswordActivity.this, "密码至少6位",Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!newPassword.equals(again_pwd)) {
			Toast.makeText(ChangePasswordActivity.this, "确认密码与新密码不相符",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
