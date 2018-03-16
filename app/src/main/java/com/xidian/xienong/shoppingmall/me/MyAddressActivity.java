package com.xidian.xienong.shoppingmall.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.AddressAdapter;
import com.xidian.xienong.adapter.CartAdapter;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.shoppingmall.brand.CartActivity;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class MyAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView add_new_address;
    private ListView address_list;
    private OKHttp httpUrl;
    private List<Map<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAddressActivity.this, AddAddressActivity.class));
            }
        });
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharePreferenceUtil sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", sp.getUserId());
        httpUrl = OKHttp.getInstance();
        httpUrl.post(Url.GetAllMyAddress, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("mcx", "GetAllMyAddress : " + Url.GetAllMyAddress);
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
                        JSONArray allAdress = jb.getJSONArray("allAdress");
                        list = new ArrayList<Map<String, String>>();
                        for (int i=0; i<allAdress.length(); i++){
                            JSONObject temp = allAdress.getJSONObject(i);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("address_id", temp.getString("address_id"));
                            map.put("consignee_name", temp.getString("consignee_name"));
                            map.put("phone", temp.getString("phone"));
                            map.put("area", temp.getString("area"));
                            map.put("detail", temp.getString("detail"));
                            map.put("is_default", temp.getString("is_default"));
                            list.add(map);
                        }
                        AddressAdapter adapter = new AddressAdapter(MyAddressActivity.this, list);
                        address_list.setAdapter(adapter);
                    }
                    else {
                        ToastCustom.makeToast(MyAddressActivity.this, "获取数据失败");
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

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        add_new_address = (TextView) findViewById(R.id.add_new_address);
        address_list = (ListView) findViewById(R.id.address_list);
    }
}
