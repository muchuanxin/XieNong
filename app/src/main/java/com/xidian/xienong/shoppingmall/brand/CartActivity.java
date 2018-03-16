package com.xidian.xienong.shoppingmall.brand;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.CartAdapter;
import com.xidian.xienong.model.CartCommodity;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.ListCartCommodity;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
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

/**
 * Created by koumiaojuan on 2017/7/2.
 */

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView createOrder;
    private ListView cart_list_view;
    private TextView total;
    private TextView total_price;
    private CheckBox all_chekbox;
    private OKHttp httpUrl;
    private List<CartCommodity> comms;
    private Button btn_edit;
    private CartAdapter cartAdapter;
    private boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_activity);
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
        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    if (cartAdapter!=null && comms.size()!=0){
                        Map<Integer, Boolean> map = cartAdapter.getCheck_map();
                        List<CartCommodity> submit_comms = new ArrayList<CartCommodity>();
                        boolean count_status = false;
                        for (int i=0; i<comms.size(); i++){
                            if (map.get(i)){
                                count_status = true;
                                submit_comms.add(comms.get(i));
                            }
                        }
                        if (count_status){
                            Intent intent = new Intent(CartActivity.this,AffirmOrderFromCartActivity.class);
                            ListCartCommodity listCartCommodity = new ListCartCommodity();
                            listCartCommodity.setCartCommodityList(submit_comms);
                            intent.putExtra("listCartCommodity", listCartCommodity);
                            startActivity(intent);
                        }
                        else {
                            ToastCustom.makeToast(CartActivity.this, "没有选中商品");
                        }
                    }
                    else {
                        ToastCustom.makeToast(CartActivity.this, "购物车里没有商品");
                    }
                }
                else {
                    if (cartAdapter!=null){
                        final Map<Integer, Boolean> check_map = cartAdapter.getCheck_map();
                        String ids = "";
                        for (int key : check_map.keySet()){
                            if (check_map.get(key)){
                                ids+=(comms.get(key).getShopping_cart_id()+",");
                            }
                        }
                        if (!"".equals(ids)){
                            SharePreferenceUtil sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("user_id", sp.getUserId());
                            map.put("shopping_cart_id_arr", ids.substring(0,ids.length()-1));
                            httpUrl.post(Url.DeleteCommodityFromCart, map, new BaseCallback<String>() {
                                @Override
                                public void onRequestBefore() {
                                    Log.i("mcx", "DeleteCommodityFromCart : " + Url.DeleteCommodityFromCart);
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
                                            ToastCustom.makeToast(CartActivity.this, "删除成功！");
                                            for (int i=comms.size()-1; i>=0; i--){
                                                if (check_map.get(i)){
                                                    comms.remove(i);
                                                }
                                            }
                                            cartAdapter = new CartAdapter(CartActivity.this, comms);
                                            cart_list_view.setAdapter(cartAdapter);
                                            cartAdapter.showReduceNumberPlus(true);
                                        }
                                        else {
                                            ToastCustom.makeToast(CartActivity.this, "删除失败，请重试！");
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
                        else {
                            ToastCustom.makeToast(CartActivity.this, "请选择要删除的商品");
                        }
                    }
                }
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    createOrder.setText("删除");
                    createOrder.setBackgroundColor(0xFFef8000);
                    btn_edit.setText("完成");
                    total.setVisibility(View.INVISIBLE);
                    total_price.setVisibility(View.INVISIBLE);
                    if (cartAdapter!=null){
                        cartAdapter.showReduceNumberPlus(status);
                    }
                    status = false;
                }
                else {
                    createOrder.setText("结算(0)");
                    createOrder.setBackgroundColor(0xFFdc143c);
                    btn_edit.setText("编辑");
                    total.setVisibility(View.VISIBLE);
                    total_price.setVisibility(View.VISIBLE);
                    total_price.setText("￥0.00");
                    if (cartAdapter!=null){
                        cartAdapter.showReduceNumberPlus(status);
                    }

                    calculateTotalNumber();
                    calculateTotalPrice();

                    String modify_shopping_cart_id_arr="";
                    String commodity_quantities_arr="";
                    for (int i=0; i<comms.size(); i++){
                        CartCommodity comm = comms.get(i);
                        if (comm.getAdded_total_quantities() != comm.getUpdate_quantities()){
                            modify_shopping_cart_id_arr+=(comm.getShopping_cart_id()+",");
                            commodity_quantities_arr+=(comm.getUpdate_quantities()+",");
                        }
                    }
                    if (!"".equals(modify_shopping_cart_id_arr)){
                        SharePreferenceUtil sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
                        Log.e("CartActivity：user_id", sp.getUserId());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("user_id", sp.getUserId());
                        map.put("modify_shopping_cart_id_arr", modify_shopping_cart_id_arr.substring(0,modify_shopping_cart_id_arr.length()-1));
                        map.put("commodity_quantities_arr", commodity_quantities_arr.substring(0,commodity_quantities_arr.length()-1));
                        Log.e("modify_shopping_cart", modify_shopping_cart_id_arr);
                        Log.e("commodity_quantities", commodity_quantities_arr);
                        httpUrl.post(Url.FinishModifyCart, map, new BaseCallback<String>() {
                            @Override
                            public void onRequestBefore() {
                                Log.i("mcx", "FinishModifyCart : " + Url.FinishModifyCart);
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
                                        ToastCustom.makeToast(CartActivity.this, "修改成功！");
                                        for (int i=0; i<comms.size(); i++){
                                            CartCommodity comm = comms.get(i);
                                            if (comm.getAdded_total_quantities() != comm.getUpdate_quantities()){
                                                comm.setAdded_total_quantities(comm.getUpdate_quantities());
                                            }
                                        }
                                    }
                                    else {
                                        ToastCustom.makeToast(CartActivity.this, "修改失败，请重试！");
                                        for (int i=0; i<comms.size(); i++){
                                            CartCommodity comm = comms.get(i);
                                            if (comm.getAdded_total_quantities() != comm.getUpdate_quantities()){
                                                comm.setUpdate_quantities(comm.getAdded_total_quantities());
                                            }
                                        }
                                        cartAdapter = new CartAdapter(CartActivity.this, comms);
                                        cart_list_view.setAdapter(cartAdapter);
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
                    else {
                        ToastCustom.makeToast(CartActivity.this, "没有改动");
                    }

                    status = true;
                }
            }
        });
        all_chekbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartAdapter!=null){
                    cartAdapter.allCheck(all_chekbox.isChecked());
                }
            }
        });
        /*all_chekbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cartAdapter!=null){
                    cartAdapter.allCheck(isChecked);
                }
            }
        });*/
    }

    public void calculateTotalNumber(){
        if (cartAdapter!=null){
            Map<Integer, Boolean> map = cartAdapter.getCheck_map();
            int count = 0;
            for (int key : map.keySet()){
                if (map.get(key)){
                    count++;
                }
            }
            createOrder.setText("结算("+count+")");
        }
    }

    public void calculateTotalPrice(){
        if (cartAdapter!=null){
            Map<Integer, Boolean> map = cartAdapter.getCheck_map();
            double total_money = 0.0;
            for (int key : map.keySet()){
                if (map.get(key)){
                    CartCommodity comm = comms.get(key);
                    total_money += (comm.getOrigin_price()*comm.getDiscount()*comm.getUpdate_quantities());
                }
            }
            total_price.setText("￥"+String.format("%.2f", total_money));
        }
    }

    public void checkAllCheck(){
        if (cartAdapter!=null){
            Map<Integer, Boolean> map = cartAdapter.getCheck_map();
            int count = 0;
            for (int key : map.keySet()){
                if (map.get(key)){
                    count++;
                }
            }
            if (count==map.size()){
                all_chekbox.setChecked(true);
            }
            else {
                all_chekbox.setChecked(false);
            }
        }
    }

    public boolean isStatus() {
        return status;
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        httpUrl = OKHttp.getInstance();
        SharePreferenceUtil sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", sp.getUserId());
        Log.e("user_id", sp.getUserId());
        httpUrl.post(Url.GetAllCommodityFromCart, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("mcx", "GetAllCommodityFromCart : " + Url.GetAllCommodityFromCart);
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
                        JSONArray orders = jb.getJSONArray("orders");
                        comms = new ArrayList<CartCommodity>();
                        for (int i=0; i<orders.length(); i++){
                            JSONObject temp = orders.getJSONObject(i);
                            CartCommodity comm = new CartCommodity();
                            comm.setCommodity_id(temp.getString("commodity_id"));
                            comm.setCommodity_name(temp.getString("commodity_name"));
                            comm.setUnit(temp.getString("unit"));
                            comm.setWeight(temp.getString("weight"));
                            comm.setCommodity_variety(temp.getString("commodity_variety"));
                            comm.setSpecification(temp.getString("specification"));
                            comm.setProducing_area(temp.getString("producing_area"));
                            comm.setCurrent_quantities(temp.getInt("current_quantities"));
                            comm.setOrigin_price(temp.getDouble("origin_price"));
                            comm.setDiscount(temp.getDouble("discount"));
                            comm.setShopping_cart_id(temp.getString("shopping_cart_id"));
                            comm.setAdded_total_quantities(temp.getInt("add_comodity_quantities"));
                            comm.setUpdate_quantities(temp.getInt("add_comodity_quantities"));
                            comm.setCommodity_pic_url(temp.getString("commodity_pic_url"));
                            //comm.setTrans_expense(temp.getDouble("trans_expense"));
                            comms.add(comm);
                        }
                        cartAdapter = new CartAdapter(CartActivity.this, comms);
                        cart_list_view.setAdapter(cartAdapter);
                    }
                    else {
                        ToastCustom.makeToast(CartActivity.this, "获取数据失败，请重试");
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
        createOrder=(TextView)findViewById(R.id.tv_go_to_pay);
        total=(TextView)findViewById(R.id.tv_total);
        total_price=(TextView)findViewById(R.id.tv_total_price);
        cart_list_view = (ListView) findViewById(R.id.cart_list_view);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        all_chekbox = (CheckBox) findViewById(R.id.all_chekbox);
    }


}
