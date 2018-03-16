package com.xidian.xienong.agriculture.resource;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.DriverAdapter;
import com.xidian.xienong.adapter.MachineAdapter;
import com.xidian.xienong.model.Driver;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.MachineIdentify;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.model.Worker;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.RecyclerDecoration;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.SnackbarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by koumiaojuan on 2017/6/12.
 */

public class InputResourceActivity extends AppCompatActivity implements OnClickListener,MachineAdapter.OnItemClickListener{

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mcollapsingbarLayout;
    private LinearLayout headLayout;
    private ImageView photo;
    private TextView managerName,machineNumber,driverNunber;
    private SharePreferenceUtil sp;
    private OKHttp httpUrl;
    private DriverAdapter adapter;
    private List<Driver> drivers = new ArrayList<Driver>();
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout driverLayout;
    private LayoutInflater mInflater;
    private RecyclerView recyclerView;
    private MachineAdapter machineAdapter;
    private List<Machine> list = new ArrayList<Machine>();
    private Worker worker;
    private RecyclerView.LayoutManager mLayoutManager;
    private View machineHeaderView;
    private FloatingActionButton publishMachineBtn;
    private Button addNewDriver;
    private ImageView imageView;
    private boolean  isUpdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_resource_activity);
        initViews();
        initData();
        initEvents();

    }

    private void initEvents() {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        publishMachineBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtil.show(InputResourceActivity.this,v, getString(R.string.confirm_publish_machine), 1);
                Intent intent = new Intent(InputResourceActivity.this, NewMachineActivity.class);
                startActivityForResult(intent,600);
            }
        });
        addNewDriver.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(InputResourceActivity.this,AddNewDriverActivity.class);
                startActivityForResult(intent, 500);
            }
        });

    }


    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我的所有农机");
        mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        loadBlurAndSetStatusBar();
        httpUrl = OKHttp.getInstance();
        sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
        mLayoutManager =  new LinearLayoutManager(InputResourceActivity.this, LinearLayoutManager.VERTICAL, false);
        /*mLayoutManager.setAutoMeasureEnabled(true);*/

        if(!sp.getHeadPhoto().equals("")){
            Glide.with(getApplicationContext()).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.author).into(photo);
        }
        mcollapsingbarLayout.setTitle(sp.getWorkerName());
        managerName.setText(sp.getUserName());
        machineAdapter = new MachineAdapter(InputResourceActivity.this,list);

        machineAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(machineAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
/*        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);*/
//        mRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RecyclerDecoration(
               InputResourceActivity.this, LinearLayoutManager.VERTICAL, R.drawable.divider));
        recyclerView.setNestedScrollingEnabled(false);

        getMachineNumberAndDriverNumber();
        getAllRegisteredMachine();
    }

    private void getAllRegisteredMachine() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("worker_id", sp.getUserId());
        httpUrl.post(Url.GetAllRegisteredMachines,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                Log.i("kmj","url is : " + Url.GetAllRegisteredMachines);
            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseMachineRequestResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseMachineRequestResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                worker = new Worker();
                JSONObject object = jb.getJSONObject("worker");
                worker.setWorkerId(object.getString("worker_id"));
                worker.setWorkerName(object.getString("worker_name"));
                worker.setTelephone(object.getString("telephone"));
                worker.setSimpleAddress(object.getString("simple_address"));
                worker.setAddress(object.getString("address"));
                worker.setHeadPhoto(object.getString("head_photo"));
                list.clear();
                JSONArray machinelist = jb.getJSONArray("machineList");
                for(int i=0; i < machinelist.length(); i++){
                    JSONObject jom = machinelist.getJSONObject(i);

                    Machine machine = new Machine();
                    machine.setBatch_number(jom.getString("batch_number"));
                    machine.setCategory_id(jom.getString("category_id"));
                    machine.setCategory_name(jom.getString("category_name"));
                    machine.setTrademark_id(jom.getString("trademark_id"));
                    machine.setTrademark_name(jom.getString("trademark"));
                    machine.setBatch_number(jom.getString("batch_number"));
                    machine.setMachineNumber(jom.getInt("number"));
                    machine.setWork_price(jom.getInt("work_price"));
                    machine.setMachineDescriptionOrMessage(jom.getString("machine_description_or_message"));
                    machine.setUploadTime(jom.getString("upload_time"));
                    machine.setIsChecked(jom.getString("isChecked"));
                    JSONArray machineIdentifyArray=jom.getJSONArray("machine_identify");
                    ArrayList<MachineIdentify> machineIdentities = new ArrayList<MachineIdentify>();
                    machineIdentities.clear();
                    for(int j = 0; j < machineIdentifyArray.length(); j++){
                        JSONObject jomif =  machineIdentifyArray.getJSONObject(j);
                        MachineIdentify machineIdentify = new MachineIdentify();
                        machineIdentify.setMachine_id(jomif.getString("machine_id"));
                        machineIdentify.setIdentify(jomif.getString("identify"));
                        machineIdentify.setIsDeleted(jomif.getString("isDeleted"));
                        machineIdentities.add(machineIdentify);
                    }
                    machine.setMachineIdentify(machineIdentities);

                    JSONArray machineImageArray = jom.getJSONArray("machine_image");
                    List<MachineImage> machineImages = new ArrayList<MachineImage>();
                    machineImages.clear();
                    for(int j = 0; j < machineImageArray.length(); j++){
                        JSONObject jomi =  machineImageArray.getJSONObject(j);
                        MachineImage machineImage = new MachineImage();
                        machineImage.setUrl(jomi.getString("image_url"));
                        machineImages.add(machineImage);
                    }
                    machine.setMachineImage(machineImages);
                    list.add(machine);
                }
                Collections.sort(list);
                machineAdapter.notifyDataSetChanged();
                if(isUpdate){
                    isUpdate = false;
                    getMachineNumberAndDriverNumber();
                }
            }else{
                Toast.makeText(getApplicationContext(), "获取农机失败，请重试",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getAllDriver() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("worker_id", sp.getUserId());
        httpUrl.post(Url.GetAddedDriver,map,new BaseCallback<String>(){
            @Override
            public void onRequestBefore() {
                   Log.i("kmj","url is : " + Url.GetAddedDriver);
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
        // TODO Auto-generated method stub
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            String message = jb.getString("message");
            if (result.equals("SUCCESS")) {
                drivers.clear();
                JSONArray driverlist = jb.getJSONArray("drivers");
                Log.i("fmy","得到了driverlist"+driverlist.length());
                for(int i=0; i < driverlist.length(); i++){
                    JSONObject object = driverlist.getJSONObject(i);
                    Driver driver = new Driver();
                    driver.setDriver_id(object.getString("driver_id"));
                    driver.setDriver_sex(object.getString("sex"));
                    driver.setDriver_name(object.getString("driver_name"));
                    driver.setDriver_telephone(object.getString("driver_telephone"));
                    driver.setDriver_identification(object.getString("driver_identification"));
                    drivers.add(driver);
                }
                Collections.sort(drivers);
                driverLayout.removeAllViews();
                adapter = new DriverAdapter(getApplicationContext(), drivers);
                for(int i=0;i < drivers.size();i++){
                    View view = mInflater.inflate(R.layout.horizontal_driver_list_item,
                            driverLayout, false);
                    TextView name = (TextView)view.findViewById(R.id.list_driver_name);
                    TextView tele = (TextView)view.findViewById(R.id.list_driver_tele);
                    Button btntele = (Button)view.findViewById(R.id.btn_driver_tele);
                    ImageView photo = (ImageView)view.findViewById(R.id.driver_image);
                    name.setText(drivers.get(i).getDriver_name());
                    tele.setText(drivers.get(i).getDriver_telephone());
                    btntele.setId(i);
                    btntele.setTag("button");
                    view.setTag("view");
                    view.setId(i);
                    btntele.setOnClickListener(this);//改动
                    view.setOnClickListener(this);
                    driverLayout.addView(view);
                }
                if(isUpdate){
                    isUpdate = false;
                    getMachineNumberAndDriverNumber();
                }
            }else{
                Toast.makeText(getApplicationContext(), "获取司机失败，请重试",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getMachineNumberAndDriverNumber() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("worker_id", sp.getUserId());
        httpUrl.post(Url.GetMyMachineNumberAndDriverNumber,map,new BaseCallback<String>(){
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
                    if (result.equals("SUCCESS")) {
                        machineNumber.setText(jb.getString("machine_number")+"台");
                        driverNunber.setText(jb.getString("driver_number")+"人");
                        getAllDriver();
                    }else{
                        Toast.makeText(InputResourceActivity.this, "异常情况！",Toast.LENGTH_SHORT).show();
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

    /**
     * 设置毛玻璃效果和沉浸状态栏
     */
    private void loadBlurAndSetStatusBar() {
        //目的是让状态栏半透明
         StatusBarUtil.setTranslucent(InputResourceActivity.this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        //目的是让状态栏全透明
//        StatusBarUtil.setTransparent(InputResourceActivity.this);

        Glide.with(this).load(R.drawable.resource_background).bitmapTransform(new BlurTransformation(this, 100))
                .into(new SimpleTarget<GlideDrawable>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> glideAnimation) {
                        headLayout.setBackground(resource);
                        mcollapsingbarLayout.setBackground(resource);
                    }
                });
//
//        Glide.with(this).load(R.drawable.resource_background).bitmapTransform(new BlurTransformation(this, 100))
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
//                            GlideDrawable> glideAnimation) {
//                        mcollapsingbarLayout.setContentScrim(resource);
//                    }
//                });
    }

    private void initViews() {
        mToolbar = (Toolbar)findViewById(R.id.input_resource_toolbar);
        mcollapsingbarLayout = (CollapsingToolbarLayout) findViewById(R.id.input_resource_collapsingbar_layout);
        headLayout = (LinearLayout)findViewById(R.id.head_layout);
        photo = (ImageView)findViewById(R.id.manager_headphoto);
        managerName = (TextView)findViewById(R.id.manager_name);
        machineNumber = (TextView)findViewById(R.id.machine_number);
        driverNunber = (TextView)findViewById(R.id.driver_number);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.driver_scrollView);
        driverLayout = (LinearLayout)findViewById(R.id.id_driver_layout);
        recyclerView = (RecyclerView)findViewById(R.id.my_machie_recyclerview);
        publishMachineBtn = (FloatingActionButton)findViewById(R.id.id_publish_machine_button);
        addNewDriver = (Button)findViewById(R.id.btn_add_driver);

    }


    @Override
    public void onClick(View v) {

        final int position = v.getId();
        Log.i("kmj","id is :" + position);
        if(v.getTag().equals("button")){
            Log.i("kmj","text is----- :");
            new SweetAlertDialog(InputResourceActivity.this, SweetAlertDialog.TIP_TYPE)
                    .setTitleText("确认手机号码")
                    .setContentText("我们将拨打司机" + drivers.get(position).getDriver_name() + "的手机号：" + drivers.get(position).getDriver_telephone())
                    .setCancelText("不，谢谢")
                    .setConfirmText("好，拨打")
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + drivers.get(position).getDriver_telephone()));
                            if (ActivityCompat.checkSelfPermission(InputResourceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                ActivityCompat.requestPermissions(InputResourceActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                                startActivity(intent);
                            }
                            else {
                                startActivity(intent);
                            }

                        }
                    })
                    .show();
        }else{
            Intent intent = new Intent(InputResourceActivity.this,ModifyDriverActivity.class);
            intent.putExtra("driver", drivers.get(position));
            startActivityForResult(intent, 500);
        }

    }


    @Override
    public void onItemClick(View view, int position) {

        {
            // TODO Auto-generated method stub

            Intent intent = new Intent(InputResourceActivity.this, MachineDetailActivity.class);
            intent.putExtra("machine", list.get(position));
            intent.putExtra("address", worker.getAddress());
            intent.putExtra("simple_address", worker.getSimpleAddress());
            intent.putExtra("machineIdentify", (Serializable) list.get(position).getMachineIdentify());
            intent.putExtra("machineImage", (Serializable) list.get(position).getMachineImage());
            intent.putExtra("position", position);
            startActivityForResult(intent, 8);


        }
    }


    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        isUpdate = true;
        if (requestCode == 500 ) {
            if(resultCode == RESULT_OK){
                driverLayout.removeAllViews();
                getMachineNumberAndDriverNumber();
                getAllDriver();
            }
        }
        if(requestCode == 600||requestCode == 8){
            if(resultCode == RESULT_OK){
                getMachineNumberAndDriverNumber();
               getAllRegisteredMachine();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
