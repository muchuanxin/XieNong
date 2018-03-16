package com.xidian.xienong.agriculture.resource;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.MachineDetailImageAdapter;
import com.xidian.xienong.adapter.MachineNameAdapter;
import com.xidian.xienong.agriculture.announcement.MapChoosePointActivity;
import com.xidian.xienong.model.ImageVo;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.model.MachineIdentify;
import com.xidian.xienong.model.MachineImage;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.photo.AlbumActivity;
import com.xidian.xienong.photo.Bimp;
import com.xidian.xienong.photo.ImageItem;
import com.xidian.xienong.photo.util.FileUtils;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.ImageSettingUtil;
import com.xidian.xienong.util.NetWorkUtil;
import com.xidian.xienong.util.NoScrollListView;
import com.xidian.xienong.util.PostParameter;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MachineDetailActivity extends AppCompatActivity implements ImageSettingUtil.ImageUploadDelegate {

	private TextView machineState;
	private TextView machineAddress;
	private TextView machineDetailAddress;
	private TextView machineType;
	private TextView machineNumber;
	private TextView machinePrice;
	private RelativeLayout rl_machineMessage;
	private EditText machineMessage;
	private GridView machinePicture;
	private Machine machine;
	private String simpleAddress,address;
	private MachineDetailImageAdapter imageAdapter;
	private MachineNameAdapter nameAdapter;
	private Button modify;
	private View line;
	private RelativeLayout rl_detail_detail_address;
	private RelativeLayout rl_camera;
	private ImageView camera,iv_locate;
	private List<MachineImage> exist_machineImages = new ArrayList<MachineImage>();
	private List<MachineImage> machineImages = new ArrayList<MachineImage>();
	private List<MachineImage> new_machineImages = new ArrayList<MachineImage>();
	private List<MachineImage> InitalachineImages = new ArrayList<MachineImage>();
	private String imageNames ="";
	private boolean isShowDelete = false;
	private RequestQueue requestQueue;
	private SharePreferenceUtil sp;
	private String uploadTime="";
	private double longtitude,lantitude;
	private List<MachineIdentify> list = new ArrayList<MachineIdentify>();
	private NetWorkUtil netWorkUtil;
	private int position;
	private NoScrollListView machineListview;
	private TextView machineTrademark;
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.machine_detail_activity);
		initViews();
		initData();
		getMachinesList();
		initEvents();
		hideViews();
	}

	private void hideViews() {
		// TODO Auto-generated method stub
		line.setVisibility(View.GONE);
		rl_detail_detail_address.setVisibility(View.GONE);
		rl_camera.setVisibility(View.GONE);
		iv_locate.setVisibility(View.GONE);

	}

	private void initEvents() {
		// TODO Auto-generated method stub
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		machineMessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.toString().equals("")){
					machineMessage.setHint("农机简介或留言");
				}
			}
		});

		modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((Button)v).getText().equals("修改")){
					showViews();
					canableEditViews();
					modify.setText("保存");
				}else{
					if(checkInfoIsValid()){
						new SweetAlertDialog(MachineDetailActivity.this, SweetAlertDialog.TIP_TYPE)
								.setTitleText("确认保存信息")
								.setContentText("确定保存您的农机信息")
								.setCancelText("不，谢谢")
								.setConfirmText("好，保存")
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
										exist_machineImages.clear();
										for (int i = 0; i < machineImages.size(); i++) {
											for(int j=0; j < InitalachineImages.size();j++){
												if(machineImages.get(i).getUrl().equals(InitalachineImages.get(j).getUrl())){
													Log.e("www","--------"+machineImages.get(i).getUrl());
													exist_machineImages.add(machineImages.get(i));
													break;
												}
											}
										}
										for(MachineImage mi : exist_machineImages){
											machineImages.remove(mi);
											if(exist_machineImages.indexOf(mi) != exist_machineImages.size()-1){
												imageNames = imageNames + mi.getUrl()+",";
											}else{
												imageNames = imageNames + mi.getUrl();
											}
											Log.i("www","--imageNames=---" + imageNames);
										}
										Log.i("kmj","modify--imageNames=---" + imageNames);
										Log.i("kmj","--machineImages=---" + machineImages.size());
										saveMachine();
									}
								})
								.show();
					}
				}
			}
		});

		iv_locate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MachineDetailActivity.this,MapChoosePointActivity.class);
				intent.putExtra("activity", "new_machine_activity");
				startActivityForResult(intent, 9);
			}
		});

		rl_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bimp.tempSelectBitmap.clear();
				for(int i=0; i < machineImages.size();i++){
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(machineImages.get(i).getUrl());
					Bimp.tempSelectBitmap.add(takePhoto);
				}
				showChoice();
			}
		});


		machinePicture.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (isShowDelete) {
					isShowDelete = false;
				} else {
					isShowDelete = true;
					imageAdapter.setIsShowDelete(isShowDelete);
					machinePicture.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
							delete(position);
							imageAdapter.setIsShowDelete(isShowDelete);
							imageAdapter.setList(machineImages);
							imageAdapter.notifyDataSetChanged();
						}

					});
				}
				imageAdapter.setIsShowDelete(isShowDelete);

				return true;
			}
		});

		machineListview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   final int arg2, long arg3) {
				// TODO Auto-generated method stub
				final int position = arg2;
				if(modify.getText().equals("保存")){
					new SweetAlertDialog(MachineDetailActivity.this, SweetAlertDialog.TIP_TYPE)
							.setTitleText("删除农机")
							.setContentText("您是否删除该农机？")
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
									deleteTheMachine(list.get(arg2));

								}
							})
							.show();
				}

				return true;
			}
		});
	}

	private void deleteTheMachine(MachineIdentify mi) {
		// TODO Auto-generated method stub
		final MachineIdentify machineIdentify = mi;
		StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.DeleteSomeMachine,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						try {
							JSONObject jb = new JSONObject(response);
							String result = jb.getString("reCode");
							String message = jb.getString("message");
							if (result.equals("SUCCESS")) {

								nameAdapter.notifyDataSetChanged();
								getMachinesList();
								int number=list.size()-1;
								machineNumber.setText(""+number+"台");
								ToastCustom.makeToast(getApplicationContext(), "删除成功");
								setResult(RESULT_OK);
								finish();
							}else{

								ToastCustom.makeToast(getApplicationContext(), message);
							}


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				// TODO Auto-generated method stub
				volleyError.printStackTrace();
			}

		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String, String>();
				map.put("worker_id", sp.getUserId());
				//map.put("machine_id", new_machineIdentify.get(position).getMachine_id());
				map.put("machine_id", machineIdentify.getMachine_id());
				map.put("category_id",machine.getCategory_id());
				map.put("trademark_id", machine.getTrademark_id());
				return map;
			}
		};
		requestQueue.add(stringrequest);
	}

	private void getMachinesList() {
		// TODO Auto-generated method stub
		if (!netWorkUtil.isNetworkAvailable()) {
			netWorkUtil.setNetwork();
		} else{
			StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.GetMyMachines,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							Log.i("kmj","response:"+response);
							parseResponse(response);
						}
					}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					// TODO Auto-generated method stub
					volleyError.printStackTrace();
				}

			}) {

				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					// TODO Auto-generated method stub
					Map<String, String> map = new HashMap<String, String>();
					map.put("worker_id", sp.getUserId());
					map.put("batch_number", machine.getBatch_number());
					map.put("category_name", machine.getCategory_name());
					map.put("trademark", machine.getTrademark_name());
					return map;
				}
			};
			requestQueue.add(stringrequest);
		}
	}

	protected void parseResponse(String response) {
		// TODO Auto-generated method stub

		try {
			JSONObject jb = new JSONObject(response);
			String result = jb.getString("reCode");
			if (result.equals("SUCCESS")) {
				list.clear();
				JSONArray machineIdentifyArray = jb.getJSONArray("machine_identify");
				for(int i=0; i < machineIdentifyArray.length(); i++){
					JSONObject jom = machineIdentifyArray.getJSONObject(i);
					MachineIdentify machineIdentify = new MachineIdentify();
					machineIdentify.setMachine_id(jom.getString("machine_id"));
					machineIdentify.setIdentify(jom.getString("identify"));
					list.add(machineIdentify);
				}
				nameAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void saveMachine() {
		// TODO Auto-generated method stub

		StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.SaveModifiedMachine,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.i("kmj","response---"+response);
						try {
							JSONObject jb = new JSONObject(response);
							String result = jb.getString("reCode");
							String message = jb.getString("message");
							if (result.equals("SUCCESS")) {
								if(machineMessage.getText().toString().equals("") && machineImages.size()==0){
									ToastCustom.makeToast(getApplicationContext(), "保存成功");
									setResult(RESULT_OK);

									finish();
								}else{
									if(machineImages.size()!=0){
										uploadTime = jb.getString("upload_time");
										uploadPic();
									}else{
										ToastCustom.makeToast(getApplicationContext(), "您的农机处于审核状态中...");
										setResult(RESULT_OK);

										finish();
									}
								}

							}else{
								Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
								Log.i("fmy","aasdasd"+message);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				// TODO Auto-generated method stub
				volleyError.printStackTrace();
			}

		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				Map<String, String> map = new HashMap<String, String>();
				map.put("worker_id", sp.getUserId());
				if(machineAddress.getText().toString().equals(simpleAddress)){
					map.put("longtitude", String.valueOf(longtitude));
					map.put("lantitude",  String.valueOf(lantitude));
				}else{
					map.put("longtitude", Constants.location.get(3));
					map.put("lantitude", Constants.location.get(2));
				}
				map.put("simple_address", machineAddress.getText().toString());
				map.put("address",machineDetailAddress.getText().equals("")? machineAddress.getText().toString():machineDetailAddress.getText().toString());
				map.put("category_id",machine.getCategory_id());
				map.put("trademark_id", machine.getTrademark_id());
				map.put("batch_number", machine.getBatch_number());
				map.put("work_price",machinePrice.getText().toString());
				map.put("machine_description_or_message",machineMessage.getText().toString());
				map.put("imageSize",String.valueOf(machineImages.size()));
				map.put("last_upload_time",machine.getUploadTime());
				map.put("exist_machine_images",imageNames);
				return map;
			}
		};
		requestQueue.add(stringrequest);

	}

	protected void uploadPic() {
		// TODO Auto-generated method stub
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if(machineImages.size() > 9){
					Looper.prepare();
					ToastCustom.makeToast(getApplicationContext(), "您最多只能选择9张图片");
					Looper.loop();
				}else{
					for (int i = 0; i < machineImages.size(); i++) {
						ImageVo imageVo = new ImageVo();
						imageVo.setImagePath(machineImages.get(i).getImagePath());
						String str = sp.getWorkerId().toString() + "_"
								+ System.currentTimeMillis() + "";
						imageVo.setImageName(str);
						PostParameter[] postParams;
						postParams = new PostParameter[6];
						postParams[0] = new PostParameter("imageName", str + ".png");
						postParams[1] = new PostParameter("worker_id", sp.getUserId());
						postParams[2] = new PostParameter("category_id", machine.getCategory_id());
						postParams[3] = new PostParameter("trademark_id", machine.getTrademark_id());
						postParams[4] = new PostParameter("batch_number", machine.getBatch_number());
						postParams[5] = new PostParameter("upload_time", uploadTime);
						InputStream is = ImageSettingUtil.compressJPG(machineImages.get(i).getUrl());
						ImageSettingUtil.uploadImage(MachineDetailActivity.this, i, postParams, imageVo, is, Constants.UploadSomeMachineImage);
					}
					setResult(RESULT_OK);

					finish();

				}
			}
		}.start();

	}

	private void delete(int position) {
		new_machineImages.clear();
		if (isShowDelete) {
			machineImages.remove(position);
			isShowDelete = false;
		}
		new_machineImages.addAll(machineImages);
		machineImages.clear();
		machineImages.addAll(new_machineImages);
	}

	protected void showChoice() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(MachineDetailActivity.this);
		builder.setItems(new String[] { "本机相册", "拍摄" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent;
						switch (which) {
							case 0:{
								intent = new Intent(MachineDetailActivity.this,AlbumActivity.class);
								startActivityForResult(intent, Constants.LOCAL_PHOTO);
							}
							break;
							case 1: {
								Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(openCameraIntent, Constants.TAKE_PICTURE);
							}
							break;
							default:
								break;
						}
					}
				});
		builder.create().show();
	}

	protected void showViews() {
		// TODO Auto-generated method stub
		line.setVisibility(View.VISIBLE);
		rl_detail_detail_address.setVisibility(View.VISIBLE);
		rl_camera.setVisibility(View.VISIBLE);
		iv_locate.setVisibility(View.VISIBLE);
		rl_machineMessage.setVisibility(View.VISIBLE);
		machineAddress.setText(simpleAddress);
		machineDetailAddress.setText(address);

		machineNumber.setText(String.valueOf(machine.getMachineNumber()));
		machinePrice.setText(String.valueOf(machine.getWork_price()));

	}



	private void initData() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		netWorkUtil = new NetWorkUtil(getApplicationContext());
		sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		machine = (Machine) getIntent().getSerializableExtra("machine");
		InitalachineImages = (List<MachineImage>) getIntent().getSerializableExtra("machineImage");
		longtitude = getIntent().getDoubleExtra("longtitude", 0);
		lantitude = getIntent().getDoubleExtra("lantitude", 0);
		simpleAddress = getIntent().getStringExtra("simple_address");
		address = getIntent().getStringExtra("address");
		machineState.setText(machine.getIsChecked());
		machineAddress.setText(address);
		machineType.setText(machine.getCategory_name());
		machineTrademark.setText(machine.getTrademark_name());
		machineNumber.setText(String.valueOf(machine.getMachineNumber())+"台");
		machinePrice.setText(String.valueOf(machine.getWork_price())+"元/亩");
		modify.setVisibility(machine.getIsChecked().equals("审核中")?View.GONE:View.VISIBLE);
		unableEditViews();
		if(machine.getMachineDescriptionOrMessage().equals("")){
			rl_machineMessage.setVisibility(View.GONE);
		}else{
			rl_machineMessage.setVisibility(View.VISIBLE);
			machineMessage.setText(machine.getMachineDescriptionOrMessage());
		}

		if(!machine.getIsChecked().equals("审核通过")){
			machinePicture.setVisibility(View.GONE);
			machineImages.clear();
//			imageAdapter = new MachineDetailImageAdapter(getApplicationContext(), machineImages );
//			machinePicture.setAdapter(imageAdapter);
		}else{

			if(machine.getMachineImage().size()==0){
				machinePicture.setVisibility(View.GONE);
			}else{
				machinePicture.setVisibility(View.VISIBLE);
				if(machine.getMachineImage().size()>0 && machine.getMachineImage().size() <= 3){
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(100));
					lp.setMargins(0, dip2px(10), 0, 0);
					machinePicture.setLayoutParams(lp);
				}else if(machine.getMachineImage().size()>=3 && machine.getMachineImage().size() <= 6){
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(200));
					lp.setMargins(0, dip2px(10), 0, 0);
					machinePicture.setLayoutParams(lp);
				}else{
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(300));
					lp.setMargins(0, dip2px(10), 0, 0);
					machinePicture.setLayoutParams(lp);
				}
				machineImages.clear();
				machineImages = machine.getMachineImage();
//				imageAdapter = new MachineDetailImageAdapter(getApplicationContext(), machineImages );
//				machinePicture.setAdapter(imageAdapter);
			}
		}
		imageAdapter = new MachineDetailImageAdapter(getApplicationContext(), machineImages );
		machinePicture.setAdapter(imageAdapter);
	}

	private void canableEditViews() {
		// TODO Auto-generated method stub
		machinePrice.setEnabled(true);
		machineMessage.setEnabled(true);
	}

	private void unableEditViews() {
		// TODO Auto-generated method stub
		machineType.setEnabled(false);
		machineNumber.setEnabled(false);
		machinePrice.setEnabled(false);
		machineMessage.setEnabled(false);
	}

	public int dip2px(float dpValue) {

		final float scale =getApplicationContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mToolbar = (Toolbar)findViewById(R.id.machine_detail_toolbar);
		machineState = (TextView)findViewById(R.id.register_machine_state);
		iv_locate = (ImageView)findViewById(R.id.machine_address_detail_located);
		machineAddress = (TextView)findViewById(R.id.tv_machine_detail_address_1);
		rl_detail_detail_address = (RelativeLayout)findViewById(R.id.rl_machine_detail_address2);
		machineDetailAddress = (TextView)findViewById(R.id.tv_machine_detail_address_2);
		machineType = (TextView)findViewById(R.id.tv_published_machine_detail_type_1);
		machineTrademark = (TextView)findViewById(R.id.tv_machine_detail_brand_1);
		machineNumber = (TextView)findViewById(R.id.tv_machine_detail_number_1);
		machinePrice = (TextView)findViewById(R.id.detail_price);
		rl_machineMessage = (RelativeLayout)findViewById(R.id.rl_machine_detail_message);
		machineMessage = (EditText)findViewById(R.id.machine_detail_message);
		machinePicture = (GridView)findViewById(R.id.detail_noScrollgridview);
		modify = (Button)findViewById(R.id.btn_modify);
		line = (View)findViewById(R.id.line);
		rl_camera = (RelativeLayout)findViewById(R.id.rl_detail_camera);
		camera = (ImageView)findViewById(R.id.iv_detail_camera);

		machineListview=(NoScrollListView) findViewById(R.id.detail_machine_listview);
		nameAdapter=new MachineNameAdapter(getApplicationContext(), list);
		machineListview.setAdapter(nameAdapter);


	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 9:
				if (resultCode == Activity.RESULT_OK ) {
					machineAddress.setText(Constants.location.get(0));
					machineDetailAddress.setText(Constants.location.get(1));
				}
				break;
			case Constants.TAKE_PICTURE:
				if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUtils.saveBitmap(bm, fileName);

					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
					takePhoto.setImagePath(FileUtils.SDPATH + fileName+ ".JPEG");
					Bimp.tempSelectBitmap.add(takePhoto);
				}
				showPictures();
				break;
			case Constants.LOCAL_PHOTO:
				if(resultCode == Activity.RESULT_OK){
					showPictures();
				}
				break;
			default:
				break;
		}
	}

	private void showPictures() {
		// TODO Auto-generated method stub
		if(Bimp.tempSelectBitmap.size()==0){
			machinePicture.setVisibility(View.GONE);
			machineImages.clear();
		}else{
			machinePicture.setVisibility(View.VISIBLE);
			if(Bimp.tempSelectBitmap.size()>0 && Bimp.tempSelectBitmap.size() <= 3){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(100));
				lp.setMargins(0, dip2px(10), 0, 0);
				machinePicture.setLayoutParams(lp);
			}else if(Bimp.tempSelectBitmap.size()>3 && Bimp.tempSelectBitmap.size() <= 6){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(200));
				lp.setMargins(0, dip2px(10), 0, 0);
				machinePicture.setLayoutParams(lp);
			}else{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(300));
				lp.setMargins(0, dip2px(10), 0, 0);
				machinePicture.setLayoutParams(lp);
			}
			machineImages.clear();
			for(ImageItem ii : Bimp.tempSelectBitmap){
				MachineImage machineImage = new MachineImage();
				machineImage.setUrl(ii.getImagePath());
				machineImages.add(machineImage);
			}
			Log.i("kmj","showPictures-----------machineImages-------"+ machineImages);
			Log.i("kmj","showPictures-----------imageAdapter-------"+ imageAdapter);
			imageAdapter.setList(machineImages);
			imageAdapter.notifyDataSetChanged();
			Log.i("kmj","showPictures-----------machineImages--111-----");
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	protected boolean checkInfoIsValid() {
		// TODO Auto-generated method stub
		if(machineAddress.getText().toString().equals("")){
			ToastCustom.makeToast(MachineDetailActivity.this, "请选择农机地址");
			return false;
		}
		if(machineDetailAddress.getText().toString().equals("")){
			ToastCustom.makeToast(MachineDetailActivity.this, "请选择农机详细地址");
			return false;
		}
		if(machineNumber.getText().toString().equals("")){
			ToastCustom.makeToast(MachineDetailActivity.this, "请填写农机数量");
			return false;
		}
		if(machinePrice.getText().toString().equals("")){
			ToastCustom.makeToast(MachineDetailActivity.this, "请填写农机价格");
			return false;
		}
		if(!isNumber(machineNumber.getText().toString())){
			ToastCustom.makeToast(MachineDetailActivity.this, "数量应为数字");
			return false;
		}
		if(!isNumber(machinePrice.getText().toString())){
			ToastCustom.makeToast(MachineDetailActivity.this, "价格应为数字");
			return false;
		}
		return true;
	}

	public static boolean isNumber(String str){
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}

	@Override
	public void setUploadProgress(int index, double x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getRecodeFromServer(int index, String reCode, String result) {
		// TODO Auto-generated method stub
		if (reCode.equalsIgnoreCase("success")) {
			if( index == machineImages.size() - 1){
				Looper.prepare();
				ToastCustom.makeToast(getApplicationContext(), "您的农机处于审核状态中...");
				setResult(RESULT_OK);
				finish();
				Looper.loop();
			}
		} else{
			Looper.prepare();
			ToastCustom.makeToast(getApplicationContext(), "发布失败，请重试");
			Looper.loop();
		}
	}

}

/*class MyListView extends ListView {

	public MyListView(Context context) {
		// TODO Auto-generated method stub
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		super(context, attrs);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
} */
