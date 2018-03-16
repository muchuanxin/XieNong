package com.xidian.xienong.shoppingmall.me;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.MallOrderAdapter;
import com.xidian.xienong.model.CommodityInOrder;
import com.xidian.xienong.model.MallOrderBean;
import com.xidian.xienong.model.MallOrderBottom;
import com.xidian.xienong.model.MallOrderMiddle;
import com.xidian.xienong.model.MallOrderTop;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.NoScrollListView;
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

import static com.xidian.xienong.shoppingmall.me.AllMallOrderActivity.mViewPager;

//import static com.xidian.xienong.agriculture.me.MyOrderActivity.mViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MallAllOrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,MallOrderAdapter.OnItemClickListener {
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NoScrollListView mListView;
//    private RecyclerView mRecyclerView;
//    private RecyclerView.LayoutManager mLayoutManager;
    private NestedScrollView scrollview;
    private LinearLayoutManager mLayoutManager;
    private MallOrderAdapter adapter;
    private List<MallOrderBean> list = new ArrayList<MallOrderBean>();
    private OKHttp httpUrl;
    private SharePreferenceUtil sp;
    public Map<Integer,Integer> indexMap=new HashMap<Integer, Integer>() ;
    private ProgressDialog progDialog = null;
    private Handler handler = new Handler();


    public MallAllOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mView==null){
            mView=inflater.inflate(R.layout.fragment_mall_orderk, container, false);
        }
        scrollview = (NestedScrollView)mView.findViewById(R.id.mall_order_nested_scrollview);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.mall_order_swiperefreshlayout);
        super.onActivityCreated(savedInstanceState);
        mListView = (NoScrollListView)mView.findViewById(R.id.mall_order_listView);
        sp = new SharePreferenceUtil(getContext(), Constants.SAVE_USER);
       // mRecyclerView = (RecyclerView) mView.findViewById(R.id.order_recyclerview);
        /* sp = new SharePreferenceUtil(getContext(), Constants.SAVE_USER);
        list = (List<MallOrderBean>) getArguments().get("data");
        indexMap = (Map<Integer,Integer>)getArguments().get("indexMap");

        adapter = new MallOrderAdapter(getActivity(),list);
        adapter.setOnItemClickListener(this);
        mListView.setAdapter(adapter);*/

       /*******www*******/
        showProgressDialog();
        handler.postDelayed(LOAD_DATA,500);

       /*******www*******/


        // 刷新时，指示器旋转后变化的颜色
        httpUrl = OKHttp.getInstance();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int key : indexMap.keySet()){
                    if (key<=position && position<=indexMap.get(key)){
                        String mall_order_id=((MallOrderTop)list.get(key)).getMall_order_id();
                        String mall_order_number=((MallOrderTop)list.get(key)).getMall_order_number();//订单编号
                        String mall_commodity_money=((MallOrderTop)list.get(key)).getMall_commodity_money();//订单商品价钱
                        String mall_interal_money=((MallOrderTop)list.get(key)).getMall_interal_money();//订单商品价钱
                        String transportation_expanse=((MallOrderTop)list.get(key)).getTransportation_expanse();
                        String total_money=((MallOrderTop)list.get(key)).getTotal_money();
                        String order_generated_time=((MallOrderTop)list.get(key)).getOrder_generated_time();
                        String order_status= ((MallOrderTop)list.get(key)).getOrder_status();

                        List<MallOrderMiddle> middleList=new ArrayList<>();

                        for (int i=key+1; i<indexMap.get(key); i++){

                            String commodity_id= ((MallOrderMiddle)list.get(i)).getCommodityId();
                            String commodity_name= ((MallOrderMiddle)list.get(i)).getCommodityName();
                            String unit_price= ((MallOrderMiddle)list.get(i)).getPrice();
                            String specification= ((MallOrderMiddle)list.get(i)).getSpecification();
                            String image_url= ((MallOrderMiddle)list.get(i)).getImage_url();
                            String quantities= ((MallOrderMiddle)list.get(i)).getBuy_number();
                            boolean isEvaluated= ((MallOrderMiddle)list.get(i)).isEvalauted();
                            MallOrderMiddle mallOrderMiddle=new MallOrderMiddle(1,commodity_id,commodity_name,unit_price,specification,image_url,quantities,isEvaluated);
                            middleList.add(mallOrderMiddle);

                        }

                        int total_number=indexMap.get(key)-key-1;

                        Intent intent = new Intent(getActivity(),MallOrderDetailActivity.class);
                        intent.putExtra("mall_order_id", mall_order_id);
                        intent.putExtra("mall_order_number", mall_order_number);
                        intent.putExtra("mall_commodity_money", mall_commodity_money);
                        intent.putExtra("mall_interal_money", mall_interal_money);
                        intent.putExtra("transportation_expanse", transportation_expanse);
                        intent.putExtra("total_money", total_money);
                        intent.putExtra("order_generated_time", order_generated_time);
                        intent.putExtra("order_status", order_status);
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
        });
    }


    public void refreshData(){
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onRefresh() {
        if(mViewPager.getCurrentItem()==0){
            refreshMallOrderList(Url.GetAllMallOrder);
        }else if(mViewPager.getCurrentItem()==1){
            refreshMallOrderList(Url.GetWaitingToPayOrder);
        }else if(mViewPager.getCurrentItem()==2){
            refreshMallOrderList(Url.GetWaitingToDispatchGoods);
        }else if(mViewPager.getCurrentItem()==3){
            refreshMallOrderList(Url.GetWaitingToReceivingGoods);
        }else{
            refreshMallOrderList(Url.GetHaveSignedGoods);
        }
    }

    public void refreshMallOrderList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
        //  map.put("farmer_id", sp.getUserId());
        map.put("user_id", sp.getUserId());
        httpUrl.post(url,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("www", "url : " + url);
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

    private void parseResponse(String response,String url) {
        try {
            JSONObject jb = new JSONObject(response);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                list.clear();
                JSONArray orderlist = jb.getJSONArray("mallOrders");
                for(int i=0; i < orderlist.length(); i++){
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
                    String order_receiver_tel=object.getString("receiver_tel");
                    MallOrderTop mallOrderTop= new MallOrderTop(0,mall_order_id,mall_order_number,mall_commodity_money,mall_interal_money,transportation_expanse,total_money,order_generated_time,order_status,order_receiver,order_receiver_addr,order_receiver_tel);
                    list.add(mallOrderTop);

                    int indexTop= list.indexOf(mallOrderTop);

                    JSONArray commArray = object.getJSONArray("commodityList");
                    List<CommodityInOrder> commodityInOrders = new ArrayList<CommodityInOrder>();
                    commodityInOrders.clear();
                    boolean allIsEvaluated=true;

                    List<MallOrderMiddle> middleList=new ArrayList<>();
                    for(int j = 0; j < commArray.length(); j++){
                        JSONObject jo =  commArray.getJSONObject(j);
                        String commodity_id=jo.getString("commodity_id");
                        String commodity_name=jo.getString("commodity_name");
                        String unit_price=jo.getString("unit_price");
                        String specification=jo.getString("specification");
                        String quantities=jo.getString("quantities");
                        String image_url=jo.getString("image");
                        boolean isEvaluated=jo.getBoolean("isEvaluated");
                        MallOrderMiddle mallOrderMiddle=new MallOrderMiddle(1,commodity_id,commodity_name,unit_price,specification,image_url,quantities,isEvaluated);
                        list.add(mallOrderMiddle);
                        middleList.add(mallOrderMiddle);

                    }

                    String transportation_expense=object.getString("transportation_expense");
                    /*for (int k=0;k<middleList.size();k++){
                        if(!middleList.get(k).isEvalauted()){
                            allIsEvaluated=false;
                        }

                    }*/

                    String order_evaluated_remark = object.getString("evaluated_remark");
                    if("待评价".equals(order_evaluated_remark)){
                        allIsEvaluated=false;
                    }
                    MallOrderBottom mallOrderBottom = new MallOrderBottom(2,transportation_expense,"共"+commArray.length(),total_money,order_status,allIsEvaluated);
                    list.add(mallOrderBottom);
                    int indexBottom=list.indexOf(mallOrderBottom);
                    indexMap.put(indexTop,indexBottom);
                    //  list.add(mallOrder);
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

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
    }

    private Runnable LOAD_DATA = new Runnable() {
        @Override
        public void run() {
            //在这里数据内容加载到Fragment上
            list = (List<MallOrderBean>) getArguments().get("data");
            indexMap = (Map<Integer,Integer>)getArguments().get("indexMap");
            adapter = new MallOrderAdapter(getActivity(),list);
            adapter.setOnItemClickListener(MallAllOrderFragment.this);
            mListView.setAdapter(adapter);
            dissmissProgressDialog();
        }
    };

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("请稍候...");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

}
