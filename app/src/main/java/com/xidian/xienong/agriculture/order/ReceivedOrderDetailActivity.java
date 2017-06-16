package com.xidian.xienong.agriculture.order;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.model.Resource;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.AMapUtil;
import com.xidian.xienong.util.CircleImageView;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/13.
 */

public class ReceivedOrderDetailActivity extends AppCompatActivity implements AMap.OnMarkerClickListener {

    private MapView mapView;
    private AMap aMap;
    private CircleImageView photo;
    private TextView name, state;
    private RelativeLayout rl_tele;
    private TextView telephone;
    private TextView reservationTime;
    private RelativeLayout rlDetail;
    private Button start;
    private OKHttp httpUrl;
    private OrderBean orderBean;
    private double longtitude;
    private double lantitude;
    private LatLonPoint crop_point;
    private LatLng cropLatLng;
    private TextView description_text;
    private String machine_id_list = "", driver_id_list = "";
    public static boolean isCancledOrder = false;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_order_detail_activity);
        mapView = (MapView) findViewById(R.id.DetailMapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
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
                if (orderBean.getOrderState().equals("已接单")) {
                    Intent intent = new Intent(Constants.DISPATCH_ORDER_SUCCESS_ACTION);
                    sendBroadcast(intent);
                } else if (orderBean.getOrderState().equals("作业中")) {
                    Intent intent = new Intent(Constants.IS_OPERATING_STATE_ORDER_ACTION);
                    sendBroadcast(intent);
                } else {
                    Intent intent = new Intent(Constants.CANCLE_ORDER_ACTION);
                    sendBroadcast(intent);
                }
                finish();
            }
        });
        rl_tele.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(ReceivedOrderDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                        .setTitleText("确认手机号码")
                        .setContentText("我们将拨打农户" + orderBean.getFarmer_name() + "的手机号：" + orderBean.getTelephone())
                        .setCancelText("不，谢谢")
                        .setConfirmText("好，拨打")
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
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderBean.getTelephone()));
                                if (ActivityCompat.checkSelfPermission(ReceivedOrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        rlDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ReceivedOrderDetailActivity.this,DispatchInformationActivity.class);
                intent.putExtra("orderBean", orderBean);
                startActivityForResult(intent, 17);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(start.getText().toString().equals("开始")){
                    if(Time.compare_with_now_date(orderBean.getReservation_time())== -1 ){
                        new SweetAlertDialog(ReceivedOrderDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("温馨提示")
                                .setContentText("对不起，还未到达作业时间!")
                                .setConfirmText("是的，确定!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        // reuse previous dialog instance
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }else{
                        new SweetAlertDialog(ReceivedOrderDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                                .setTitleText("开始作业")
                                .setContentText("您确认点击开始作业吗")
                                .setCancelText("不，放弃")
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
                                        getStartOperating();
                                    }
                                })
                                .show();

                    }

                }else{
                    new SweetAlertDialog(ReceivedOrderDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                            .setTitleText("结束作业")
                            .setContentText("您确认点击结束作业吗")
                            .setCancelText("不，放弃")
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
                                    endOperating();
                                }
                            })
                            .show();
                }

            }
        });
    }

    protected void endOperating() {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", orderBean.getOrder_id());
        map.put("reservation_time", orderBean.getReservation_time()+"_"+orderBean.getOrder_id());
        map.put("machine_id_list", machine_id_list);
        map.put("driver_id_list", driver_id_list);
        httpUrl.post(Url.EndOperated,map,new BaseCallback<String>(){
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
                        start.setBackgroundResource(R.drawable.gray_corner);
                        start.setClickable(false);
                        orderBean.setOrderState("已完成");
                        Toast.makeText(ReceivedOrderDetailActivity.this,"订单已完成",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Constants.OPERATED_ORDER_ACTION);
                        sendBroadcast(intent);
                        finish();
                    }else{
                        Toast.makeText(ReceivedOrderDetailActivity.this,"异常情况，请重试",Toast.LENGTH_SHORT).show();
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

    protected void getStartOperating() {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", orderBean.getOrder_id());
        httpUrl.post(Url.StartOperating,map,new BaseCallback<String>(){
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
                        SweetAlertDialog sd = new SweetAlertDialog(ReceivedOrderDetailActivity.this);
                        sd.setCancelable(true);
                        sd.setCanceledOnTouchOutside(true);
                        sd.setTitleText("订单处于作业中状态");
                        sd.show();
                        start.setText("结束");
                        description_text.setText("请在作业完成后点击");
                        state.setText("作业中");
                        orderBean.setOrderState("作业中");
                    }else{
                        Toast.makeText(ReceivedOrderDetailActivity.this,"异常情况，请重试",Toast.LENGTH_SHORT).show();
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
        // TODO Auto-generated method stub
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        registerBroadcastReceiver();
        httpUrl = OKHttp.getInstance();
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderBean");
        longtitude = orderBean.getCrop_longtitude();
        lantitude = orderBean.getCrop_lantitude();
        getSupportActionBar().setTitle(orderBean.getMachine_category());
        name.setText(orderBean.getFarmer_name());
        state.setText(orderBean.getOrderState());
        telephone.setText("电话："+ orderBean.getTelephone());
        reservationTime.setText("预约时间："+ orderBean.getReservation_time());
        crop_point = new LatLonPoint(lantitude,longtitude);
        aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(crop_point)).icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        cropLatLng = new LatLng(lantitude, longtitude);
        builder.include(cropLatLng);
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(cropLatLng, 0, 0, 0)),null);
        if(!orderBean.getHeadphoto().equals("")){
            Glide.with(getApplicationContext()).load(orderBean.getHeadphoto()).centerCrop().placeholder(R.drawable.author).into(photo);
        }
        for(Resource rs : orderBean.getResources()){
            if(orderBean.getResources().indexOf(rs) == orderBean.getResources().size()-1){
                machine_id_list = machine_id_list + rs.getMachine().getMachine_id();
                driver_id_list = driver_id_list + rs.getDriver().getDriver_id();
            }else{
                machine_id_list = machine_id_list + rs.getMachine().getMachine_id()+"-";
                driver_id_list = driver_id_list + rs.getDriver().getDriver_id()+"-";
            }
        }
        start.setClickable(true);
        if(orderBean.getOrderState().equals("已接单")){
            start.setText("开始");
            description_text.setText("请在作业当天点击开始");
        }else{
            start.setText("结束");
            description_text.setText("请在作业完成后点击");
        }
    }

    private void registerBroadcastReceiver() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constants.DISPATCH_INFO_CANCLE_ORDER_ACTION);
        filter.addAction(Constants.CANCLE_REQUEST_ACTION);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals(Constants.DISPATCH_INFO_CANCLE_ORDER_ACTION)){
                orderBean.setOrderState("已取消");
                state.setText("已取消");
                start.setBackgroundResource(R.drawable.gray_corner);
                start.setClickable(false);

            }else{
                if(orderBean.getAdviceState().equals("0")){
                    orderBean.setAdviceState("1");
                }

            }
        }
    };



    private void setUpMap() {
        // TODO Auto-generated method stub
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        aMap.setOnMarkerClickListener(this);
    }


    private void initViews() {
        // TODO Auto-generated method stub
        photo = (CircleImageView)findViewById(R.id.detail_headphoto);
        name = (TextView)findViewById(R.id.detail_name);
        state = (TextView)findViewById(R.id.detail_state);
        rl_tele = (RelativeLayout)findViewById(R.id.rl_detail_tele);
        telephone = (TextView)findViewById(R.id.detail_tele);
        reservationTime = (TextView)findViewById(R.id.detail_reservation_time);
        rlDetail = (RelativeLayout)findViewById(R.id.received_order_detail);
        description_text = (TextView)findViewById(R.id.description_text);
        start = (Button)findViewById(R.id.btn_start);
        mToolbar = (Toolbar)findViewById(R.id.received_order_toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
        try {
            // 调起高德地图导航
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
        return false;
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        aMap.moveCamera(update);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(orderBean.getOrderState().equals("已接单")){
            Intent intent = new Intent(Constants.DISPATCH_ORDER_SUCCESS_ACTION);
            sendBroadcast(intent);
        }else if(orderBean.getOrderState().equals("作业中")){
            Intent intent = new Intent(Constants.IS_OPERATING_STATE_ORDER_ACTION);
            sendBroadcast(intent);
        }else{
            Intent intent = new Intent(Constants.CANCLE_ORDER_ACTION);
            sendBroadcast(intent);
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 17 ) {
            if(resultCode == 203){
                orderBean.setOrderState("已取消");
                state.setText("已取消");
                start.setBackgroundResource(R.drawable.gray_corner);
                start.setClickable(false);
            }
        }
    }



}
