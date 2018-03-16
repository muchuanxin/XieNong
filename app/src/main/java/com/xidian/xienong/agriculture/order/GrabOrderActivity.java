package com.xidian.xienong.agriculture.order;

import android.Manifest;
import android.content.Intent;
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
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.AMapUtil;
import com.xidian.xienong.util.CircleImageView;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/13.
 */

public class GrabOrderActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MapView mapView;
    private AMap aMap;
    private double longtitude;
    private double lantitude;
    private LatLonPoint crop_point;
    private LatLng crop_latlon;
    private SharePreferenceUtil sp;
    private LatLngBounds.Builder builder;
    private CircleImageView photo;
    private TextView name;
    private RelativeLayout rl_tele;
    private TextView telephone;
    private TextView reservationTime;
    private TextView croplandType;
    private TextView croplandNumber;
    private Button grabOrder;
    private OrderBean orderBean;
    private OKHttp httpUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grab_order_activity);
        mapView = (MapView) findViewById(R.id.grabOrderMapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
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
        rl_tele.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(GrabOrderActivity.this, SweetAlertDialog.TIP_TYPE)
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
                                if (ActivityCompat.checkSelfPermission(GrabOrderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

        grabOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(GrabOrderActivity.this, SweetAlertDialog.TIP_TYPE)
                        .setTitleText("确认抢单")
                        .setContentText("您确定要抢该笔订单吗")
                        .setCancelText("不，放弃")
                        .setConfirmText("好，抢单")
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
                                grabOrder();
                            }
                        })
                        .show();

            }
        });
    }

    private void grabOrder() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("worker_id", sp.getUserId());
        map.put("order_id", orderBean.getOrder_id());
        httpUrl.post(Url.GrabOrder,map,new BaseCallback<String>(){
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
                    if(result.equals("SUCCESS")){
                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                        orderBean.setOrderState("已接单");
                        Intent intent = new Intent(GrabOrderActivity.this,DispatchResourceActivity.class);
                        intent.putExtra("orderBean", orderBean);
                        startActivity(intent);
                        finish();
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

    private void initDatas() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        httpUrl = OKHttp.getInstance();
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderBean");
        longtitude = getIntent().getDoubleExtra("longtitude", 0.0);
        lantitude = getIntent().getDoubleExtra("lantitude", 0.0);
        if(!orderBean.getFarmerHeadphoto().equals("")){
            Glide.with(getApplicationContext()).load(orderBean.getFarmerHeadphoto()).centerCrop().placeholder(R.drawable.author).into(photo);
        }
        getSupportActionBar().setTitle(orderBean.getMachine_category());
        name.setText(orderBean.getFarmer_name());
        telephone.setText("电话："+ orderBean.getTelephone());
        reservationTime.setText("预约时间："+ orderBean.getReservation_time());
        croplandType.setText("农田类型："+ orderBean.getCropland_type());
        croplandNumber.setText("预约亩数："+ orderBean.getCropland_number());

        crop_point = new LatLonPoint(lantitude,longtitude);
        aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(crop_point)).icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));

        crop_latlon = new LatLng(lantitude,longtitude);
        builder = new LatLngBounds.Builder();
        builder.include(crop_latlon);
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(crop_latlon, 0, 0, 0)),null);
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        aMap.moveCamera(update);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10));

    }

    private void setUpMap() {
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    }

    private void initViews() {
        mToolbar = (Toolbar)findViewById(R.id.grab_order_toolbar);
        photo = (CircleImageView)findViewById(R.id.grab_order_headphoto);
        name = (TextView)findViewById(R.id.grab_order_name);
        rl_tele = (RelativeLayout)findViewById(R.id.rl_grab_tele);
        telephone = (TextView)findViewById(R.id.grab_order_tele);
        reservationTime = (TextView)findViewById(R.id.grab_order_reservation_time);
        croplandType = (TextView)findViewById(R.id.grab_order_cropland_type);
        croplandNumber = (TextView)findViewById(R.id.grab_order_cropNumber);
        grabOrder = (Button)findViewById(R.id.btn_garb_order_1);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(Constants.GRAB_ORDER_FAIL_ACTION);
        sendBroadcast(intent);
        finish();
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
    }


}
