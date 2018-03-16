package com.xidian.xienong.agriculture.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.DispatchedDriverAdapter;
import com.xidian.xienong.adapter.DispatchedMachineAdapter;
import com.xidian.xienong.adapter.DispatchedResourceAdapter;
import com.xidian.xienong.agriculture.me.CancleOrderActivity;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.model.Resource;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
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
 * Created by koumiaojuan on 2017/6/13.
 */

public class DispatchResourceActivity extends AppCompatActivity{

    private Toolbar mToolbar;

    private Button confirmResource;
    private ListView avaliableMachineListView,avaliableDriverListView,finalDispatchedResourceList;
    private DispatchedMachineAdapter machineAdapter;
    private DispatchedDriverAdapter driverAdapter;
    private DispatchedResourceAdapter resourceAdapter;
    private List<Machine> dispatchedMachines = new ArrayList<Machine>();
    private List<Driver> dispatchedDrivers = new ArrayList<Driver>();
    private ArrayList<Resource> selectedResource = new ArrayList<Resource>();
    private RelativeLayout confirmAdd;
    private View footerView;
    private Button cancleOrder,cancleOrder1,continueOperating;
    private Button dispatchOrder;
    private SharePreferenceUtil sp;
    private OrderBean orderBean;
    private LinearLayout noAvaliableResource;
    private Machine choosedMachine;
    private Driver choosedDriver;
    private Resource choosedResource;
    private String machine_id_list="",driver_id_list="";
    private LinearLayout twoListView;
    private ImageButton down,up;
    private View headerView;
    private String adviceState="";
    private TextView machineName;
    private String reason="";
    private OKHttp httpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch_resource_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(cancleOrder1.getText().equals("订单已取消")){
                    Intent intent = new Intent(Constants.CANCLE_ORDER_ACTION);
                    sendBroadcast(intent);
                }else{
                    if(cancleOrder.getText().equals("取消订单")){
                        Intent intent = new Intent(Constants.GRAB_ORDER_SUCCESS_ACTION);
                        sendBroadcast(intent);
                    }else{
                        Intent intent = new Intent(Constants.CANCLE_GRABBED_ORDER_ACTION);
                        sendBroadcast(intent);
                    }
                }
                finish();
            }
        });

        avaliableMachineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                machineAdapter.setSelected(true, arg2);
                choosedMachine = dispatchedMachines.get(arg2);

            }
        });

        avaliableDriverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                driverAdapter.setSelected(true, arg2);
                choosedDriver = dispatchedDrivers.get(arg2);
            }
        });

        finalDispatchedResourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if(arg2 != 0){
                    resourceAdapter.setSelected(true, arg2-1);
                    choosedResource = selectedResource.get(arg2-1);
                    resourceAdapter.notifyDataSetChanged();
                }

            }
        });

        down.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Resource resource = new Resource();
                resource.setMachine(choosedMachine);
                resource.setDriver(choosedDriver);
                if(choosedMachine == null || choosedDriver ==null ){
                    Toast.makeText(getApplicationContext(), "请选择农机或司机！",Toast.LENGTH_SHORT).show();
                }else{
                    if(hasResource(resource)){
                        Toast.makeText(getApplicationContext(), "请选择新的农机及司机，请重新选择！",Toast.LENGTH_SHORT).show();
                    }else{
                        selectedResource.add(resource);
                        dispatchedMachines.remove(choosedMachine);
                        dispatchedDrivers.remove(choosedDriver);
                        for(int i=0;i < dispatchedMachines.size();i++){
                            machineAdapter.setSelected(false, i);
                        }
                        for(int i=0;i < dispatchedDrivers.size();i++){
                            driverAdapter.setSelected(false, i);
                        }
                        for(int i=0;i < selectedResource.size();i++){
                            resourceAdapter.setSelected(false, i);
                        }
                        machineAdapter.notifyDataSetChanged();
                        driverAdapter.notifyDataSetChanged();
                        resourceAdapter.notifyDataSetChanged();

                        choosedMachine =null;
                        choosedDriver=null;
                    }
                }
            }
        });


        up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				 TODO Auto-generated method stub
                if(choosedResource == null){
                    Toast.makeText(getApplicationContext(), "请选择农机与司机！",Toast.LENGTH_SHORT).show();
                }else{
                    selectedResource.remove(choosedResource);
                    dispatchedMachines.add(choosedResource.getMachine());
                    dispatchedDrivers.add(choosedResource.getDriver());
                    for(int i=0;i < dispatchedMachines.size();i++){
                        machineAdapter.setSelected(false, i);
                    }
                    for(int i=0;i < dispatchedDrivers.size();i++){
                        driverAdapter.setSelected(false, i);
                    }
                    for(int i=0;i < selectedResource.size();i++){
                        resourceAdapter.setSelected(false, i);
                    }
                    resourceAdapter.notifyDataSetChanged();
                    machineAdapter.notifyDataSetChanged();
                    driverAdapter.notifyDataSetChanged();
                    choosedResource = null;
                }
            }
        });

        dispatchOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(dispatchOrder.getText().toString().equals("订单已取消")){
                    ToastCustom.makeToast(DispatchResourceActivity.this, "该订单已自动取消");
                }else{
                    if(checkInfoIsCompleted()){
                        new SweetAlertDialog(DispatchResourceActivity.this, SweetAlertDialog.TIP_TYPE)
                                .setTitleText("确认派单")
                                .setContentText("您确认上述派单信息无误吗")
                                .setCancelText("不，谢谢")
                                .setConfirmText("好，派单")
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
                                        dispatchOrder();
                                    }
                                })
                                .show();
                    }
                }
            }
        });

        cancleOrder1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DispatchResourceActivity.this,CancleOrderActivity.class);
                intent.putExtra("orderBean", orderBean);
                intent.setType("dispatch_resource_activity");
                startActivityForResult(intent, 200);

            }
        });

        cancleOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SweetAlertDialog(DispatchResourceActivity.this, SweetAlertDialog.TIP_TYPE)
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
                                Intent intent = new Intent(DispatchResourceActivity.this,CancleOrderActivity.class);
                                intent.putExtra("orderBean", orderBean);
                                intent.setType("choose_cancle_reason");
                                startActivityForResult(intent, 200);
//		            	 sendCancleOrderRequest(orderBean.getOrder_id());
                            }
                        })
                        .show();
            }
        });
    }

    private void dispatchOrder() {
        getParameters();
        Map<String, String> map = new HashMap<String, String>();
        map.put("reservation_time", orderBean.getReservation_time());
        map.put("order_id", orderBean.getOrder_id());
        map.put("machine_id_list", machine_id_list);
        map.put("driver_id_list", driver_id_list);
        httpUrl.post(Url.DispatchOrder,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseDispatchOrderResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });

    }

    private void getParameters() {
        machine_id_list = "";
        driver_id_list = "";
        for(int i=0; i < selectedResource.size(); i++){
            if( i == selectedResource.size()-1){
                machine_id_list = machine_id_list + selectedResource.get(i).getMachine().getMachine_id();
                driver_id_list = driver_id_list + selectedResource.get(i).getDriver().getDriver_id();
            }else{
                machine_id_list = machine_id_list + selectedResource.get(i).getMachine().getMachine_id()+"-";
                driver_id_list = driver_id_list + selectedResource.get(i).getDriver().getDriver_id()+"-";
            }
        }
    }

    private void parseDispatchOrderResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                orderBean.setResources(selectedResource);
                Intent intent = new Intent(DispatchResourceActivity.this,ReceivedOrderDetailActivity.class);
                intent.putExtra("orderBean", orderBean);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "派单失败，请重试",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected boolean checkInfoIsCompleted() {
        // TODO Auto-generated method stub
        if(selectedResource.size() == 0){
            ToastCustom.makeToast(DispatchResourceActivity.this, "请添加派出农机与司机");
            return false;
        }
        return true;
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        httpUrl = OKHttp.getInstance();
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        orderBean = (OrderBean) getIntent().getSerializableExtra("orderBean");
        machineName.setText(""+orderBean.getMachine_category());
        adviceState = orderBean.getAdviceState();
        if(adviceState.equals("0")){
            cancleOrder.setText("申请取消订单");
            cancleOrder.setEnabled(true);
            dispatchOrder.setEnabled(true);//
            dispatchOrder.setBackgroundResource(R.drawable.corner);
            cancleOrder.setBackgroundResource(R.drawable.verify_corner);
            dispatchOrder.setVisibility(View.VISIBLE);
            cancleOrder1.setVisibility(View.GONE);
        }else if(adviceState.equals("1")){
            cancleOrder.setText("取消请求已发出");
            cancleOrder.setEnabled(false);
            dispatchOrder.setEnabled(false);
            dispatchOrder.setBackgroundResource(R.drawable.corner);
            cancleOrder.setBackgroundResource(R.drawable.gray_corner);
            dispatchOrder.setVisibility(View.GONE);
            cancleOrder1.setVisibility(View.GONE);
        }else if(adviceState.equals("2")){
            cancleOrder.setText("对方已同意取消订单");
            dispatchOrder.setEnabled(true);
            dispatchOrder.setBackgroundResource(R.drawable.gray_corner);
            cancleOrder.setEnabled(false);
            cancleOrder.setBackgroundResource(R.drawable.gray_corner);
            dispatchOrder.setVisibility(View.GONE);
            cancleOrder1.setVisibility(View.VISIBLE);
        }else{
            dispatchOrder.setText("继续派单");
            cancleOrder.setText("对方已拒绝取消订单");
            cancleOrder1.setText("强制取消订单");
            dispatchOrder.setEnabled(true);
            dispatchOrder.setBackgroundResource(R.drawable.corner);
            cancleOrder.setEnabled(false);
            cancleOrder.setBackgroundResource(R.drawable.gray_corner);
            dispatchOrder.setVisibility(View.VISIBLE);
            cancleOrder1.setVisibility(View.VISIBLE);
        }
        getAvaliableResource();
    }

    private void getAvaliableResource() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("worker_id", sp.getUserId());
        map.put("category_id", orderBean.getCategory_id());
        map.put("reservation_time",orderBean.getReservation_time());
        httpUrl.post(Url.GetAvaliableResource,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });

    }

    private void parseResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                dispatchedMachines.clear();
                JSONArray idleMachinelist = jb.getJSONArray("idleMachine");
                for(int i=0; i < idleMachinelist.length(); i++){
                    JSONObject object = idleMachinelist.getJSONObject(i);
                    Machine machine = new Machine();
                    machine.setMachine_id(object.getString("machine_id"));
//					machine.setTrademark_name(object.getString("trademark"));
                    machine.setIdentification_number(object.getString("machine_identification"));
                    machine.setReservationTime(object.getString("reservation_time"));
                    dispatchedMachines.add(machine);
                }
                JSONArray idleDriverlist = jb.getJSONArray("idleDriver");
                for(int i=0; i < idleDriverlist.length(); i++){
                    JSONObject object = idleDriverlist.getJSONObject(i);
                    Driver driver = new Driver();
                    driver.setDriver_id(object.getString("driver_id"));
                    driver.setDriver_name(object.getString("driver_name"));
                    driver.setDriver_telephone(object.getString("driver_telephone"));
                    dispatchedDrivers.add(driver);
                }
                Log.i("kmj","----machine------" +dispatchedMachines.size()+"----driver-----"+ dispatchedDrivers.size());
                if(dispatchedMachines.size() == 0 || dispatchedDrivers.size() == 0){
                    twoListView.setVisibility(View.GONE);
                    avaliableMachineListView.setVisibility(View.GONE);
                    avaliableDriverListView.setVisibility(View.GONE);
                    confirmAdd.setVisibility(View.GONE);
                    finalDispatchedResourceList.setVisibility(View.GONE);
                    noAvaliableResource.setVisibility(View.VISIBLE);
                }else{
                    twoListView.setVisibility(View.VISIBLE);
                    avaliableMachineListView.setVisibility(View.VISIBLE);
                    avaliableDriverListView.setVisibility(View.VISIBLE);
                    confirmAdd.setVisibility(View.VISIBLE);
                    finalDispatchedResourceList.setVisibility(View.VISIBLE);
                    noAvaliableResource.setVisibility(View.GONE);
                    machineAdapter.notifyDataSetChanged();
                    driverAdapter.notifyDataSetChanged();
                }

            }else{
                Toast.makeText(getApplicationContext(), "获取可派农机与司机失败，请重试",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected boolean hasResource(Resource resource) {
        // TODO Auto-generated method stub
        for(Resource rs : selectedResource){
            if(rs.getMachine().getMachine_id() == resource.getMachine().getMachine_id() ||
                    rs.getDriver().getDriver_id() == resource.getDriver().getDriver_id() ){
                return true;
            }
        }
        return false;
    }

    private void initViews() {
        mToolbar = (Toolbar)findViewById(R.id.dispatch_order_toolbar);
        machineName = (TextView)findViewById(R.id.avaliable_machine_listView_header);
        down = (ImageButton)findViewById(R.id.down);
        up = (ImageButton)findViewById(R.id.up);
        twoListView = (LinearLayout)findViewById(R.id.ll_two_listview);
        avaliableMachineListView = (ListView)findViewById(R.id.avaliable_machine_listView);
        avaliableDriverListView = (ListView)findViewById(R.id.avaliable_driver_listView);
        machineAdapter = new DispatchedMachineAdapter(getApplicationContext(), dispatchedMachines);
        avaliableMachineListView.setAdapter(machineAdapter);
        driverAdapter = new DispatchedDriverAdapter(getApplicationContext(), dispatchedDrivers);
        avaliableDriverListView.setAdapter(driverAdapter);
        confirmAdd = (RelativeLayout)findViewById(R.id.confirm_dispatch_machine_and_driver);
        noAvaliableResource = (LinearLayout)findViewById(R.id.no_avaliable_resource);
        headerView = getLayoutInflater().inflate(R.layout.dispatched_resource_header_view,null);
        footerView = getLayoutInflater().inflate(R.layout.dispatch_order_footer,null);
        dispatchOrder = (Button)footerView.findViewById(R.id.btn_dispatch_order);
        cancleOrder = (Button)footerView.findViewById(R.id.btn_dispatch_cancle);
        cancleOrder1 = (Button)footerView.findViewById(R.id.btn_dispatch_cancle_1);
        continueOperating = (Button)footerView.findViewById(R.id.btn_dispatch_continue);

        finalDispatchedResourceList = (ListView)findViewById(R.id.dispatched_resource_listView);
        finalDispatchedResourceList.addHeaderView(headerView);
        finalDispatchedResourceList.addFooterView(footerView);
        resourceAdapter = new DispatchedResourceAdapter(DispatchResourceActivity.this, selectedResource);
        finalDispatchedResourceList.setAdapter(resourceAdapter);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(cancleOrder1.getText().equals("订单已取消")){
            Intent intent = new Intent(Constants.CANCLE_ORDER_ACTION);
            sendBroadcast(intent);
        }else{
            if(cancleOrder.getText().equals("取消订单")){
                Intent intent = new Intent(Constants.GRAB_ORDER_SUCCESS_ACTION);
                sendBroadcast(intent);
            }else{
                Intent intent = new Intent(Constants.CANCLE_GRABBED_ORDER_ACTION);
                sendBroadcast(intent);
            }
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 ) {
            if(resultCode == 201){
                cancleOrder1.setText("订单已取消");
                cancleOrder1.setClickable(false);
                cancleOrder1.setBackgroundResource(R.drawable.gray_corner);
                dispatchOrder.setClickable(false);
                dispatchOrder.setBackgroundResource(R.drawable.gray_corner);
                Intent intent = new Intent(Constants.CANCLE_ORDER_ACTION);
                sendBroadcast(intent);
                finish();

            }
            if(resultCode == 208){
                sendCancleOrderRequest(orderBean.getOrder_id());
            }
        }
    }

    private void sendCancleOrderRequest(String order_id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", orderBean.getOrder_id());
        map.put("cancle_method", "0");
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
                        dispatchOrder.setClickable(false);
                        dispatchOrder.setBackgroundResource(R.drawable.gray_corner);
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


}
