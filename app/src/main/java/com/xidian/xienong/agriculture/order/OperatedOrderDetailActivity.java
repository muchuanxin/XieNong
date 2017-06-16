package com.xidian.xienong.agriculture.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.DispatchedResourceShowAdapter;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.model.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/13.
 */

public class OperatedOrderDetailActivity extends AppCompatActivity{

    private TextView orderCode;
    private TextView orderState;
    private TextView tv_crop_address;
    private TextView tv_machine_type;
    private TextView tv_reservation;
    private TextView tv_crop_type;
    private TextView tv_crop_num;
    private LinearLayout showCancleInfo;
    private TextView showCancleTime,showCancleReason;
    private OrderBean orderBean;
    private ListView resourceListView;
    private DispatchedResourceShowAdapter adapter;
    private List<Resource> resources = new ArrayList<Resource>();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operated_order_detail_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        // TODO Auto-generated method stub
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderBean");
        orderCode.setText(orderBean.getOrderCode());
        if(orderBean.getCancleReason().equals("")){
            orderState.setText("已完成");
            showCancleInfo.setVisibility(View.GONE);
        }else{
            orderState.setText("已取消");
            showCancleInfo.setVisibility(View.VISIBLE);
            showCancleTime.setText(orderBean.getCancleTime()+",订单已取消");
            showCancleReason.setText(orderBean.getCancleReason());
        }

        tv_crop_address.setText(orderBean.getCrop_address());
        tv_machine_type.setText(orderBean.getMachine_category());
        tv_reservation.setText(orderBean.getReservation_time());
        tv_crop_type.setText(orderBean.getCropland_type());
        tv_crop_num.setText(orderBean.getCropland_number()+"亩");
        resources = orderBean.getResources();
        Log.i("kmj","-resources--"+resources.size());
        adapter = new DispatchedResourceShowAdapter(OperatedOrderDetailActivity.this, resources);
        resourceListView.setAdapter(adapter);
    }

    private void initViews() {
        // TODO Auto-generated method stub
        mToolbar = (Toolbar)findViewById(R.id.operated_order_detail_toolbar);
        orderCode = (TextView)findViewById(R.id.operated_order_code);
        orderState = (TextView)findViewById(R.id.operated_order_state);
        tv_crop_address = (TextView)findViewById(R.id.tv_operated_detail_address_1);
        tv_machine_type = (TextView)findViewById(R.id.tv_operated_machine_type_1);
        tv_reservation = (TextView)findViewById(R.id.tv_operated_reservation_1);
        tv_crop_type = (TextView)findViewById(R.id.tv_operated_crop_type_1);
        tv_crop_num = (TextView)findViewById(R.id.tv_operated_crop_num_1);
        showCancleInfo = (LinearLayout)findViewById(R.id.show_operated_cancle_info);
        showCancleTime = (TextView)findViewById(R.id.show_operated_cancle_time);
        showCancleReason = (TextView)findViewById(R.id.show_operated_cancle_reason);
        resourceListView = (ListView)findViewById(R.id.operated_resource_listView);

    }



}
