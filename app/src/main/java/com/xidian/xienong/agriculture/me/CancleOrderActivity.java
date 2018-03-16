package com.xidian.xienong.agriculture.me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.CancleOrderAdapter;
import com.xidian.xienong.model.CancleReason;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by koumiaojuan on 2017/6/9.
 */

public class CancleOrderActivity extends AppCompatActivity{

    private Toolbar cancleOrderToolbar;
    private Button cancleOrder;
    private OrderBean order;
    private String cancleReasonId="",reason="";
    private ListView cancleOrderListView;
    private CancleOrderAdapter adapter;
    private List<CancleReason> cancleReasons = new ArrayList<CancleReason>();
    private SharePreferenceUtil sp;
    private boolean isSelected = false;
    private String type="";
    private OKHttp httpUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancle_order_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        cancleOrderToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancleOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                isSelected = true;
                adapter.setSelected(isSelected, position);
                cancleReasonId = cancleReasons.get(adapter.getSelected()).getReason_id();
                reason = cancleReasons.get(adapter.getSelected()).getReason();
            }
        });


        cancleOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(cancleReasonId.equals("")){
                    Toast.makeText(CancleOrderActivity.this,"请选择取消原因",Toast.LENGTH_SHORT).show();
                }else{
                    if(type.equals("choose_cancle_reason")){
                        applyCancleOrderReason();
                    }else{
                        showCancleOrderDialog();
                    }

                }

            }
        });
    }

    private void showCancleOrderDialog() {
        new SweetAlertDialog(CancleOrderActivity.this, SweetAlertDialog.TIP_TYPE)
                .setTitleText("温馨提示")
                .setContentText("是否确定取消订单?")
                .setCancelText("不，谢谢")
                .setConfirmText("好，取消")
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
//                        if(sp.getisWorker().equals("0")){
//                            requestCancleOrder();
//                        }else{
//                            CancleOrder();
//                        }
                        if(type.equals("farmerCancleOrder")){
                            requestCancleOrder();
                        }else{
                            CancleOrder();
                        }

                    }
                })
                .show();
    }

    private void CancleOrder() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", order.getOrder_id());
        map.put("reservation_time", order.getReservation_time());
        map.put("cancle_reason_id", cancleReasonId);
        httpUrl.post(Url.CancleHaveGrabbedOrder,map,new BaseCallback<String>(){
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
                        Toast.makeText(CancleOrderActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
                        if(type.equals("dispatch_resource_activity")){
                            setResult(201); //dispatch_resource_activity
                        }else{
                            setResult(203); //dispatch_info_activity
                        }
                        finish();
                    }else{
                        Toast.makeText(CancleOrderActivity.this,"取消失败，请重试",Toast.LENGTH_SHORT).show();
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

    private void requestCancleOrder() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", order.getOrder_id());
        map.put("cancle_reason_id", cancleReasonId);
        httpUrl.post(Url.CancleAnnouncement,map,new BaseCallback<String>(){
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
                        Toast.makeText(CancleOrderActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
                        setResult(300);
                        finish();
                    }else{
                        Toast.makeText(CancleOrderActivity.this,"取消失败，请重试",Toast.LENGTH_SHORT).show();
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

    private void applyCancleOrderReason() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", order.getOrder_id());
        map.put("apply_cancle_reason_id", cancleReasonId);
        httpUrl.post(Url.ApplyCancleOrder,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("kmj","ApplyCancleOrder : " + Url.ApplyCancleOrder);
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
//								Toast.makeText(CancleOrderActivity.this,message,Toast.LENGTH_SHORT).show();
                        setResult(208);
                        finish();
                    }else{
                        Toast.makeText(CancleOrderActivity.this,message,Toast.LENGTH_SHORT).show();
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

    private void initData() {
        setSupportActionBar(cancleOrderToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        httpUrl = OKHttp.getInstance();
        order = (OrderBean) getIntent().getSerializableExtra("orderBean");
        type = getIntent().getType();
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        getCancleOrderReason();
    }

    private void getCancleOrderReason() {
        // TODO Auto-generated method stub
        final Map<String, String> map = new HashMap<String, String>();
        map.put("cancle_source", type.equals("farmerCancleOrder")?"0":"1");
        httpUrl.post(Url.GetAllCancleReason,map,new BaseCallback<String>(){
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
                    if (result.equals("SUCCESS")) {
                        cancleReasons.clear();
                        JSONArray list = jb.getJSONArray("reasonList");
                        for(int i=0; i < list.length(); i++){
                            JSONObject object = list.getJSONObject(i);
                            CancleReason cr = new CancleReason();
                            cr.setReason_id(object.getString("reason_id"));
                            cr.setReason(object.getString("reason"));
                            cancleReasons.add(cr);
                        }
                        adapter.setList(cancleReasons);
                        adapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(CancleOrderActivity.this,"异常情况，请重试",Toast.LENGTH_SHORT).show();
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

    private void initViews() {
        cancleOrderToolbar = (Toolbar)findViewById(R.id.cancle_order_toolbar);
        // TODO Auto-generated method stub
        cancleOrderListView = (ListView)findViewById(R.id.cancle_order_listView);
        cancleOrder = (Button)findViewById(R.id.btn_cancle_order);
        adapter = new CancleOrderAdapter(getApplicationContext(),cancleReasons);
        cancleOrderListView.setAdapter(adapter);
    }
}
