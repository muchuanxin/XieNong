package com.xidian.xienong.agriculture.announcement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.CroplandListAdapter;
import com.xidian.xienong.adapter.MachineCategoryListAdapter;
import com.xidian.xienong.model.CroplandType;
import com.xidian.xienong.model.MachineCategory;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.Time;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/6.
 */

public class NewAnnounceActivity extends AppCompatActivity{

    private SharePreferenceUtil sp;
    private ImageView locate;
    private TextView crop_address;
    private TextView detail_address;
    private TextView machine_type;
    private TextView reserve_time;
    private TextView cropland_type;
    private EditText cropland_number;
    private Button preview;
    private Button publish;
    private  OKHttp httpUrl;
    private String category_id="";
    private String cropland_id="";
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private String month,day;
    private Calendar calendar = Calendar.getInstance();
    private View chooseCropLayout,chooseMachineLayout;
    private ListView chooseCropListView,chooseMachineListView;
    private CroplandListAdapter croplandAdapter;
    private MachineCategoryListAdapter machineCategoryAdapter;
    private AlertDialog  croplandDialog,machineDialog;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_announce_activity);
        initViews();
        initData();
        initEvents();
    }
    private void initEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        locate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NewAnnounceActivity.this,MapChoosePointActivity.class);
                intent.putExtra("activity", "new_announce_activity");
                startActivityForResult(intent, 1);
            }
        });
        machine_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(Constants.machineCategory.size() == 0){
                    getMachineCategory();
                }else{
                    showMachineCategory();
                }
            }
        });

        cropland_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(Constants.croplandType.size() == 0){
                    getCroplandType();
                }else{
                    showCroplandType();
                }
            }
        });

        chooseCropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cropland_id = Constants.croplandType.get(position).getCroplandId();
                cropland_type.setText( Constants.croplandType.get(position).getCroplandType());
                croplandDialog.dismiss();
            }
        });

        chooseMachineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category_id = Constants.machineCategory.get(position).getCategory_id();
                machine_type.setText(Constants.machineCategory.get(position).getCategory_name());
                machineDialog.dismiss();
            }
        });

        reserve_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new DatePickerDialog(NewAnnounceActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        currentYear = year;
                        currentMonth = monthOfYear + 1;
                        currentDay = dayOfMonth;
                        if(currentMonth >0 && currentMonth <10){
                            month="0"+currentMonth;
                        }else{
                            month=currentMonth+"";
                        }
                        if(currentDay >0 && currentDay <10){
                            day="0"+currentDay;
                        }else{
                            day=currentDay+"";
                        }
                        Animation animation = AnimationUtils.loadAnimation(
                                NewAnnounceActivity.this, R.anim.text_showin);
                        reserve_time.setText(currentYear + "-" + month + "-"
                                + day);
                        reserve_time.startAnimation(animation);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkInformationIsValid()){
                    SweetAlertDialog sweet =  new SweetAlertDialog(NewAnnounceActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                    sweet.setTitleText("您发布的信息如下");
                    sweet.setContentText("您选择的地址是"+Constants.location.get(0)+"，其位于"
                            +Constants.location.get(1)+"，农机类型为"+machine_type.getText()+"，预约时间为"
                            +reserve_time.getText()+"，农田类型为"+cropland_type.getText()+"，农田亩数为"+cropland_number.getText().toString());
                    sweet.setCustomImage(R.drawable.custom_img);
                    sweet.setCancelable(false);
                    sweet.show();
                }
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkInformationIsValid()){
                    Log.i("kmj","jajhajskd");
                    new SweetAlertDialog(NewAnnounceActivity.this, SweetAlertDialog.TIP_TYPE)
                            .setTitleText("确认发布信息")
                            .setContentText("请先预览您的发布信息")
                            .setCancelText("不，谢谢")
                            .setConfirmText("好，发布")
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
                                    publishAnnouncement();
                                }
                            })
                            .show();
                }
            }
        });

    }

    private void publishAnnouncement() {
        final Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.GetAllCroplandType, map, new BaseCallback<String>() {

            @Override
            public void onRequestBefore() {
                map.put("farmer_id","6219");
                map.put("crop_address", crop_address.getText().toString());
                map.put("longtitude", Constants.location.get(3));
                map.put("lantitude", Constants.location.get(2));
                map.put("machine_category_id",category_id);
                map.put("cropland_type_id",cropland_id);
                map.put("reservation_time", reserve_time.getText().toString());
                map.put("cropland_number",cropland_number.getText().toString());
            }

            @Override
            public void onFailure(Request request, Exception e) {}

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("SUCCESS")) {
                        ToastCustom.makeToast(getApplicationContext(), "发布成功");
                        setResult(Activity.RESULT_OK);
                        finish();
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
                Log.i("kmj", "onError : " + e.toString());
            }
        });

    }

    private boolean checkInformationIsValid() {
        Log.i("kmj","checkInformationIsValid");
        if(crop_address.getText().equals("")){
            ToastCustom.makeToast(NewAnnounceActivity.this, "请选择农田地址");
            return false;
        }
        if(machine_type.getText().equals("")){
            ToastCustom.makeToast(NewAnnounceActivity.this, "请选择农机种类");
            return false;
        }
        if(reserve_time.getText().equals("")){
            ToastCustom.makeToast(NewAnnounceActivity.this, "请选择预约时间");
            return false;
        }
        if(cropland_type.getText().equals("")){
            ToastCustom.makeToast(NewAnnounceActivity.this, "请选择农田类型");
            return false;
        }

        if(cropland_number.getText().toString().equals("")){
            ToastCustom.makeToast(NewAnnounceActivity.this, "请填写农田亩数");
            return false;
        }
        if(Time.compare_with_now_date(reserve_time.getText().toString()) == 1){
            ToastCustom.makeToast(NewAnnounceActivity.this, "预约时间应大于或等于当前时间");
            return false;
        }
        if(!isNumber(cropland_number.getText().toString())){
            ToastCustom.makeToast(NewAnnounceActivity.this, "亩数应为数字");
            return false;
        }
        return true;
    }

    public static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    private void getMachineCategory() {
        Constants.machineCategory.clear();
        Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.GetAllMachineCategory, map, new BaseCallback<String>() {

            @Override
            public void onRequestBefore() {
                Log.i("kmj","Url.GetAllMachineCategory: " + Url.GetAllMachineCategory);
            }

            @Override
            public void onFailure(Request request, Exception e) {}

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("SUCCESS")) {
                        JSONArray list = jb.getJSONArray("machineCategory");
                        for(int i=0; i < list.length(); i++){
                            JSONObject object = list.getJSONObject(i);
                            MachineCategory mc = new MachineCategory();
                            mc.setCategory_id(object.getString("category_id"));
                            mc.setCategory_name(object.getString("category_name"));
                            Constants.machineCategory.add(mc);
                        }
                        machineCategoryAdapter.notifyDataSetChanged();
                        machineDialog.show();
                        machineDialog.getWindow().setContentView(chooseMachineLayout);
                    }else{
                        Toast.makeText(NewAnnounceActivity.this, message,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "onError : " + e.toString());
            }
        });
    }


    private void getCroplandType() {
        Constants.croplandType.clear();
        Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.GetAllCroplandType, map, new BaseCallback<String>() {

            @Override
            public void onRequestBefore() {
                Log.i("kmj","Url.GetAllMachineCategory: " + Url.GetAllCroplandType);
            }

            @Override
            public void onFailure(Request request, Exception e) {}

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                try {
                    JSONObject jb = new JSONObject(resultResponse);
                    String result = jb.getString("reCode");
                    String message = jb.getString("message");
                    if (result.equals("SUCCESS")) {
                        JSONArray list = jb.getJSONArray("croplandType");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject object = list.getJSONObject(i);
                            CroplandType ct = new CroplandType();
                            ct.setCroplandId(object.getString("cropland_id"));
                            ct.setCroplandType(object.getString("cropland_type"));
                            Constants.croplandType.add(ct);
                        }
                        croplandAdapter.notifyDataSetChanged();
                        croplandDialog.show();
                        croplandDialog.getWindow().setContentView(chooseCropLayout);

                    } else {
                        Toast.makeText(NewAnnounceActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "onError : " + e.toString());
            }
        });
    }

    private void showMachineCategory() {
        machineCategoryAdapter.notifyDataSetChanged();
        machineDialog.show();
    }

    private void showCroplandType() {
        croplandAdapter.notifyDataSetChanged();
        croplandDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ) {
            if (resultCode == Activity.RESULT_OK ) {
                crop_address.setText(Constants.location.get(0));
                detail_address.setText(Constants.location.get(1));
            }

        }
    }

    private void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        httpUrl = OKHttp.getInstance();
        croplandDialog = new AlertDialog.Builder(NewAnnounceActivity.this).create();
        machineDialog = new AlertDialog.Builder(NewAnnounceActivity.this).create();
        croplandAdapter = new CroplandListAdapter(NewAnnounceActivity.this,Constants.croplandType);
        chooseCropListView.setAdapter(croplandAdapter);
        machineCategoryAdapter = new MachineCategoryListAdapter(NewAnnounceActivity.this,Constants.machineCategory);
        chooseMachineListView.setAdapter(machineCategoryAdapter);
    }


    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.announce_toolbar);
        locate = (ImageView) findViewById(R.id.address_located);
        crop_address = (TextView)findViewById(R.id.tv_crop_address_1);
        detail_address = (TextView)findViewById(R.id.tv_crop_address_2);
        machine_type = (TextView)findViewById(R.id.tv_machine_type_1);
        reserve_time = (TextView)findViewById(R.id.tv_reserve_time_1);
        cropland_type = (TextView)findViewById(R.id.tv_crop_type_1);
        cropland_number = (EditText)findViewById(R.id.cropland_number);
        preview = (Button)findViewById(R.id.btn_preview);
        publish = (Button)findViewById(R.id.btn_publish_announce);
        chooseCropLayout = getLayoutInflater().inflate(R.layout.crop_list_alert_dialog,null);
        chooseMachineLayout = getLayoutInflater().inflate(R.layout.machine_list_alert_dialog,null);
        chooseCropListView = (ListView) chooseCropLayout.findViewById(R.id.choose_crop_list);
        chooseMachineListView = (ListView) chooseMachineLayout.findViewById(R.id.choose_machine_list);
    }

}
