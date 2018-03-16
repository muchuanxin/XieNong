/*package com.xidian.xienong.agriculture.me;

import android.support.v7.app.AppCompatActivity;

*//**
 * Created by koumiaojuan on 2017/6/9.
 *//*

public class EvaluateActivity extends AppCompatActivity{

}*/
package com.xidian.xienong.agriculture.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.CommentBean;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateActivity extends AppCompatActivity {
    private RatingBar workStar;
    private RatingBar serviceStar;
    private EditText content;
    private Button postComment;
    private NetWorkUtil netWorkUtil;
    private SharePreferenceUtil sp;
    private Intent previousIntent;
    private Float evaluateValue1;
    private Float evaluateValue2;
    private ImageView machine_image;
    private Button comment1,comment2,comment3,comment4,comment5;
    private String s="";
    private TextView worker_name,cropNum,total_money;
    private OrderBean order;
    private Context context;
    private RequestQueue requestQueue;
    private List<CommentBean> commentContents = new ArrayList<CommentBean>();
    private List<Button> buttons = new ArrayList<Button>();
    private String commentContentId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setTitle("评价");
        //  showEvaluateViews();
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        workStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub
                evaluateValue1 = rating;
            }
        });
        serviceStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub
                evaluateValue2 = rating;
            }
        });
        postComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                submit();
            }
        });
        comment1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetStates();
                setStates(v);
                commentContentId = commentContents.get(0).getComment_id();
            }
        });
        comment2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetStates();
                setStates(v);
                commentContentId = commentContents.get(1).getComment_id();
            }
        });
        comment3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetStates();
                setStates(v);
                commentContentId = commentContents.get(2).getComment_id();
            }
        });
        comment4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetStates();
                setStates(v);
                commentContentId = commentContents.get(3).getComment_id();
            }
        });
        comment5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetStates();
                setStates(v);
                commentContentId = commentContents.get(4).getComment_id();
            }
        });
    }

    @SuppressLint("NewApi") protected void resetStates() {
        // TODO Auto-generated method stub
        for(int i= 0; i < buttons.size();i++){
            buttons.get(i).setBackground(context.getResources().getDrawable(R.drawable.announce_state_corner2));
            buttons.get(i).setTextColor(context.getResources().getColor(R.color.orange));
            buttons.get(i).setPadding(10, 0, 10, 0);
        }
    }

    @SuppressLint("NewApi") protected void setStates(View v) {
        // TODO Auto-generated method stub
        ((Button)v).setBackground(context.getResources().getDrawable(R.drawable.announce_state_corner1));
        ((Button)v).setTextColor(context.getResources().getColor(R.color.white));
        ((Button)v).setPadding(10, 0, 10, 0);
    }

    protected void submit() {
        // TODO Auto-generated method stub
        if(workStar.getRating()==0.0){
            ToastCustom.makeToast(getApplicationContext(), "请对作业满意度进行评价");
            return ;
        }else if(serviceStar.getRating()==0.0){
            ToastCustom.makeToast(getApplicationContext(), "请对服务满意度进行评价");
            return ;
        }else if(content.getText().toString().equals("")){
            ToastCustom.makeToast(getApplicationContext(), "评价内容不能为空");
            return ;
        }else if(commentContentId.equals("")){
            ToastCustom.makeToast(getApplicationContext(), "请选择您对该农机主的整体评价");
            return ;
        }else{
            StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.FarmerEvaluateWorker,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // TODO Auto-generated method stub
                            try {
                                JSONObject jb = new JSONObject(response);
                                String result = jb.getString("reCode");
                                String message = jb.getString("message");
                                if (result.equals("SUCCESS")) {
                                    Toast.makeText(EvaluateActivity.this, "评价成功",Toast.LENGTH_SHORT).show();
                                    setResult(500);
                                    finish();
                                }else{
                                    Toast.makeText(EvaluateActivity.this, message,Toast.LENGTH_SHORT).show();
                                }
                                //  dismissRequestDialog();

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
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
                    map.put("order_id", order.getOrder_id());
                    map.put("farmer_id", order.getFarmer_id());
                    map.put("worker_id", order.getWorker_id());
                    map.put("work_satisfaction_degree", workStar.getRating()+"");
                    map.put("service_satisfaction_degree", serviceStar.getRating()+"");
                    map.put("comment_content_id", commentContentId);
                    map.put("date",getDate());
                    map.put("input_content", content.getText().toString());
                    return map;
                }
            };
            requestQueue.add(stringrequest);
        }

    }

    protected String getDate() {
        // TODO Auto-generated method stub
        String date;
        Calendar cal=Calendar.getInstance();
        date=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(cal.DATE);
        Log.i("kmj","date is " + date);
        return date;
    }

    private void initData() {
        // TODO Auto-generated method stub
        netWorkUtil = new NetWorkUtil(EvaluateActivity.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        context = getApplicationContext();
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        previousIntent = getIntent();
        order = (OrderBean) previousIntent.getSerializableExtra("order");
        worker_name.setText(order.getWorker_name());
        cropNum.setText(order.getCropland_number()+"");
        total_money.setText("¥"+ order.getPrice()*order.getCropland_number()+"");
        if(order.getMachineImages().size() > 0){
            Glide.with(context).load(order.getMachineImages().get(0).getUrl()).centerCrop().placeholder(R.drawable.empty_picture).into(machine_image);
        }
        getCommentContent();
    }

    private void getCommentContent() {
        // TODO Auto-generated method stub
        StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.GetCommentContent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        try {
                            JSONObject jb = new JSONObject(response);
                            String result = jb.getString("reCode");
                            if (result.equals("SUCCESS")) {
                                commentContents.clear();
                                JSONArray list = jb.getJSONArray("commentContentList");
                                for(int i=0; i < list.length(); i++){
                                    JSONObject object = list.getJSONObject(i);
                                    CommentBean content = new CommentBean();
                                    content.setComment_id(object.getString("comment_id"));
                                    content.setComment_content(object.getString("comment_content"));
                                    content.setServeValue(object.getString("value"));
                                    commentContents.add(content);
                                }
                                setCommentContent();

                            }else{
                                Toast.makeText(EvaluateActivity.this,"异常情况，请重试",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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
                return map;
            }
        };
        requestQueue.add(stringrequest);
    }

    protected void setCommentContent() {
        // TODO Auto-generated method stub
        for(int i=0; i< buttons.size(); i++){
            buttons.get(i).setText(commentContents.get(i).getComment_content());
        }

    }

    private void initViews() {
        // TODO Auto-generated method stub

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        machine_image = (ImageView)findViewById(R.id.detail_machine_image_one);
        worker_name = (TextView)findViewById(R.id.detail_worker);
        cropNum = (TextView)findViewById(R.id.detail_cropNum);
        total_money = (TextView)findViewById(R.id.detail_total_money);
        workStar= (RatingBar)findViewById(R.id.my_evaluate_rabar1);
        serviceStar= (RatingBar)findViewById(R.id.my_evaluate_rabar2);
        content =(EditText)findViewById(R.id.et_evaluate);
        postComment = (Button)findViewById(R.id.post_comment);
        comment1 = (Button)findViewById(R.id.btn_comment_1);
        comment2 = (Button)findViewById(R.id.btn_comment_2);
        comment3 = (Button)findViewById(R.id.btn_comment_3);
        comment4 = (Button)findViewById(R.id.btn_comment_4);
        comment5 = (Button)findViewById(R.id.btn_comment_5);
        buttons.add(comment1);
        buttons.add(comment2);
        buttons.add(comment3);
        buttons.add(comment4);
        buttons.add(comment5);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

