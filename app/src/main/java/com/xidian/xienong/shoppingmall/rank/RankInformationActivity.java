package com.xidian.xienong.shoppingmall.rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.HistogramView;
import com.xidian.xienong.util.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/7/6.
 */

public class RankInformationActivity extends AppCompatActivity {

    private Intent previousIntent;
    private String classificationId;
    private Toolbar toolBar;
    private TextView no_data;
    private LinearLayout ll_chart;
    private String title;
    private OKHttp httpUrl;
    private List<Commodity>  commodityList = new ArrayList<>();
    private RelativeLayout chart;
    private HistogramView histogramView;
    private String[] names;
    private float[] numbers;
    private TextView  titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_info_activity);
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas() {
        previousIntent = getIntent();
        classificationId = previousIntent.getStringExtra("classification_id");
        title = previousIntent.getStringExtra("title");
        titleName.setText(title);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        httpUrl = OKHttp.getInstance();
        getRecentMonthHotCommodityByVariety();

    }

    private void getRecentMonthHotCommodityByVariety() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("classifction_id", classificationId);
        map.put("begin_date", Time.getAmonthAgoDay());
        map.put("end_date", Time.getToday());

        httpUrl.post(Url.GetRecentMonthHotCommodityByVariety, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetRecentMonthHotCommodityByVariety : " + Url.GetRecentMonthHotCommodityByVariety);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
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
            if (result.equals("SUCCESS")) {
                commodityList.clear();
                JSONArray ranklist = jb.getJSONArray("rankDatas");
                Map<String, Integer> commMap = new HashMap<String, Integer>();
                Map<String, Integer> commMap2 = new LinkedHashMap<String, Integer>();
                for(int i=0; i < ranklist.length(); i++){
                    JSONObject object = ranklist.getJSONObject(i);
                    Commodity c = new Commodity();
                    c.setCommodityId(object.getString("commodity_id"));
                    c.setCommodityName(object.getString("commodity_name"));
                    c.setVariety(object.getString("commodity_variety"));
                    c.setSale_quantity(object.getString("sale_volume"));
                    commodityList.add(c);
                    /**********www************/
                    if (commMap.containsKey(object.getString("commodity_variety"))){
                        commMap.put(object.getString("commodity_variety"), commMap.get(object.getString("commodity_variety"))+object.getInt("sale_volume"));
                    }
                    else {
                        commMap.put(object.getString("commodity_variety"), object.getInt("sale_volume"));
                    }
                    /**********www************/
                }

                /**********www************/
                List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(commMap.entrySet());
                Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
                    //升序排序
                    public int compare(Map.Entry<String,Integer> o1,
                                       Map.Entry<String,Integer> o2) {
                        if(o1.getValue()>=o2.getValue()){
                            return -1;
                        }else
                            return 1;
                    }

                });

                for(Map.Entry<String,Integer> mapping:list){
                    commMap2.put(mapping.getKey(),mapping.getValue());
                    System.out.println(mapping.getKey()+":"+mapping.getValue());
                }
                 if(commMap2.size()>0){
                    names = new String[commMap2.size()];
                    numbers = new float[commMap2.size()];
                    int i=0;
                    for (String key : commMap2.keySet()){
                        names[i] = key;
                        numbers[i] = commMap2.get(key);
                        i++;
                    }
                    histogramView=new HistogramView(RankInformationActivity.this,names,numbers);
                    chart.addView(histogramView);
                    no_data.setVisibility(View.GONE);
                    ll_chart.setVisibility(View.VISIBLE);
                }else{
                    no_data.setVisibility(View.VISIBLE);
                    ll_chart.setVisibility(View.GONE);
                }

                /**********www************/

                Log.i("kmj","-----commo size():" + commodityList.size());
            }else{
                Toast.makeText(RankInformationActivity.this,"获取排行失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initViews() {
        toolBar = (Toolbar)findViewById(R.id.rank_info_toolbar);
        titleName = (TextView)findViewById(R.id.act_describe_gray_bar);
        no_data = (TextView)findViewById(R.id.no_data);
        ll_chart = (LinearLayout)findViewById(R.id.ll_have_data);
        chart = (RelativeLayout)findViewById(R.id.hot_commodity_chart);
    }
}
