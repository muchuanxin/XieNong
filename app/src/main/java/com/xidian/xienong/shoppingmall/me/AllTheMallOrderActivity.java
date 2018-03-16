package com.xidian.xienong.shoppingmall.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.ViewPagerAdapter;
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

import static android.support.design.widget.TabLayout.MODE_FIXED;

/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class AllTheMallOrderActivity  extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private SharePreferenceUtil sp;
    private OKHttp httpUrl;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    public static ViewPager mallOrderViewPager;
    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments = new ArrayList<>();
   // private List<MallsOrderBean> list = new ArrayList<MallsOrderBean>();
    private List<MallOrderBean> temp = new ArrayList<MallOrderBean>();
    public static ViewPagerAdapter mViewPagerAdapter;
    /*public static List<List<MallsOrderBean>>  mallOrders = new ArrayList<>();
    public static List<MallsOrderBean> allMallOrderList = new ArrayList<>();
    public static List<MallsOrderBean> waitingToPayOrderList = new ArrayList<>();
    public static List<MallsOrderBean> waitingToDispatchOrders = new ArrayList<>();
    public static List<MallsOrderBean> waitingToReceivingOrders = new ArrayList<>();
    public static List<MallsOrderBean> waitingToEvaluateOrders = new ArrayList<>();*/
    public static List<List<MallOrderBean>>  mallOrders = new ArrayList<>();
    public static List<MallOrderBean> allMallOrderList = new ArrayList<>();
    public static List<MallOrderBean> waitingToPayOrderList = new ArrayList<>();
    public static List<MallOrderBean> waitingToDispatchOrders = new ArrayList<>();
    public static List<MallOrderBean> waitingToReceivingOrders = new ArrayList<>();
    public static List<MallOrderBean> waitingToEvaluateOrders = new ArrayList<>();
    private int currentItem = 0;
    public Map<Integer,Integer> indexMap=new HashMap<Integer, Integer>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_mall_order_activity);
        initViews();
        initData();
        configViews();
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
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        httpUrl = OKHttp.getInstance();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitles = getResources().getStringArray(R.array.shop_order_titles);
        currentItem=getIntent().getIntExtra("currentItem",0);
        mallOrders.add(allMallOrderList);
        mallOrders.add(waitingToPayOrderList);
        mallOrders.add(waitingToDispatchOrders);
        mallOrders.add(waitingToReceivingOrders);
        mallOrders.add(waitingToEvaluateOrders);
        for(int i=0; i < mTitles.length;i++){
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("data", (Serializable)mallOrders.get(i));
            mBundle.putSerializable("indexMap",(Serializable)indexMap);
            MallMyOrderFragment mFragment = new MallMyOrderFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(mFragment);
        }
        requestMallOrderList(Url.GetAllMallOrder);
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
                temp.clear();
                JSONArray orderlist = jb.getJSONArray("mallOrders");
                for(int i=0; i < orderlist.length(); i++){
                   /* JSONObject object = orderlist.getJSONObject(i);

                    MallsOrderBean order = new MallsOrderBean();
                  //  temp.clear();

                    order.setMall_order_id(object.getString("mall_order_id"));
                    order.setMall_order_number(object.getString("order_number"));//订单编号
                    order.setMall_commodity_money(object.getString("commodity_money"));//订单商品价钱
                    order.setMall_interal_money(object.getString("interal_money"));//订单积分可抵价钱
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
                    /*"receiver": "吕翔","receiver_addr": "西电","receiver_tel": "1234567",*/
                    String order_receiver=object.getString("receiver");
                    String order_receiver_addr=object.getString("receiver_addr");
                    String order_receiver_tel=object.getString("receiver_tel");
                    MallOrderTop mallOrderTop= new MallOrderTop(0,mall_order_id,mall_order_number,mall_commodity_money,mall_interal_money,transportation_expanse,total_money,order_generated_time,order_status,order_receiver,order_receiver_addr,order_receiver_tel);
                    temp.add( mallOrderTop);
                    int topIndex = temp.indexOf(mallOrderTop);

                    List<CommodityInOrder> commodityInOrders = new ArrayList<CommodityInOrder>();
                    commodityInOrders.clear();
                    JSONArray commArray = object.getJSONArray("commodityList");
                    boolean allIsEvaluated=true;
                    int total_number=0;
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
                        temp.add(mallOrderMiddle);
                        middleList.add(mallOrderMiddle);
                    }
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
                    int bottomIndex=temp.indexOf(mallOrderBottom);
                    indexMap.put(topIndex,bottomIndex);
                }
                fillFragmentsWithData(url,temp,indexMap);
            }else {
                Toast.makeText(AllTheMallOrderActivity.this, "获取订单失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

 /*   private void fillFragmentsWithData(String url, List<MallsOrderBean> list) {
        if(url.equals(Url.GetAllMallOrder)){
            allMallOrderList.clear();
            allMallOrderList.addAll(list);
        }else if(url.equals(Url.GetWaitingToPayOrder)){
            waitingToPayOrderList.clear();
            waitingToPayOrderList.addAll(list);
        }else if(url.equals(Url.GetWaitingToDispatchGoods)){
            waitingToDispatchOrders.clear();
            waitingToDispatchOrders.addAll(list);
        }else if(url.equals(Url.GetWaitingToReceivingGoods)){
            waitingToReceivingOrders.clear();
            waitingToReceivingOrders.addAll(list);
        }else{
            waitingToEvaluateOrders.clear();
            waitingToEvaluateOrders.addAll(list);
        }
        mViewPagerAdapter.notifyDataSetChanged();
        mallOrderViewPager.setCurrentItem(currentItem);
    }*/
 private void fillFragmentsWithData(String url, List<MallOrderBean> list,Map<Integer,Integer> indexMap) {
     if(url.equals(Url.GetAllMallOrder)){
         allMallOrderList.clear();
         allMallOrderList.addAll(list);
         this.indexMap=indexMap;
     }else if(url.equals(Url.GetWaitingToPayOrder)){
         waitingToPayOrderList.clear();
         waitingToPayOrderList.addAll(list);
         this.indexMap=indexMap;
     }else if(url.equals(Url.GetWaitingToDispatchGoods)){
         waitingToDispatchOrders.clear();
         waitingToDispatchOrders.addAll(list);
         this.indexMap=indexMap;
     }else if(url.equals(Url.GetWaitingToReceivingGoods)){
         waitingToReceivingOrders.clear();
         waitingToReceivingOrders.addAll(list);
         this.indexMap=indexMap;
     }else{
         waitingToEvaluateOrders.clear();
         waitingToEvaluateOrders.addAll(list);
         this.indexMap=indexMap;
     }
     mViewPagerAdapter.notifyDataSetChanged();
     mallOrderViewPager.setCurrentItem(currentItem);
 }


    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.mall_order_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.mall_order_tablayout);
        mallOrderViewPager = (ViewPager) findViewById(R.id.mall_order_viewpager);
    }

    private void configViews() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments,"my_mall_order");
        mallOrderViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mallOrderViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mallOrderViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_FIXED);

        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mallOrderViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }






    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
        if(position == 0){
            currentItem = 0;
            requestMallOrderList(Url.GetAllMallOrder);
        }else if(position == 1){
            currentItem = 1;
            requestMallOrderList(Url.GetWaitingToPayOrder);
        }else if(position == 2){
            currentItem = 2;
            requestMallOrderList(Url.GetWaitingToDispatchGoods);
        }else if(position == 3){
            currentItem = 3;
            requestMallOrderList(Url.GetWaitingToReceivingGoods);
        }else{
            currentItem = 4;
            requestMallOrderList(Url.GetWaitingToEvaluateMallOrders);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
