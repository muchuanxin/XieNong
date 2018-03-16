package com.xidian.xienong.shoppingmall.me;

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
import com.xidian.xienong.adapter.MallsOrderAdapter;
import com.xidian.xienong.model.CommodityInOrder;
import com.xidian.xienong.model.MallOrderBean;
import com.xidian.xienong.model.MallOrderBottom;
import com.xidian.xienong.model.MallOrderMiddle;
import com.xidian.xienong.model.MallOrderTop;
import com.xidian.xienong.model.MallsOrderBean;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.RecyclerDecoration;
import com.xidian.xienong.util.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

import static com.xidian.xienong.shoppingmall.me.AllTheMallOrderActivity.mallOrderViewPager;


/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class MallMyOrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,MallsOrderAdapter.OnItemClickListener{

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OKHttp httpUrl;
    private SharePreferenceUtil sp;
  //  private List<MallsOrderBean> list = new ArrayList<MallsOrderBean>();
    private MallsOrderAdapter adapter;
    private List<MallOrderBean> temp = new ArrayList<MallOrderBean>();
    public Map<Integer,Integer> indexMap=new HashMap<Integer, Integer>() ;


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
       // list = (List<MallsOrderBean>) getArguments().get("data");
        temp = (List<MallOrderBean>) getArguments().get("data");
        indexMap = (Map<Integer,Integer>)getArguments().get("indexMap");
        mLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
       // adapter = new MallsOrderAdapter(getActivity(),list);
        adapter = new MallsOrderAdapter(getActivity(),temp);
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
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh() {
        if(mallOrderViewPager.getCurrentItem()==0){
            requestMallOrderList(Url.GetAllMallOrder);
        }else if(mallOrderViewPager.getCurrentItem()==1){
            requestMallOrderList(Url.GetWaitingToPayOrder);
        }else if(mallOrderViewPager.getCurrentItem()==2){
            requestMallOrderList(Url.GetWaitingToDispatchGoods);
        }else if(mallOrderViewPager.getCurrentItem()==3){
            requestMallOrderList(Url.GetWaitingToReceivingGoods);
        }else{
            requestMallOrderList(Url.GetWaitingToEvaluateMallOrders);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
          //......
        for (int key : indexMap.keySet()){
            if (key<=position && position<=indexMap.get(key)){
                String mall_order_id=((MallOrderTop)temp.get(key)).getMall_order_id();
                String mall_order_number=((MallOrderTop)temp.get(key)).getMall_order_number();//订单编号
                String mall_commodity_money=((MallOrderTop)temp.get(key)).getMall_commodity_money();//订单商品价钱
                String mall_interal_money=((MallOrderTop)temp.get(key)).getMall_interal_money();//订单商品价钱
                String transportation_expanse=((MallOrderTop)temp.get(key)).getTransportation_expanse();
                String total_money=((MallOrderTop)temp.get(key)).getTotal_money();
                String order_generated_time=((MallOrderTop)temp.get(key)).getOrder_generated_time();
                String order_status= ((MallOrderTop)temp.get(key)).getOrder_status();
                /*增加收货信息*/
                String order_receiver=((MallOrderTop)temp.get(key)).getOrder_receiver();
                String order_receiver_addr=((MallOrderTop)temp.get(key)).getOrder_receiver_addr();
                String order_receiver_tel= ((MallOrderTop)temp.get(key)).getOrder_receiver_tel();

                List<MallOrderMiddle> middleList=new ArrayList<>();

                for (int i=key+1; i<indexMap.get(key); i++){

                    String commodity_id= ((MallOrderMiddle)temp.get(i)).getCommodityId();
                    String commodity_name= ((MallOrderMiddle)temp.get(i)).getCommodityName();
                    String unit_price= ((MallOrderMiddle)temp.get(i)).getPrice();
                    String specification= ((MallOrderMiddle)temp.get(i)).getSpecification();
                    String image_url= ((MallOrderMiddle)temp.get(i)).getImage_url();
                    String quantities= ((MallOrderMiddle)temp.get(i)).getBuy_number();
                    boolean isEvaluated= ((MallOrderMiddle)temp.get(i)).isEvalauted();
                    MallOrderMiddle mallOrderMiddle=new MallOrderMiddle(1,commodity_id,commodity_name,unit_price,specification,image_url,quantities,isEvaluated);
                    middleList.add(mallOrderMiddle);

                }

              //  int total_number=indexMap.get(key)-key-1;

                Intent intent = new Intent(getActivity(),MallOrderDetailActivity.class);
                intent.putExtra("mall_order_id", mall_order_id);
                intent.putExtra("mall_order_number", mall_order_number);
                intent.putExtra("mall_commodity_money", mall_commodity_money);
                intent.putExtra("mall_interal_money", mall_interal_money);
                intent.putExtra("transportation_expanse", transportation_expanse);
                intent.putExtra("total_money", total_money);
                intent.putExtra("order_generated_time", order_generated_time);
                intent.putExtra("order_status", order_status);
                intent.putExtra("order_receiver", order_receiver);
                intent.putExtra("order_receiver_addr", order_receiver_addr);
                intent.putExtra("order_receiver_tel", order_receiver_tel);
                String total_number= ((MallOrderBottom)temp.get(indexMap.get(key))).getComm_number();
                Log.e("www","共"+total_number);
                intent.putExtra("total_number",total_number);
                intent.putExtra("oneMallOrderMiddle",(Serializable)middleList);
                startActivity(intent);

                        /*for (int i=key; i<=indexMap.get(key); i++){
                            list.get(i);
                        }*/
                break;
            }
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private void requestMallOrderList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", sp.getUserId());
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

    private void parseResponse(String resultResponse, String url) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
             //   list.clear();
                JSONArray orderlist = jb.getJSONArray("mallOrders");
                temp.clear();
                for(int i=0; i < orderlist.length(); i++){
                    /*JSONObject object = orderlist.getJSONObject(i);

                    MallsOrderBean order = new MallsOrderBean();
                    temp.clear();

                    order.setMall_order_id(object.getString("mall_order_id"));
                    order.setMall_order_number(object.getString("order_number"));//订单编号
                    order.setMall_commodity_money(object.getString("commodity_money"));//订单商品价钱
                    order.setMall_interal_money(object.getString("interal_money"));//订单商品价钱
                    order.setTransportation_expanse(object.getString("transportation_expense"));
                    order.setTotal_money(object.getString("total_money"));
                    order.setOrder_generated_time(object.getString("order_generated_time"));
                    order.setOrder_status(object.getString("order_status"));
                    order.setEvaluated_remark(object.getString("evaluated_remark"));
                    MallOrderTop mallOrderTop= new MallOrderTop(0,order.getOrder_generated_time(),order.getOrder_status());
                    temp.add(mallOrderTop);*/
                    JSONObject object = orderlist.getJSONObject(i);
                    String mall_order_id=object.getString("mall_order_id");
                    String mall_order_number=object.getString("order_number");//订单编号
                    String mall_commodity_money=object.getString("commodity_money");//订单商品价钱
                    String mall_interal_money=object.getString("interal_money");//订单商品价钱
                    String transportation_expanse=object.getString("transportation_expense");
                    String total_money=object.getString("total_money");
                    String order_generated_time=object.getString("order_generated_time");
                    String order_status=object.getString("order_status");
                    String order_receiver=object.getString("receiver");
                    String order_receiver_addr=object.getString("receiver_addr");
                    String order_receiver_tel= object.getString("receiver_tel");

                    MallOrderTop mallOrderTop= new MallOrderTop(0,mall_order_id,mall_order_number,mall_commodity_money,mall_interal_money,transportation_expanse,total_money,order_generated_time,order_status,order_receiver,order_receiver_addr,order_receiver_tel);
                    temp.add(mallOrderTop);
                    int indexTop= temp.indexOf(mallOrderTop);

                    List<CommodityInOrder> commodityInOrders = new ArrayList<CommodityInOrder>();
                    commodityInOrders.clear();
                    JSONArray commArray = object.getJSONArray("commodityList");
                    int total_number=0;
                    List<MallOrderMiddle> middleList=new ArrayList<>();
                    boolean allIsEvaluated=true;
                    for(int j = 0; j < commArray.length(); j++){
                        /*JSONObject jo =  commArray.getJSONObject(j);
                        CommodityInOrder commodity = new CommodityInOrder();
                        commodity.setCommodityId(jo.getString("commodity_id"));
                        commodity.setCommodityName(jo.getString("commodity_name"));
                        commodity.setPrice(jo.getString("unit_price"));
                        commodity.setSpecification(jo.getString("specification"));
                        commodity.setQuantities(jo.getString("quantities"));
                        commodity.setImage_url(jo.getString("image"));
                        commodity.setEvaluated(jo.getBoolean("isEvaluated"));
                        MallOrderMiddle mallOrderMiddle=new MallOrderMiddle(1,commodity.getCommodityId(),commodity.getCommodityName(),
                                commodity.getPrice(),commodity.getSpecification(),commodity.getImage_url(),
                                commodity.getQuantities(),commodity.isEvaluated());
                        temp.add(mallOrderMiddle);
                        commodityInOrders.add(commodity);*/
                        JSONObject jo =  commArray.getJSONObject(j);
                        String commodity_id=jo.getString("commodity_id");
                        String commodity_name=jo.getString("commodity_name");
                        String unit_price=jo.getString("unit_price");
                        String specification=jo.getString("specification");
                        String quantities=jo.getString("quantities");
                        String image_url=jo.getString("image");
                        boolean isEvaluated=jo.getBoolean("isEvaluated");
                        MallOrderMiddle mallOrderMiddle=new MallOrderMiddle(1,commodity_id,commodity_name,unit_price,specification,image_url,quantities,isEvaluated);
                        temp.add(mallOrderMiddle);
                        middleList.add(mallOrderMiddle);
                    }
                   // order.setCommodities(commodityInOrders);

                    String transportation_expense=object.getString("transportation_expense");
                    String order_evaluated_remark = object.getString("evaluated_remark");
                    if("待评价".equals(order_evaluated_remark)){
                        allIsEvaluated=false;
                    }
                    for (int k=0;k<middleList.size();k++){
                        total_number+=Integer.parseInt(middleList.get(k).getBuy_number());
                    }
                    MallOrderBottom mallOrderBottom = new MallOrderBottom(2,transportation_expense,"共"+total_number,total_money,order_status,allIsEvaluated);
                    temp.add(mallOrderBottom);
                    int indexBottom=temp.indexOf(mallOrderBottom);
                    indexMap.put(indexTop,indexBottom);

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

}
