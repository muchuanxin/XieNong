package com.xidian.xienong.agriculture.me;

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
import com.xidian.xienong.adapter.OrderAdapter;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.MarqueeView;
import com.xidian.xienong.util.RecyclerDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

import static com.xidian.xienong.agriculture.me.MyOrderActivity.finishedOrderList;
import static com.xidian.xienong.agriculture.me.MyOrderActivity.operingOrderList;
import static com.xidian.xienong.agriculture.me.MyOrderActivity.receivedOrderList;
import static com.xidian.xienong.agriculture.me.MyOrderActivity.waitingOrderList;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderAdapter adapter;
    private List<OrderBean> list = new ArrayList<OrderBean>();
    private OKHttp httpUrl;

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
        list = (List<OrderBean>) getArguments().get("data");
        mLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new OrderAdapter(getActivity(),list);
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
        refreshAnnounceList(Url.GetAllAnnouncement);
    }

    private void refreshAnnounceList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("farmer_id", "6219");
        httpUrl.post(url,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "url refresh: " + url);
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
                list.clear();
                JSONArray orderlist = jb.getJSONArray("AnnouncementList");
                for(int i=0; i < orderlist.length(); i++){
                    JSONObject object = orderlist.getJSONObject(i);

                    OrderBean order = new OrderBean();
                    order.setOrder_id(object.getString("order_id"));
                    order.setOrderCode(object.getString("orderCode"));
                    order.setFarmer_id(object.getString("farmer_id"));
                    order.setFarmer_name(object.getString("farmer_name"));
                    order.setWorker_id(object.getString("worker_id"));
                    order.setWorker_name(object.getString("worker_name"));
                    order.setTelephone(object.getString("farmer_telephone"));
                    order.setWorker_telephone(object.getString("worker_telephone"));
                    order.setHeadphoto(object.getString("head_photo"));
                    order.setUpload_time(object.getString("upload_time"));
                    order.setCrop_address(object.getString("crop_address"));
                    order.setCrop_lantitude(object.getDouble("crop_lantitude"));
                    order.setCrop_longtitude(object.getDouble("crop_longtitude"));
                    order.setMachine_category(object.getString("category_name"));
                    order.setCropland_type(object.getString("cropland_type"));
                    order.setReservation_time(object.getString("reservation_time"));
                    order.setCropland_number(object.getDouble("cropland_number"));
                    order.setEvaluate(object.getBoolean("isEvaluate"));
                    order.setDeletedByFarmer(object.getBoolean("isDeletedByFarmer"));
                    order.setOrderState(object.getString("orderState"));
                    order.setCancleTime(object.getString("cancle_time"));
                    order.setCancleReason(object.getString("cancle_reason"));
                    order.setAdviceState(object.getString("advice_state"));
                    order.setPrice(object.getInt("work_price"));

                    JSONArray driverArray = object.getJSONArray("drivers");
                    List<Driver> drivers = new ArrayList<Driver>();
                    drivers.clear();
                    for(int j = 0; j < driverArray.length(); j++){
                        JSONObject jo =  driverArray.getJSONObject(j);
                        Driver driver = new Driver();
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
                        machine.setIdentification_number(jo.getString("identification_number"));
                        machines.add(machine);
                    }
                    order.setMachines(machines);

                    JSONArray machineImageArray = object.getJSONArray("machine_url");
                    List<MachineImage> machineImages = new ArrayList<MachineImage>();
                    machineImages.clear();
                    for(int j = 0; j < machineImageArray.length(); j++){
                        JSONObject jo =  machineImageArray.getJSONObject(j);
                        MachineImage image = new MachineImage();
                        image.setUrl(jo.getString("image_url"));
                        machineImages.add(image);
                    }
                    order.setMachineImages(machineImages);
                    list.add(order);
                }
                fillFragmentsWithData(list);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }else {
                Toast.makeText(getActivity(), "获取订单失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    private void fillFragmentsWithData(List<OrderBean> list) {
        waitingOrderList.clear();
        receivedOrderList.clear();
        operingOrderList.clear();
        finishedOrderList.clear();
        for(OrderBean order : list){
            if(order.getOrderState().equals("待接单")){
                waitingOrderList.add(order);
            }else if(order.getOrderState().equals("已接单")){
                receivedOrderList.add(order);
            }else if(order.getOrderState().equals("作业中")){
                operingOrderList.add(order);
            }else{
                finishedOrderList.add(order);
            }
        }
    }
}
