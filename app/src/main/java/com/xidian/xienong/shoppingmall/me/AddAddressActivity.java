package com.xidian.xienong.shoppingmall.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.AddressAdapter;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class AddAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout rl_address;
    private TextView address;
    private EditText name;
    private EditText telephone;
    private TextView address_detail;
    //private CheckBox checkBox;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initViews();
        initData();
        initEvent();
    }

    private void initEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddAddressActivity.this, AreaProvinceActivity.class), 1234);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = name.getText().toString();
                String telephone_str = telephone.getText().toString();
                String address_str = address.getText().toString();
                String address_detail_str = address_detail.getText().toString();
                if (!"".equals(name_str) && !"".equals(telephone_str) && !"请选择".equals(address_str) && !"".equals(address_detail_str)){
                    if (telephone_str.length()==11 && telephone_str.startsWith("1")){

                        SharePreferenceUtil sp = new SharePreferenceUtil(AddAddressActivity.this, Constants.SAVE_USER);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("user_id", sp.getUserId());
                        map.put("consignee_name", name_str);
                        map.put("phone", telephone_str);
                        map.put("area", address_str);
                        map.put("detail", address_detail_str);
                        OKHttp httpUrl = OKHttp.getInstance();
                        httpUrl.post(Url.AddMyAddress, map, new BaseCallback<String>() {
                            @Override
                            public void onRequestBefore() {
                                Log.i("mcx", "AddMyAddress : " + Url.AddMyAddress);
                            }

                            @Override
                            public void onFailure(Request request, Exception e) {
                                Log.i("mcx", "onFailure : " + e.toString());
                            }

                            @Override
                            public void onSuccess(Response response, String resultResponse) {
                                Log.i("mcx", "result : " + resultResponse);
                                try {
                                    JSONObject jb = new JSONObject(resultResponse);
                                    String result = jb.getString("reCode");
                                    if (result.equals("SUCCESS")) {
                                        ToastCustom.makeToast(AddAddressActivity.this, "添加成功");
                                        finish();
                                    }
                                    else {
                                        ToastCustom.makeToast(AddAddressActivity.this, "添加失败");
                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response response, int errorCode, Exception e) {
                                Log.i("mcx", "error : " + e.toString());
                            }
                        });
                    }
                    else {
                        ToastCustom.makeToast(AddAddressActivity.this, "请填写正确的手机号");
                    }
                }
                else {
                    ToastCustom.makeToast(AddAddressActivity.this, "请填写信息");
                }
            }
        });
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        address = (TextView) findViewById(R.id.address);
        name = (EditText) findViewById(R.id.name);
        telephone = (EditText) findViewById(R.id.telephone);
        address_detail = (EditText) findViewById(R.id.address_detail);
        //checkBox = (CheckBox) findViewById(R.id.checkbox);
        save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==4321 && data != null) {
            address.setText(data.getStringExtra("address"));
        }
    }
}
