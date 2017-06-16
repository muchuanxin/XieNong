package com.xidian.xienong.agriculture.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.AddedResourceAdapter;
import com.xidian.xienong.agriculture.me.CancleOrderActivity;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.model.Resource;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/14.
 */

public class DispatchInformationActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private View headerView,footerView;
    private TextView croplandType;
    private TextView croplandNumber;
    private ListView listView;
    private AddedResourceAdapter adapter;
    private List<Resource> resources = new ArrayList<Resource>();
    private Button cancleOrder,cancleOrder2;
    private OrderBean orderBean;
    private OKHttp httpUrl;
    private String adviceState="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatched_info_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        // TODO Auto-generated method stub
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!cancleOrder2.getText().equals("取消订单")){
                    Intent intent = new Intent(Constants.DISPATCH_INFO_CANCLE_ORDER_ACTION);
                    sendBroadcast(intent);
                }
                if(!cancleOrder.getText().equals("取消订单")){
                    Intent intent = new Intent(Constants.CANCLE_REQUEST_ACTION);
                    sendBroadcast(intent);
                }
                finish();
            }
        });

        cancleOrder2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancleOrder();
            }
        });
        cancleOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(DispatchInformationActivity.this, SweetAlertDialog.TIP_TYPE)
                        .setTitleText("取消订单")
                        .setContentText("点击后将向对方发出取消订单请求？")
                        .setCancelText("不，谢谢")
                        .setConfirmText("好，点击")
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
                                Intent intent = new Intent(DispatchInformationActivity.this,CancleOrderActivity.class);
                                intent.putExtra("orderBean", orderBean);
                                intent.setType("choose_cancle_reason");
                                startActivityForResult(intent, 202);
                            }
                        })
                        .show();
            }
        });
    }

    private void sendCancleOrderRequest(String order_id) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", orderBean.getOrder_id());
        map.put("cancle_method", "1");
        httpUrl.post(Url.RequestCancleOrder,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("SUCCESS")) {
                        ToastCustom.makeToast(getApplicationContext(), "取消请求发送成功");
                        cancleOrder.setText("取消请求已发出");
                        cancleOrder.setClickable(false);
                        cancleOrder.setBackgroundResource(R.drawable.gray_corner);

                    }else{
                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
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



    private void cancleOrder() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(DispatchInformationActivity.this,CancleOrderActivity.class);
        intent.putExtra("orderBean", orderBean);
        intent.setType("dispatch_info_activity");
        startActivityForResult(intent, 202);
    }


    private void initData() {
        // TODO Auto-generated method stub
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        httpUrl = OKHttp.getInstance();
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderBean");
        adviceState = orderBean.getAdviceState();
        croplandType.setText(orderBean.getCropland_type());
        croplandNumber.setText(orderBean.getCropland_number()+"亩");
        resources = orderBean.getResources();
        adapter.setList(resources);
        adapter.notifyDataSetChanged();
        if(orderBean.getOrderState().equals("已接单")){
            if(adviceState.equals("0")){
                cancleOrder.setText("申请取消订单");
                cancleOrder.setClickable(true);
                cancleOrder.setBackgroundResource(R.drawable.verify_corner);
                cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder2.setVisibility(View.GONE);
            }else if(adviceState.equals("1")){
                cancleOrder.setText("取消请求已发出");
                cancleOrder.setClickable(false);
                cancleOrder.setBackgroundResource(R.drawable.gray_corner);
                cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder2.setVisibility(View.GONE);
            }else if(adviceState.equals("2")){
                cancleOrder.setText("对方已同意取消订单");
                cancleOrder.setClickable(false);
                cancleOrder.setBackgroundResource(R.drawable.gray_corner);
                cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder2.setVisibility(View.VISIBLE);
            }else{
                cancleOrder.setText("对方已拒绝取消订单");
                cancleOrder2.setText("强制取消订单");
                cancleOrder.setClickable(false);
                cancleOrder.setBackgroundResource(R.drawable.gray_corner);
                cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder2.setVisibility(View.VISIBLE);
            }
        }else{
            cancleOrder.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        // TODO Auto-generated method stub
        mToolbar = (Toolbar)findViewById(R.id.detail_info_toolbar);
        headerView = getLayoutInflater().inflate(R.layout.dispatched_info_header,null);
        croplandType = (TextView)headerView.findViewById(R.id.tv_dispatch_info_type);
        croplandNumber = (TextView)headerView.findViewById(R.id.tv_dispatch_info_num);
        listView = (ListView)findViewById(R.id.dispatched_info_listView);
        listView.addHeaderView(headerView);
        footerView =  getLayoutInflater().inflate(R.layout.dispatch_info_footer,null);
        cancleOrder = (Button)footerView.findViewById(R.id.btn_dispatch_info_cancle);
        cancleOrder2 = (Button)footerView.findViewById(R.id.btn_dispatch_info_cancle_2);
        listView.addFooterView(footerView);
        adapter = new AddedResourceAdapter(DispatchInformationActivity.this, resources);
        listView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(!cancleOrder2.getText().equals("取消订单")){
            Intent intent = new Intent(Constants.DISPATCH_INFO_CANCLE_ORDER_ACTION);
            sendBroadcast(intent);
        }
        if(!cancleOrder.getText().equals("取消订单")){
            Intent intent = new Intent(Constants.CANCLE_REQUEST_ACTION);
            sendBroadcast(intent);
        }
        finish();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202 ) {
            if(resultCode == 203){
                cancleOrder2.setText("订单已取消");
                cancleOrder2.setClickable(false);
                cancleOrder2.setBackgroundResource(R.drawable.gray_corner);
            }
            if(resultCode == 208){
                sendCancleOrderRequest(orderBean.getOrder_id());
            }
        }
    }



}
