package com.xidian.xienong.agriculture.me;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/9.
 */

public class AnnounceDetailActivity extends AppCompatActivity {

    private Toolbar detailToolbar;
    private RelativeLayout description;
    private TextView orderCode;
    private TextView orderState;
    private RelativeLayout machine_name;
    private RelativeLayout machine_tele;
    private RelativeLayout operate_driver;
    private TextView tv_machine_name;
    private TextView tv_machine_tele;
    private TextView tv_operate_driver;
    private TextView tv_detail_address;
    private TextView tv_machine_type;
    private TextView tv_reservation;
    private TextView tv_crop_type;
    private TextView tv_crop_num;
    private OrderBean order;
    private Button btnCancle, btnEvaluate;
    private String drivers = "";
    private View line1, line2;
    private OKHttp httpUrl;
    private RelativeLayout picture;
    private ImageView iv_pic_image;
    private TextView numberOfPicture;
    private LinearLayout showCancleInfo;
    private TextView showCancleTime, showCancleReason;
    private TextView concel_message;
    private String advice_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announce_detail_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (btnEvaluate.getText().equals("您已评价")) {
                    Intent intent = new Intent(Constants.HAS_EVALUATED_BY_FARMER);
                    sendBroadcast(intent);
                }
                if (btnCancle.getText().equals("您已取消该订单") || btnEvaluate.getText().equals("您已拒绝取消该订单")) {
                    Intent intent = new Intent(Constants.HAS_AGREE_OR_REFUSE_BY_FARMER);
                    sendBroadcast(intent);
                }
                finish();
            }
        });

        iv_pic_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(AnnounceDetailActivity.this, AllMachineImageActivity.class);
                intent.putExtra("image_url", (Serializable) order.getMachineImages());
                startActivity(intent);
            }
        });
        machine_tele.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(AnnounceDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                        .setTitleText("确认手机号码")
                        .setContentText("我们将拨打农机主" + order.getWorker_name() + "的手机号：" + order.getWorker_telephone())
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
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getWorker_telephone()));
                                if (ActivityCompat.checkSelfPermission(AnnounceDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        btnCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(order.getOrderState().equals("待处理")){
                    turnToCancleOrder();
                }else{
                    if(btnCancle.getText().equals("同意取消该订单")){

                        new SweetAlertDialog(AnnounceDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                                .setTitleText("取消订单")
                                .setContentText("您确定同意取消订单？")
                                .setCancelText("不，谢谢")
                                .setConfirmText("好，确定")
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
                                        agreeCancelOrder();
                                    }
                                })
                                .show();

                    }else{
                        if(Time.compare_with_now_date(order.getReservation_time())!= -1 ){
                            new SweetAlertDialog(AnnounceDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("温馨提示")
                                    .setContentText("对不起，您已超过取消订单时间!")
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
                            turnToCancleOrder();
                        }

                    }
                }
            }
        });

        btnEvaluate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(btnEvaluate.getText().equals("拒绝取消该订单")){

                    new SweetAlertDialog(AnnounceDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                            .setTitleText("取消订单")
                            .setContentText("您确定拒绝取消订单？")
                            .setCancelText("不，谢谢")
                            .setConfirmText("好，确定")
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
                                    refuseCancelOrder();
                                }
                            })
                            .show();

                }else{
                    new SweetAlertDialog(AnnounceDetailActivity.this, SweetAlertDialog.TIP_TYPE)
                            .setTitleText("评价农机主")
                            .setContentText("您是否要对农机主进行评价？")
                            .setCancelText("不，谢谢")
                            .setConfirmText("好，评价")
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
                                    Intent intent = new Intent(AnnounceDetailActivity.this,EvaluateActivity.class);
                                    intent.putExtra("order", order);
                                    startActivityForResult(intent, 301);
                                }
                            })
                            .show();
                }

            }
        });

    }

    private void agreeCancelOrder() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", order.getOrder_id());
        map.put("reservation_time", order.getReservation_time());
        map.put("cancel_reason_id", order.getApplyCancleReasonId());
        httpUrl.post(Url.AgreeCancleOrder,map,new BaseCallback<String>(){
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
                        btnCancle.setBackgroundResource(R.drawable.gray_corner);
                        btnEvaluate.setBackgroundResource(R.drawable.gray_corner);
                        btnCancle.setEnabled(false);
                        btnEvaluate.setEnabled(false);
                        btnCancle.setText("您已取消该订单");
                    }else{
                        Toast.makeText(AnnounceDetailActivity.this, message,Toast.LENGTH_SHORT).show();
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

    private void refuseCancelOrder() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", order.getOrder_id());
        httpUrl.post(Url.RefuseCancleOrder,map,new BaseCallback<String>(){
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
                        btnCancle.setBackgroundResource(R.drawable.gray_corner);
                        btnEvaluate.setBackgroundResource(R.drawable.gray_corner);
                        btnCancle.setEnabled(false);
                        btnEvaluate.setEnabled(false);
                        btnEvaluate.setText("您已拒绝取消该订单");
                    }else{
                        Toast.makeText(AnnounceDetailActivity.this, message,Toast.LENGTH_SHORT).show();
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

    protected void turnToCancleOrder() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(AnnounceDetailActivity.this,CancleOrderActivity.class);
        intent.putExtra("orderBean", order);
        intent.setType("farmerCancleOrder");
        startActivityForResult(intent, 301);
    }




    private void initData() {
        setSupportActionBar(detailToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order = (OrderBean) getIntent().getSerializableExtra("announce");
        //advice_state=order.getAdvice_state();
        httpUrl = OKHttp.getInstance();
        advice_state=order.getAdviceState();
        for(int i=0; i < order.getDrivers().size(); i++){
            if(i != order.getDrivers().size()-1){
                drivers = drivers + order.getDrivers().get(i).getDriver_name()+"、";
            }else{
                drivers = drivers + order.getDrivers().get(i).getDriver_name();
            }
        }

        orderCode.setText(order.getOrderCode());
        orderState.setText(order.getOrderState());
        if(order.getOrderState().equals("待处理")){
            iv_pic_image.setVisibility(View.GONE);
            picture.setVisibility(View.GONE);
            machine_name.setVisibility(View.GONE);
            machine_tele.setVisibility(View.GONE);
            operate_driver.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            showCancleInfo.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);
            btnEvaluate.setVisibility(View.GONE);
        }else if(order.getOrderState().equals("已处理")|| order.getOrderState().equals("作业中") ||
                order.getOrderState().equals("已完成")){
            if(order.getMachineImages().size()==0){
                Log.i("kmj","--order.getMachineImages().size()----" + order.getMachineImages().size());
                iv_pic_image.setVisibility(View.GONE);
                picture.setVisibility(View.GONE);
            }else{
                iv_pic_image.setVisibility(View.VISIBLE);
                picture.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(order.getMachineImages().get(0).getUrl()).centerCrop().placeholder(R.drawable.author).into(iv_pic_image);
                numberOfPicture.setText("共有"+order.getMachineImages().size()+"张图片");
            }
            machine_name.setVisibility(View.VISIBLE);
            machine_tele.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            tv_machine_name.setText(order.getWorker_name());
            tv_machine_tele.setText(order.getWorker_telephone());
            if(drivers.equals("")){
                operate_driver.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
            }else{
                operate_driver.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                tv_operate_driver.setText(drivers);
            }

            if(order.getOrderState().equals("作业中") || order.getOrderState().equals("已完成")){
                description.setVisibility(View.GONE);
                btnCancle.setVisibility(View.GONE);
                if(order.getOrderState().equals("作业中")){
                    btnEvaluate.setVisibility(View.GONE);
                }else{
                    if(order.isEvaluate()==true){
                        btnEvaluate.setVisibility(View.GONE);
                    }else{
                        btnEvaluate.setVisibility(View.VISIBLE);
                    }

                }
            }else if(order.getOrderState().equals("已处理")){
                description.setVisibility(View.GONE);
                if(advice_state.equals("0")){
                    concel_message.setVisibility(View.GONE);
                    btnCancle.setVisibility(View.GONE);
                    btnEvaluate.setVisibility(View.GONE);
                }else if(advice_state.equals("1")){
                    concel_message.setText(order.getApplyCancleReason());
                    concel_message.setVisibility(View.VISIBLE);
                    btnCancle.setVisibility(View.VISIBLE);
                    btnCancle.setText("同意取消该订单");
                    btnEvaluate.setVisibility(View.VISIBLE);
                    btnEvaluate.setText("拒绝取消该订单");
                }else if(advice_state.equals("2")){
                    concel_message.setText(order.getApplyCancleReason());
                    concel_message.setVisibility(View.VISIBLE);
                    btnCancle.setVisibility(View.VISIBLE);
                    btnCancle.setText("您已同意取消该订单");
                    btnCancle.setBackgroundResource(R.drawable.gray_corner);
                    btnCancle.setEnabled(false);
                    btnEvaluate.setVisibility(View.GONE);
                }else{//-1
                    concel_message.setText(order.getApplyCancleReason());
                    concel_message.setVisibility(View.VISIBLE);
                    btnCancle.setVisibility(View.VISIBLE);
                    btnCancle.setText("您已拒绝取消该订单");
                    btnCancle.setBackgroundResource(R.drawable.gray_corner);
                    btnCancle.setEnabled(false);
                    btnEvaluate.setVisibility(View.GONE);
                }
            }else{
                description.setVisibility(View.VISIBLE);
                btnCancle.setVisibility(View.VISIBLE);
            }

            showCancleInfo.setVisibility(View.GONE);
        }else{
            if(order.getOrderState().equals("已取消")){
                btnCancle.setVisibility(View.GONE);
                btnEvaluate.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                showCancleInfo.setVisibility(View.VISIBLE);
                showCancleTime.setText(order.getCancleTime()+",订单已取消");
                showCancleReason.setText(order.getCancleReason());
                operate_driver.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
            }
        }

        tv_detail_address.setText(order.getCrop_address());
        tv_machine_type.setText(order.getMachine_category());
        tv_reservation.setText(order.getReservation_time());
        tv_crop_type.setText(order.getCropland_type());
        tv_crop_num.setText(order.getCropland_number()+"亩");
    }

    private void initViews() {
        detailToolbar = (Toolbar)findViewById(R.id.announce_detail_toolbar);
        description = (RelativeLayout)findViewById(R.id.description);
        orderCode = (TextView)findViewById(R.id.order_code);
        orderState = (TextView)findViewById(R.id.order_state);
        machine_name = (RelativeLayout)findViewById(R.id.rl_machine_name);
        machine_tele = (RelativeLayout)findViewById(R.id.rl_machine_telephone);
        operate_driver = (RelativeLayout)findViewById(R.id.rl_operate_driver);
        tv_machine_name = (TextView)findViewById(R.id.tv_machine_name);
        tv_machine_tele = (TextView)findViewById(R.id.tv_machine_telephone);
        tv_operate_driver = (TextView)findViewById(R.id.tv_operate_driver);
        tv_detail_address = (TextView)findViewById(R.id.tv_detail_address_1);
        tv_machine_type = (TextView)findViewById(R.id.tv_machine_type_1);
        tv_reservation = (TextView)findViewById(R.id.tv_reservation_1);
        tv_crop_type = (TextView)findViewById(R.id.tv_crop_type_1);
        tv_crop_num = (TextView)findViewById(R.id.tv_crop_num_1);
        btnCancle = (Button)findViewById(R.id.btn_cancle);
        btnEvaluate = (Button)findViewById(R.id.btn_evaluate);
        line1 = (View)findViewById(R.id.line_1);
        line2 = (View)findViewById(R.id.line_2);
        picture = (RelativeLayout)findViewById(R.id.rl_picture);
        iv_pic_image = (ImageView)findViewById(R.id.iv_picture);
        numberOfPicture = (TextView)findViewById(R.id.numofpic);
        concel_message=(TextView) findViewById(R.id.show_concel_message);
        showCancleInfo = (LinearLayout)findViewById(R.id.show_cancle_info);
        showCancleTime = (TextView)findViewById(R.id.show_cancle_time);
        showCancleReason = (TextView)findViewById(R.id.show_cancle_reason);
    }

    public void onBackPressed() {

        super.onBackPressed();
        if(btnEvaluate.getText().equals("您已评价")){
            Intent intent = new Intent(Constants.HAS_EVALUATED_BY_FARMER);
            sendBroadcast(intent);
        }

        if(btnCancle.getText().toString().equals("您已取消该订单")|| btnEvaluate.getText().equals("您已拒绝取消该订单") ){
            Intent intent = new Intent(Constants.HAS_AGREE_OR_REFUSE_BY_FARMER);
            sendBroadcast(intent);
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 301 ) {
            if (resultCode == 300 ) {
                setResult(300);
                Intent intent = new Intent(Constants.HAS_CANCLED_BY_FARMER);
                sendBroadcast(intent);
                finish();
            }
            if (resultCode == 500 ) {
                btnEvaluate.setText("您已评价");
                btnEvaluate.setClickable(false);
                btnEvaluate.setBackgroundResource(R.drawable.gray_corner);
            }
        }

    }

}
