package com.xidian.xienong.agriculture.me;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.DialogFactory;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangeNameActivity extends AppCompatActivity{

    private EditText changeName;
    private ImageButton clearBtn;
    private SharePreferenceUtil sp;
    private Toolbar mToolbar;
    private Button save;
    private RequestQueue requestQueue;
    private Dialog mDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_name_activity);
        initViews();
        initDatas();
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
        changeName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                clearBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if(s.equals("")){
                    clearBtn.setVisibility(View.GONE);
                }
            }
        });
        clearBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeName.setText("");
                clearBtn.setVisibility(View.GONE);
            }
        });

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.ChangeName,
                        new Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // TODO Auto-generated method stub
                                try {
                                    Log.i("kmj","-----response-----" + response);
                                    JSONObject jb = new JSONObject(response);
                                    String result = jb.getString("reCode");
                                    String message = jb.getString("message");
                                    if (result.equals("SUCCESS")) {
                                        Log.i("kmj","保存成功");
                                        Toast.makeText(ChangeNameActivity.this, "保存成功",Toast.LENGTH_SHORT).show();
                                        sp.setUserName(changeName.getText().toString());
                                        finish();
                                    }else{
                                        Toast.makeText(ChangeNameActivity.this, message,Toast.LENGTH_SHORT).show();
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
                        map.put("id", sp.getUserId());
                        map.put("name",changeName.getText().toString());
                        return map;
                    }
                };
                requestQueue.add(stringrequest);
            }
        });
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(ChangeNameActivity.this, Constants.SAVE_USER);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        changeName.setText(sp.getUserName());
    }

    private void initViews() {
        // TODO Auto-generated method stub
        mToolbar = (Toolbar)findViewById(R.id.change_name_toolbar);
        save = (Button)findViewById(R.id.btn_save_name);
        changeName = (EditText)findViewById(R.id.tv_change_name);
        clearBtn = (ImageButton)findViewById(R.id.btn_clear_name);
    }

    public void showRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = DialogFactory.creatRequestDialog(ChangeNameActivity.this, "正在保存...");
        mDialog.show();
    }

    public void dismissRequestDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }



}
