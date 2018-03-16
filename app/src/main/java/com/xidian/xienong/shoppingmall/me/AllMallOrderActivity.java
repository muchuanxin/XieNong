package com.xidian.xienong.shoppingmall.me;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;
import okhttp3.Response;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class AllMallOrderActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener{
    private SharePreferenceUtil sp;
    private OKHttp httpUrl;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    public static ViewPager mViewPager;
    private boolean isContinue = true;
    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments = new ArrayList<>();
    private List<MallOrderBean> list = new ArrayList<MallOrderBean>();
    public ViewPagerAdapter mViewPagerAdapter;
    public List<List<MallOrderBean>>  orders = new ArrayList<>();
    public List<MallOrderBean> allOrderList = new ArrayList<>();
    public List<MallOrderBean> waitingPayOrderList = new ArrayList<>();
    public List<MallOrderBean> waitingSendOrderList = new ArrayList<>();
    public List<MallOrderBean> waitingReceiveOrderList = new ArrayList<>();
    public List<MallOrderBean> waitingEvalueOrderList = new ArrayList<>();
    public Map<Integer,Integer> indexMap=new HashMap<Integer, Integer>() ;
    private ProgressDialog progDialog = null;
    private int currentItem = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_mall_order);
        initViews();
        initData();
        configViews();
        initEvents();

    }

    private void configViews() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments,"my_mall_order");
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(AllMallOrderActivity.this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        requestMallOrderList(Url.GetAllMallOrder);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.my_mall_order_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.mall_order_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.mall_order_viewpager);

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

        currentItem=getIntent().getIntExtra("currentItem",0);
        mTitles = getResources().getStringArray(R.array.shop_order_titles);
        orders.add(allOrderList);
        orders.add(waitingPayOrderList);
        orders.add(waitingSendOrderList);
        orders.add(waitingReceiveOrderList);
        orders.add(waitingEvalueOrderList);
        for(int i=0; i < mTitles.length;i++){

            Bundle mBundle = new Bundle();
            mBundle.putSerializable("data", (Serializable)orders.get(i));
            mBundle.putSerializable("indexMap",(Serializable)indexMap);
            MallAllOrderFragment mFragment= new MallAllOrderFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(mFragment);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
        Timer timer = new Timer();
        showProgressDialog();
        if(position == 0){
            currentItem = 0;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            requestMallOrderList(Url.GetAllMallOrder);
                        }
                    });
                }
            }, 400);

        }else if(position == 1){
            currentItem = 1;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            requestMallOrderList(Url.GetWaitingToPayOrder);
                        }
                    });
                }
            }, 400);

        }else if(position == 2){
            currentItem = 2;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            requestMallOrderList(Url.GetWaitingToDispatchGoods);
                        }
                    });
                }
            }, 400);
           // requestMallOrderList(Url.GetWaitingToDispatchGoods);
        }else if(position == 3){
            currentItem = 3;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            requestMallOrderList(Url.GetWaitingToReceivingGoods);
                        }
                    });
                }
            }, 400);
           // requestMallOrderList(Url.GetWaitingToReceivingGoods);
        }else{
            currentItem = 4;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            requestMallOrderList(Url.GetWaitingToEvaluateMallOrders);//待评价
                        }
                    });
                }
            }, 400);
           // requestMallOrderList(Url.GetHaveSignedGoods);//已签收？待评价?
        }

    }

    public void requestMallOrderList(final String url) {
        Map<String, String> map = new HashMap<String, String>();
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
                    list.add( mallOrderTop);
                    int topIndex = list.indexOf(mallOrderTop);

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
                    for (int k=0;k<middleList.size();k++){
                        if(!middleList.get(k).isEvalauted()){
                            allIsEvaluated=false;
                        }

                    }
                    MallOrderBottom mallOrderBottom= new MallOrderBottom(2,transportation_expense,"共"+commArray.length(),total_money,order_status,allIsEvaluated);
                    list.add(mallOrderBottom);

                    int bottomIndex=list.indexOf(mallOrderBottom);
                    indexMap.put(topIndex,bottomIndex);

                  //  list.add(mallOrder);
                }
                fillFragmentsWithData(list,url,indexMap);
                dissmissProgressDialog();
            }else {
                Toast.makeText(AllMallOrderActivity.this, "获取订单失败，请重试", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    private void fillFragmentsWithData(List<MallOrderBean> list, String url, Map<Integer,Integer> indexMap) {

        if(url.equals(Url.GetAllMallOrder)) {
            allOrderList.clear();
            allOrderList.addAll(list);
            this.indexMap=indexMap;
        }else if(url.equals(Url.GetWaitingToPayOrder)){
            waitingPayOrderList.clear();
            waitingPayOrderList.addAll(list);
            this.indexMap=indexMap;
        }else if(url.equals(Url.GetWaitingToDispatchGoods)){
            waitingSendOrderList.clear();
            waitingSendOrderList.addAll(list);
            this.indexMap=indexMap;
        }else if(url.equals(Url.GetWaitingToReceivingGoods)){
            waitingReceiveOrderList.clear();
            waitingReceiveOrderList.addAll(list);
            this.indexMap=indexMap;
        }else{
            waitingEvalueOrderList.clear();
            waitingEvalueOrderList.addAll(list);
            this.indexMap=indexMap;
        }
        mViewPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
       // unregisterReceiver(receiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onPageSelected(currentItem);
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
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

    private Runnable LOAD_DATA = new Runnable() {
        @Override
        public void run() {
            //在这里数据内容加载到Fragment上
            orders.add(allOrderList);
            orders.add(waitingPayOrderList);
            orders.add(waitingSendOrderList);
            orders.add(waitingReceiveOrderList);
            orders.add(waitingEvalueOrderList);
            for(int i=0; i < mTitles.length;i++){

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("data", (Serializable)orders.get(i));
                mBundle.putSerializable("indexMap",(Serializable)indexMap);
                MallAllOrderFragment mFragment= new MallAllOrderFragment();
                mFragment.setArguments(mBundle);
                mFragments.add(mFragment);
            }
            dissmissProgressDialog();
        }
    };

}
