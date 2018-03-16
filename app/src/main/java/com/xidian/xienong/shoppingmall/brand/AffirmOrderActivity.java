package com.xidian.xienong.shoppingmall.brand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.AddressAdapter;
import com.xidian.xienong.adapter.CartAdapter;
import com.xidian.xienong.model.CartCommodity;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.shoppingmall.me.MyAddressActivity;
import com.xidian.xienong.shoppingmall.me.SelectAddressActivity;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class AffirmOrderActivity extends AppCompatActivity {
     //单个商品订单

    private Commodity commodity;
    private Toolbar toolbar;
    private ImageView picture;
    private TextView name;
    private TextView price;
    private TextView number;
    private TextView number2;
    private TextView add;
    private TextView reduce;
    private TextView total_number;
    private TextView total_price;
    private TextView total_price2;
    private TextView submit;
    private TextView specification;
    private double price_money;//单价
    private double total_money;//总价
    private OKHttp httpUrl;

    private TextView me_name;
    private TextView me_tel;
    private TextView address;
    private String receiver=null;
    private String receiver_tel=null;
    private String receiver_addr=null;
    private RelativeLayout rl_address_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affirm_order_activity);

        commodity = (Commodity)getIntent().getSerializableExtra("commodity");

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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(number.getText().toString());
                if (num<Integer.parseInt(commodity.getCurrentCount())){
                    number.setText(""+(++num));
                    number2.setText("x"+num);
                    total_money = price_money * num;
                    total_number.setText("共"+num+"件商品");
                    total_price.setText("小计：￥"+String.format("%.2f", total_money)+"元");
                    total_price2.setText("合计：￥"+String.format("%.2f", total_money)+"元");
                }
                else {
                    ToastCustom.makeToast(AffirmOrderActivity.this, "不能超过最大产品数量");
                }
            }
        });
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(number.getText().toString());
                if (num>1){
                    number.setText(""+(--num));
                    number2.setText("x"+num);
                    total_money = price_money * num;
                    total_number.setText("共"+num+"件商品");
                    total_price.setText("小计：￥"+String.format("%.2f", total_money)+"元");
                    total_price2.setText("合计：￥"+String.format("%.2f", total_money)+"元");
                }
                else {
                    ToastCustom.makeToast(AffirmOrderActivity.this, "购买数量不能低于1件");
                }
            }
        });
        rl_address_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AffirmOrderActivity.this, SelectAddressActivity.class), 1432);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receiver!=null && receiver_tel!=null && receiver_addr!=null){
                    try {
                        JSONArray request_str = new JSONArray();
                        JSONObject order = new JSONObject();
                        order.put("order_number", "");//订单号，后台生成
                        SharePreferenceUtil sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
                        order.put("user_id", sp.getUserId());
                        order.put("money_of_all_order", total_money);//订单总金额
                        order.put("money_of_integral", 0);//积分抵现
                        order.put("transportation_expense", 0);//运费
                        order.put("total_money", total_money);//实付总金额
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        order.put("order_generated_time", format.format(new Date()));//订单生成时间
                        order.put("order_status", 0);//订单状态：待付款0，待发货1，待收货2，已签收3
                        order.put("valid_order", 1);//订单是否有效，0无效，1有效
                        order.put("remark", "");//备注
                        order.put("receiver", receiver);//收货人
                        order.put("receiver_tel", receiver_tel);//收货人电话
                        order.put("receiver_addr", receiver_addr);//收货人地址
                        JSONArray order_detail = new JSONArray();
                        JSONObject comm = new JSONObject();
                        comm.put("commodity_id", commodity.getCommodityId());
                        comm.put("is_evaluation", 0);//是否评价
                        comm.put("unit_price", price_money);//商品单价
                        comm.put("order_commodity_quantities", number.getText().toString());//商品数量
                        comm.put("order_commodity_total_price", total_money);//商品总价
                        comm.put("remark", "");
                        order_detail.put(comm);
                        order.put("order_detail", order_detail);
                        request_str.put(order);

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("request_str", request_str.toString());

                        httpUrl = OKHttp.getInstance();
                        httpUrl.post(Url.CreateMallOrder, map, new BaseCallback<String>() {
                            @Override
                            public void onRequestBefore() {
                                Log.i("mcx", "CreateMallOrder : " + Url.CreateMallOrder);
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
                                    switch (result){
                                        case "REPEAT_ORDER":
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "重复提交订单，请求失败！");
                                            break;
                                        case "BLANK_ORDER":
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "空白订单，请求失败！");
                                            break;
                                        case "INSERT_ORDER_FAIL":
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "订单生成失败，未知错误！");
                                            break;
                                        case "SUCCESS":
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "订单生成成功！");
                                            break;
                                        case "FAIL":
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "未知错误或无可用数据库，请求失败！");
                                            break;
                                        case "INVALID_ORDER":
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "无效订单，订单商品数量大于剩余商品数量！");
                                            break;
                                        default:
                                            ToastCustom.makeToast(AffirmOrderActivity.this, "未知错误！");
                                            break;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response response, int errorCode, Exception e) {
                                Log.i("mcx", "error : " + e.toString());
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    ToastCustom.makeToast(AffirmOrderActivity.this, "请填写收货地址！");
                }

            }
        });
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(AffirmOrderActivity.this).load(commodity.getCommodityImageList().get(0).getCommodityImgUrl())
                .centerCrop().placeholder(R.drawable.empty_picture).into(picture);
        name.setText(commodity.getCommodityName());
        price_money = Double.parseDouble(commodity.getPrice()) * Double.parseDouble(commodity.getDiscount());
        price.setText("￥"+String.format("%.2f", price_money)+"元");
        total_money = price_money;
        total_price.setText("小计：￥"+String.format("%.2f", price_money)+"元");
        total_price2.setText("合计：￥"+String.format("%.2f", price_money)+"元");
        specification.setText(commodity.getSpecification());


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
                        for (int i=0; i<allAdress.length(); i++){
                            JSONObject temp = allAdress.getJSONObject(i);
                            if ("1".equals(temp.getString("is_default"))){
                                receiver = temp.getString("consignee_name");
                                me_name.setText("收货人："+receiver);
                                receiver_tel = temp.getString("phone");
                                me_tel.setText(receiver_tel);
                                receiver_addr = temp.getString("area")+" "+temp.getString("detail");
                                address.setText("收货地址："+receiver_addr);
                                break;
                            }
                        }
                    }
                    else {
                        ToastCustom.makeToast(AffirmOrderActivity.this, "获取数据失败");
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
        toolbar = (Toolbar)findViewById(R.id.shopping_car_toolbar);
        picture = (ImageView) findViewById(R.id.picture);
        name = (TextView) findViewById(R.id.tv_name);
        price = (TextView) findViewById(R.id.tv_price);
        number = (TextView) findViewById(R.id.tv_num);
        number2 = (TextView) findViewById(R.id.tv_number);
        add = (TextView) findViewById(R.id.tv_add);
        reduce = (TextView) findViewById(R.id.tv_reduce);
        total_number = (TextView) findViewById(R.id.total_number);
        total_price = (TextView) findViewById(R.id.total_price);
        total_price2 = (TextView) findViewById(R.id.total_price2);
        submit = (TextView) findViewById(R.id.tv_buy_ok);
        specification = (TextView) findViewById(R.id.tv_specification);
        me_name = (TextView) findViewById(R.id.me_name);
        me_tel = (TextView) findViewById(R.id.me_tel);
        address = (TextView) findViewById(R.id.address);
        rl_address_information = (RelativeLayout) findViewById(R.id.rl_address_information);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==2341 && data!=null){
            receiver = data.getStringExtra("consignee_name");
            me_name.setText("收货人："+receiver);
            receiver_tel = data.getStringExtra("phone");
            me_tel.setText(receiver_tel);
            receiver_addr = data.getStringExtra("area")+" "+data.getStringExtra("detail");
            address.setText("收货地址："+receiver_addr);
        }
    }
}
