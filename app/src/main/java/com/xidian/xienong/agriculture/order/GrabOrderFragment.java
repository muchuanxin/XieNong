package com.xidian.xienong.agriculture.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.GrabOrderAdapter;
import com.xidian.xienong.adapter.OrderAdapter;
import com.xidian.xienong.agriculture.me.AnnounceDetailActivity;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.model.Resource;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.RecyclerDecoration;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

import static com.xidian.xienong.agriculture.me.MyOrderActivity.mViewPager;
import static com.xidian.xienong.agriculture.order.RecommendOrderActivity.mRecommendViewPager;

/**
 * Created by koumiaojuan on 2017/6/12.
 */

public class GrabOrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OrderAdapter.OnItemClickListener{
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GrabOrderAdapter adapter;
    private List<OrderBean> list = new ArrayList<OrderBean>();
    private OKHttp httpUrl;
    private SharePreferenceUtil sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.order_fragment_main, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.order_swiperefreshlayout);
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.order_recyclerview);
        sp = new SharePreferenceUtil(getContext(), Constants.SAVE_USER);
        list = (List<OrderBean>) getArguments().get("data");
        mLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new GrabOrderAdapter(getActivity(),list);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new RecyclerDecoration(
                getActivity(), LinearLayoutManager.VERTICAL, R.drawable.divider));

        // 刷新时，指示器旋转后变化的颜色
        httpUrl = OKHttp.getInstance();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    public void refreshData(){
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        if(mRecommendViewPager.getCurrentItem()==0){
            refreshRecommendOrderList(Url.RecommendOptimalOrder);
        }else if(mRecommendViewPager.getCurrentItem()==1){
            refreshRecommendOrderList(Url.GetHaveGrabbedOrder);
        }else if(mRecommendViewPager.getCurrentItem()==2){
            refreshRecommendOrderList(Url.GetIsOperatingOrder);
        }else{
            refreshRecommendOrderList(Url.GetOperatedOrder);
        }

    }

    private void refreshRecommendOrderList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("worker_id", sp.getWorkerId());
        map.put("worker_id", "41");
        if(url.equals(Url.RecommendOptimalOrder)){
//            map.put("worker_longtitude", sp.getLongtitude());
//            map.put("worker_lantitude", sp.getLantitude());
            map.put("worker_longtitude", "108.93849000");
            map.put("worker_lantitude", "34.34024000");
        }

        httpUrl.post(url,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "url : " + url);
            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseResponse(resultResponse,url);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });


    }

    private void parseResponse(String resultResponse,String url) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                list.clear();
                JSONArray orderlist = jb.getJSONArray("grabOrders");
                for(int i=0; i < orderlist.length(); i++){
                    JSONObject object = orderlist.getJSONObject(i);
                    OrderBean order = new OrderBean();
                    order.setDistance(object.getDouble("distance"));
                    order.setOrder_id(object.getString("order_id"));
                    order.setOrderCode(object.getString("orderCode"));
                    order.setOrderState(object.getString("orderState"));
                    order.setFarmer_id(object.getString("farmer_id"));
                    order.setFarmer_name(object.getString("farmer_name"));
                    order.setTelephone(object.getString("telephone"));
                    order.setHeadphoto(object.getString("head_photo"));

                    order.setCrop_address(object.getString("crop_address"));
                    order.setCrop_lantitude(object.getDouble("crop_lantitude"));
                    order.setCrop_longtitude(object.getDouble("crop_longtitude"));
                    order.setCategory_id(object.getString("category_id"));
                    order.setMachine_category(object.getString("category_name"));
                    order.setCropland_type(object.getString("cropland_type"));
                    order.setReservation_time(object.getString("reservation_time"));
                    order.setAdviceState(object.getString("advice_state"));
                    order.setUpload_time(object.getString("upload_time"));
                    order.setGrabOrderTime(object.getString("grab_order_time"));
                    order.setDispatchTime(object.getString("dispatch_time"));
                    order.setOperatingTime(object.getString("operating_time"));
                    order.setOperatedTime(object.getString("has_operated_time"));
                    order.setEvaluateTime(object.getString("evaluate_time"));

                    order.setCropland_number(object.getDouble("cropland_number"));
                    order.setEvaluate(object.getBoolean("isEvaluate"));
                    order.setDeleteByWorker(object.getBoolean("isDeletedByWorker"));
                    order.setCancleTime(object.getString("cancle_time"));
                    order.setCancleReason(object.getString("cancle_reason"));
                    order.setCancleMethod(object.getString("cancle_method"));
                    JSONArray driverArray = object.getJSONArray("drivers");
                    List<Driver> drivers = new ArrayList<Driver>();
                    drivers.clear();
                    for(int j = 0; j < driverArray.length(); j++){
                        JSONObject jo =  driverArray.getJSONObject(j);
                        Driver driver = new Driver();
                        driver.setDriver_id(jo.getString("driver_id"));
                        driver.setDriver_name(jo.getString("driver_name"));
                        driver.setDriver_telephone(jo.getString("driver_telephone"));
                        drivers.add(driver);
                    }
                    order.setDrivers(drivers);
                    JSONArray machineArray = object.getJSONArray("machines");
                    List<Machine> machines = new ArrayList<Machine>();
                    machines.clear();
                    for(int j = 0; j < machineArray.length(); j++){
                        JSONObject jo =  machineArray.getJSONObject(j);
                        Machine machine = new Machine();
                        machine.setMachine_id(jo.getString("machine_id"));
                        machine.setIdentification_number(object.getString("category_name") +"-"+jo.getString("identification_number"));
                        machines.add(machine);
                    }
                    order.setMachines(machines);
                    List<Resource> resources = new ArrayList<Resource>();
                    resources.clear();
                    for(int i1 = 0; i1 < machines.size();i1++){
                        Resource re = new Resource();
                        re.setMachine(machines.get(i1));
                        re.setDriver(drivers.get(i1));
                        resources.add(re);
                    }
                    order.setResources(resources);
                    list.add(order);
                }
                Collections.sort(list);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);

            }else{
                Toast.makeText(getActivity(), "获取可抢单列表失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void onItemClick(View view, int position) {
        OrderBean order = list.get(position);
        if(order.getOrderState().equals("待接单")){
            Intent intent = new Intent(getActivity(),GrabOrderActivity.class);
            intent.putExtra("longtitude", order.getCrop_longtitude());
            intent.putExtra("lantitude", order.getCrop_lantitude());
            intent.putExtra("orderBean", order);
            startActivityForResult(intent,14);
        }else if(order.getOrderState().equals("已接单")){
            if(order.getMachines().size() == 0){
                Intent intent = new Intent(getActivity(),DispatchResourceActivity.class);
                intent.putExtra("orderBean", order);
                startActivityForResult(intent,14);
            }else{
                Intent intent = new Intent(getActivity(),ReceivedOrderDetailActivity.class);
                intent.putExtra("orderBean", order);
                startActivityForResult(intent,14);
            }
        }else if(order.getOrderState().equals("作业中")){
            Intent intent = new Intent(getActivity(),ReceivedOrderDetailActivity.class);
            intent.putExtra("orderBean", order);
            startActivityForResult(intent,14);
        }else{
            Intent intent = new Intent(getActivity(),OperatedOrderDetailActivity.class);
            intent.putExtra("orderBean", order);
            startActivityForResult(intent,14);
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {
        final OrderBean order = list.get(position);
        if(order.getOrderState().equals("已完成") || order.getOrderState().equals("已取消")){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.TIP_TYPE)
                    .setTitleText("删除订单")
                    .setContentText("您是否要删除订单？")
                    .setCancelText("不，谢谢")
                    .setConfirmText("好，删除")
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
                            deleteOrder(order);
                        }
                    })
                    .show();
        }
    }

    private void deleteOrder(OrderBean order) {
        final OrderBean order1 = order;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id",order1.getOrder_id());
        map.put("isWorker",sp.getisWorker());
        httpUrl.post(Url.DeleteHaveOperatedOrder,map,new BaseCallback<String>(){
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
                        ToastCustom.makeToast(getActivity(), "删除成功");
                        refreshRecommendOrderList(Url.GetOperatedOrder);
                    }else{
                        ToastCustom.makeToast(getActivity(), message);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 14 ) {
            if (resultCode == 13 || resultCode == 15) {
                mRecommendViewPager.setCurrentItem(3);
                refreshRecommendOrderList(Url.GetOperatedOrder);
            }
        }
    }
}
