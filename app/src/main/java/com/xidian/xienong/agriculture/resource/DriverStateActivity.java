package com.xidian.xienong.agriculture.resource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.DriverUsedAdapter;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.DriverReservation;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.KCalendar;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.Time;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koumiaojuan on 2017/7/23.
 */

public class DriverStateActivity extends AppCompatActivity{

    private Toolbar toolBar;
    private TextView calendarMonth;
    private KCalendar calendar;
    private RelativeLayout calendar_last_month;
    private RelativeLayout calendar_next_month;
    private RequestQueue requestQueue;
    private SharePreferenceUtil sp;
    private View headerView;
    private ListView listView;
    private DriverUsedAdapter adapter;
    private ArrayList<Driver> list = new ArrayList<Driver>();
    private String[] times = null, machineCategory = null;
    private ArrayList<DriverReservation>  DRS = new ArrayList<DriverReservation>();
    private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    private String driverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_used_state_activity);
        initViews();
        initData();
        initEvents();

    }

    private void initEvents() {
        // TODO Auto-generated method stub
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1,dateFormat.lastIndexOf("-")));

                if (calendar.getCalendarMonth() - month == 1//跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();
                } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();
                } else {
                    calendar.removeAllBgColor();
                    //calendar.setCalendarDayBgColor(dateFormat,R.drawable.calendar_date_focused);
                    date = dateFormat;//最后返回给全局 date
                    getReservationDrivers(date);

                }
            }
        });
        calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {

            @Override
            public void onCalendarDateChanged(int year, int month) {
                // TODO Auto-generated method stub
                calendarMonth.setText(year + "年" + month + "月");
            }
        });

        calendar_last_month.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                calendar.lastMonth();
            }
        });

        calendar_next_month.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                calendar.nextMonth();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                calendar.removeAllMarks();
                Driver driver=list.get(position);
                driverID=driver.getDriver_id();
                requestReservationDriverTime();
            }
        });
    }

    private void initData() {
        // TODO Auto-generated method stub
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarMonth.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestReservationDriverDate();
    }

    private void requestReservationDriverTime(){
        StringRequest stringrequest=new StringRequest(Request.Method.POST, Url.GetReservationTimeByDriverId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        //parseResponse(response);
                        parseDriverTimeResponse(response);
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("kmj0", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                Map<String, String> map = new HashMap<String, String>();
                map.put("driver_id",driverID);
                return map;
            }
        };
        requestQueue.add(stringrequest);
    }

    private void requestReservationDriverDate() {
        // TODO Auto-generated method stub
        StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.GetDriverReservationState,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        Log.i("wyyTest", response);
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                Log.i("kmj", arg0.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                Map<String, String> map = new HashMap<String, String>();
                map.put("worker_id", sp.getUserId());
                return map;
            }

        };
        requestQueue.add(stringrequest);
    }

    protected void parseResponse(String response) {
        // TODO Auto-generated method stub
        try {
            JSONObject jb = new JSONObject(response);
            String reservationTimes = jb.getString("reservation_times");
            times = reservationTimes.split(",");
            for (int i = 0; i < times.length; i++) {

                //calendar.addMark(times[i], R.drawable.calendar_bg_tag_green);
            }

            JSONArray array1 = jb.getJSONArray("reservationTime");
            for(int i = 0; i < array1.length(); i++){
                JSONObject jo = array1.getJSONObject(i);
                DriverReservation dr = new DriverReservation();
                dr.setReservationTime(jo.getString("reservation_time"));
                dr.setDrivers(jo.getString("drivers"));
                DRS.add(dr);
            }

            JSONArray array = jb.getJSONArray("drivers");
            for(int i = 0; i < array.length(); i++){
                JSONObject jo = array.getJSONObject(i);
                Driver driver = new Driver();
                driver.setDriver_id(jo.getString("driver_id"));
                driver.setDriver_name(jo.getString("driver_name"));
                list.add(driver);
            }
            getReservationDrivers(Time.getToday());
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void parseDriverTimeResponse(String response) {
        // TODO Auto-generated method stub
        try {
            JSONObject jb = new JSONObject(response);
            String reservationTimes = jb.getString("reservationTime");
            if(reservationTimes.equals("")){
                ToastCustom.makeToast(DriverStateActivity.this, "该司机当前没有预约");
            }
            times = reservationTimes.split(",");
            for (int i = 0; i < times.length; i++) {
                //在日历上添加标记
                calendar.addMark(times[i], R.drawable.calendar_bg_tag_green);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getReservationDrivers(String date) {
        // TODO Auto-generated method stub
        for(Driver d : list){
            d.setUsed(false);
        }
        for(DriverReservation dr : DRS){
            if(dr.getReservationTime().equals(date)){
                String[] driverIds = dr.getDrivers().split(",");
                for(int i=0; i < driverIds.length; i++){
                    for(Driver d : list){
                        if(d.getDriver_id().equals(driverIds[i])){
                            d.setUsed(true);
                        }
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        toolBar = (Toolbar)findViewById(R.id.driver_statistics_toolbar);
        calendar = (KCalendar)findViewById(R.id.kcalendar);
        calendarMonth = (TextView)findViewById(R.id.calendar_month);
        calendar_last_month = (RelativeLayout)findViewById(R.id.calendar_last_month);
        calendar_next_month = (RelativeLayout)findViewById(R.id.calendar_next_month);
        listView = (ListView)findViewById(R.id.reservation_driver_listview);
        adapter = new DriverUsedAdapter(getApplicationContext(), list);
        listView.setAdapter(adapter);
    }
}
