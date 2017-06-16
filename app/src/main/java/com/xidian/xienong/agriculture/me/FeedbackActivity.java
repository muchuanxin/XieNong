package com.xidian.xienong.agriculture.me;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class FeedbackActivity extends AppCompatActivity{

    private SharePreferenceUtil sp;
    private EditText content;
    private TextView remainingWords;
    private Button submit;
    private CharSequence temp;
    private int totalNumber = 100;
    private int number ;
    private int selectionStart;
    private int selectionEnd;
    private int tempSelection;
    private RequestQueue requestQueue;
    private Toolbar mToolbar;
    private OKHttp httpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        setTitle("意见反馈");
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

        content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                number = totalNumber - s.length();
                remainingWords.setText(""+number);
                selectionStart = content.getSelectionStart();
                selectionEnd = content.getSelectionEnd();
                if(temp.length() > totalNumber){
                    s.delete(selectionStart-1, selectionEnd);
                    tempSelection = selectionEnd;
                    content.setText(s);
                    content.setSelection(tempSelection); //设置光标在最后
                }
            }
        });

        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

               /* StringRequest stringrequest = new StringRequest(Request.Method.POST, ConnectUtil.UserFeedback,
                        new Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // TODO Auto-generated method stub
                                try {
                                    JSONObject jb = new JSONObject(response);
                                    String result = jb.getString("reCode");
                                    String message = jb.getString("message");
                                    if (result.equals("SUCCESS")) {
                                        Toast.makeText(FeedbackActivity.this, "反馈成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(FeedbackActivity.this, message,Toast.LENGTH_SHORT).show();
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
                        map.put("username", sp.getisWorker().equals("0")? sp.getFarmerName():sp.getWorkerName());
                        map.put("content",content.getText().toString());
                        map.put("date", Time.getTime());
                        map.put("osType","Android");
                        map.put("version","2.0");
                        map.put("other",sp.getisWorker().equals("0")? "fromFarmer":"fromWorker");
                        return map;
                    }
                };
                requestQueue.add(stringrequest);*/


                Map<String, String> map = new HashMap<String, String>();
                map.put("username", sp.getisWorker().equals("0")? sp.getFarmerName():sp.getWorkerName());
                map.put("content",content.getText().toString());
                map.put("date", Time.getTime());
                map.put("osType","Android");
                map.put("version","2.0");
                map.put("other",sp.getisWorker().equals("0")? "fromFarmer":"fromWorker");
                httpUrl.post(Url.UserFeedback,map,new BaseCallback<String>(){
                    @Override
                    public void onRequestBefore() {

                    }

                    @Override
                    public void onFailure(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onSuccess(okhttp3.Response response, String resultResponse) {
                        Log.i("kmj","response:"+resultResponse);
                        try {
                            JSONObject jb = new JSONObject(resultResponse);
                            String result = jb.getString("reCode");
                            String message = jb.getString("message");
                            if (result.equals("SUCCESS")) {
                                Toast.makeText(FeedbackActivity.this, "反馈成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(FeedbackActivity.this, message,Toast.LENGTH_SHORT).show();
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
        });

    }

    private void initData() {
        // TODO Auto-generated method stub
        httpUrl = OKHttp.getInstance();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(FeedbackActivity.this, Constants.SAVE_USER);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void initViews() {
        // TODO Auto-generated method stub
        content = (EditText)findViewById(R.id.content);
        remainingWords =(TextView)findViewById(R.id.num);
        submit =(Button)findViewById(R.id.submit);
        mToolbar = (Toolbar)findViewById(R.id.feedback_toolbar);
    }

}
