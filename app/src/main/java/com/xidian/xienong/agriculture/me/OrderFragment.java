package com.xidian.xienong.agriculture.me;

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
import com.xidian.xienong.adapter.OrderAdapter;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.model.OrderBean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;
import static com.xidian.xienong.agriculture.me.MyOrderActivity.mViewPager;


/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OrderAdapter.OnItemClickListener{

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderAdapter adapter;
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
        adapter = new OrderAdapter(getActivity(),list);
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
        if(mViewPager.getCurrentItem()==0){
            refreshAnnounceList(Url.GetAllAnnouncement);
        }else if(mViewPager.getCurrentItem()==1){
            refreshAnnounceList(Url.GetWaitingToReceiveAnnouncement);
        }else if(mViewPager.getCurrentItem()==2){
            refreshAnnounceList(Url.GetHaveReceivedAnnouncement);
        }else if(mViewPager.getCurrentItem()==3){
            refreshAnnounceList(Url.GetIsOperatingAnnouncement);
        }else{
            refreshAnnounceList(Url.GetOperatedAnnouncement);
        }

    }

    private void refreshAnnounceList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("farmer_id", "6223");
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
                    order.setAdviceState(object.getString("advice_state"));
                    order.setCancleTime(object.getString("cancle_time"));
                    order.setCancleReason(object.getString("cancle_reason"));
//                    order.setApplyCancleReason(object.getString("apply_cancle_reason"));
//                    order.setApplyCancleReasonId(object.getString("apply_cancle_reason_id"));
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



    @Override
    public void onItemClick(View view, int position) {
        Log.i("kmj","wojds;ld");
        Intent intent = new Intent(getActivity(),AnnounceDetailActivity.class);
        intent.putExtra("announce", list.get(position));
        startActivityForResult(intent, 4);
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
                        refreshAnnounceList(Url.GetOperatedAnnouncement);
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
        if(requestCode == 4){
            if (resultCode == 300 ) {
                onRefresh();
            }

        }
    }

}
