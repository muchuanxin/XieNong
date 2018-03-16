package com.xidian.xienong.agriculture.resource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
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

public class AddNewDriverActivity extends AppCompatActivity {

	private EditText driverName;
	private RadioGroup driverSexGroup;
	private EditText driverTele;
	private EditText driverIdentification;
	private Button newDriverBtn;
	private NetWorkUtil netWorkUtil;
	private RequestQueue requestQueue;
	private SharePreferenceUtil sp;
	private int sex=0;
	public static int FEMALE = 1;
	public static int MALE = 0;
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_driver_activity);
		initViews();
		initData();
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
		newDriverBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkInfoIsValid()){
					new SweetAlertDialog(AddNewDriverActivity.this, SweetAlertDialog.TIP_TYPE)
							.setTitleText("确认添加")
							.setContentText("您确定要添加该司机吗")
							.setCancelText("不，放弃")
							.setConfirmText("好，添加")
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
									addDriver();
								}
							})
							.show();
				}
			}
		});

		driverSexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId== R.id.radio_new_driver_sex_female){
					sex=FEMALE;
				}else if(checkedId== R.id.radio_new_driver_sex_male){
					sex=MALE;
				}
			}
		});

	}

	private void addDriver() {
		// TODO Auto-generated method stub
		if (!netWorkUtil.isNetworkAvailable()) {
			netWorkUtil.setNetwork();
		} else{
			StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.AddNewDriver,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							Log.i("kmj","response:"+response);
							JSONObject jb;
							try {
								jb = new JSONObject(response);
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
					}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					// TODO Auto-generated method stub
					volleyError.printStackTrace();
				}

			}) {

				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					// TODO Auto-generated method stub
					Map<String, String> map = new HashMap<String, String>();
					map.put("worker_id", sp.getUserId());
					map.put("sex", ""+sex);
					map.put("driver_name", driverName.getText().toString());
					map.put("driver_telephone", driverTele.getText().toString());
					map.put("driver_identification", driverIdentification.getText().toString());
					return map;
				}
			};
			requestQueue.add(stringrequest);
		}
	}

	protected boolean checkInfoIsValid() {
		// TODO Auto-generated method stub
		if(driverName.getText().toString().equals("")){
			ToastCustom.makeToast(AddNewDriverActivity.this, "请填写司机姓名");
			return false;
		}
		if(driverTele.getText().toString().equals("")){
			ToastCustom.makeToast(AddNewDriverActivity.this, "请填写联系电话");
			return false;
		}
		if(driverIdentification.getText().toString().equals("")){
			ToastCustom.makeToast(AddNewDriverActivity.this, "请填写身份证号");
			return false;
		}
		if(!CheckPhoneNumber.isPhoneNum(driverTele.getText().toString())){
			Toast.makeText(AddNewDriverActivity.this, "输入的手机号有误", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;

	}



	private void initData() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		netWorkUtil = new NetWorkUtil(getApplicationContext());
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		driverName = (EditText)findViewById(R.id.tv_new_driver_name);
		driverSexGroup = (RadioGroup)findViewById(R.id.radio_new_driver_sex_group);
		driverTele = (EditText)findViewById(R.id.tv_new_driver_tele);
		driverIdentification=(EditText)findViewById(R.id.tv_new_driver_identification);
		newDriverBtn = (Button)findViewById(R.id.btn_new_driver);
		mToolbar = (Toolbar)findViewById(R.id.add_driver_toolbar);
	}



}
