package com.xidian.xienong.shoppingmall.me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.OneMallOrderListItem;
import com.xidian.xienong.model.MallOrderMiddle;
import com.xidian.xienong.util.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

public class MallOrderDetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView tv_time,tv_status,tv_commidity_money,tv_transport,tv_jifen_money,tv_total_number,tv_all_total_money;
    private TextView tv_receiver_name,tv_receiver_addr,tv_receiver_tel;
    private NoScrollListView lv_one_mall_order;
    private TextView button_1,button_2,button_3;
    private ScrollView scv;
    private LinearLayout ll_view;
    private String mall_order_id,mall_order_number,mall_commodity_money,mall_interal_money,transportation_expanse,total_money,order_generated_time,str_status;
    private String total_number,order_receiver,order_receiver_addr,order_receiver_tel;
    private List<MallOrderMiddle> oneMallOrderMiddle = new ArrayList<MallOrderMiddle>();
    private OneMallOrderListItem adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_order_detail);
        initViews();
        initDatas();
        initEvents();
    }

    void initViews(){
        scv = (ScrollView)findViewById(R.id.scv_mall_order);
        scv.smoothScrollTo(0,0);

        tv_receiver_name = (TextView)findViewById(R.id.me_name);
        tv_receiver_addr = (TextView)findViewById(R.id.me_address);
        tv_receiver_tel = (TextView)findViewById(R.id.me_tel);

        mToolbar = (Toolbar) findViewById(R.id.mall_order_detail_toolbar);
        tv_time = (TextView)findViewById(R.id.one_mall_order_generate_time);
        tv_status = (TextView)findViewById(R.id.one_mall_order_status);
        lv_one_mall_order = (NoScrollListView) findViewById(R.id.lv_one_mall_order);
        tv_commidity_money = (TextView)findViewById(R.id.one_mall_order_comm_money);
        tv_transport = (TextView)findViewById(R.id.one_mall_order_transport_money);
        tv_jifen_money = (TextView)findViewById(R.id.one_mall_order_jifen_money);
        tv_total_number = (TextView)findViewById(R.id.one_mall_order_comm_number);
        tv_all_total_money = (TextView)findViewById(R.id.one_mall_order_xiaoji);
        button_1 = (TextView)findViewById(R.id.one_mall_order_delete_1);
        button_2 = (TextView)findViewById(R.id.one_mall_order_trans_2);
        button_3 = (TextView)findViewById(R.id.one_mall_order_evaluate_3);
        ll_view = (LinearLayout)findViewById(R.id.ll_view);
        /*ll_view.setFocusable(true);
        ll_view.setFocusableInTouchMode(true);
        ll_view.requestFocus();*/
    }

    void initDatas(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        str_status=getIntent().getStringExtra("order_status");
        mall_order_id = getIntent().getStringExtra("mall_order_id");
        mall_order_number = getIntent().getStringExtra("mall_order_number");//订单编号
        mall_commodity_money = getIntent().getStringExtra("mall_commodity_money");
        mall_interal_money = getIntent().getStringExtra("mall_interal_money");
        transportation_expanse = getIntent().getStringExtra("transportation_expanse");
        total_money = getIntent().getStringExtra("total_money");
        order_generated_time = getIntent().getStringExtra("order_generated_time");
        order_receiver = getIntent().getStringExtra("order_receiver");
        order_receiver_addr = getIntent().getStringExtra("order_receiver_addr");
        order_receiver_tel = getIntent().getStringExtra("order_receiver_tel");
        oneMallOrderMiddle = (List<MallOrderMiddle>)getIntent().getSerializableExtra("oneMallOrderMiddle");
        total_number =  getIntent().getStringExtra("total_number");


       // total_number = getIntent().getIntExtra("total_number",1);
        tv_receiver_name.setText("收货人："+order_receiver);
        tv_receiver_addr.setText("收货地址："+order_receiver_addr);
        tv_receiver_tel.setText(""+order_receiver_tel);
        tv_time.setText("下单时间："+order_generated_time);
        tv_status.setText(str_status);
        tv_commidity_money.setText("¥"+mall_commodity_money);
        tv_transport.setText("¥"+transportation_expanse);
        tv_jifen_money.setText("-¥"+mall_interal_money);
        tv_total_number.setText(total_number+"件商品");
        tv_all_total_money.setText("小计：¥"+total_money+"元");

        adapter = new OneMallOrderListItem(oneMallOrderMiddle,MallOrderDetailActivity.this);
        lv_one_mall_order.setAdapter(adapter);

    }

    private void initEvents() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(str_status.equals("待付款")){
            button_1.setVisibility(View.GONE);
            button_2.setText("取消订单");
            button_3.setText("付款");
        }else if(str_status.equals("待发货")){
            button_1.setVisibility(View.GONE);
            button_2.setVisibility(View.GONE);
            button_3.setText("提醒发货");
        }
        else if(str_status.equals("待收货")){
            button_1.setVisibility(View.GONE);
            button_2.setVisibility(View.GONE);
            button_3.setText("查看物流");
        }else if(str_status.equals("待评价")){
            button_1.setText("删除订单");
            button_2.setText("查看物流");
            button_3.setText("评价");
        }
    }

}
