package com.xidian.xienong.agriculture.resource;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
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
import com.xidian.xienong.adapter.MachineCategoryListAdapter;
import com.xidian.xienong.adapter.MachineImageGridVewAdapter;
import com.xidian.xienong.adapter.PreviewImageAdapter;
import com.xidian.xienong.adapter.TradeMarkAdapter;
import com.xidian.xienong.agriculture.announcement.MapChoosePointActivity;
import com.xidian.xienong.model.ImageVo;
import com.xidian.xienong.model.MachineCategory;
import com.xidian.xienong.model.MachineTrademark;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.photo.AlbumActivity;
import com.xidian.xienong.photo.Bimp;
import com.xidian.xienong.photo.GalleryActivity;
import com.xidian.xienong.photo.ImageItem;
import com.xidian.xienong.photo.util.FileUtils;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.ImageSettingUtil;
import com.xidian.xienong.util.ImageSettingUtil.ImageUploadDelegate;
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

;import okhttp3.Response;


public class NewMachineActivity extends AppCompatActivity implements ImageUploadDelegate {
	
	private SharePreferenceUtil sp;
	private RequestQueue requestQueue;
	private ImageView machine_address_located,camera;
	private TextView machine_address;
	private TextView machine_detail_address;
	private TextView machine_type;
	private TextView machine_brand;
	private TextView machine_number;
	private TextView work_price;
	private RelativeLayout rl_machine_message;
	private TextView machine_message;
	private RelativeLayout machine_picture;
	private Button info_preview;
	private Button machine_publish;
	private List<MachineCategory> havePublishedMachineCategory = new ArrayList<MachineCategory>();
	private List<MachineCategory> canAddedMachineCategory = new ArrayList<MachineCategory>();
	private List<MachineCategory> tempAllMachineCategory = new ArrayList<MachineCategory>();
	private List<MachineTrademark> allMachineTrademark =  new ArrayList<MachineTrademark>();
	private String category_id="";
	private MachineImageGridVewAdapter adapter;
	
	private GridView noScrollgridview;
	boolean isOpen = false;
	private PopupWindow pop;
	private View layout;
	private TextView preview_address_1;
	private TextView preview_address_2;
	private TextView preview_machine_type_1,preview_machine_trademark_1;
	private TextView preview_number_1;
	private TextView preview_price;
	private RelativeLayout rl_preview_message;
	private TextView preview_message;
	private GridView preview_noScrollgridview;
	private PreviewImageAdapter imageAdapter;
	private String uploadTime="";
	private LinearLayout preview_window;
	private RelativeLayout rl_address;
	private String trademark_id=""; //品牌id
	private String batch_number=""; //批次号
	private Toolbar mToolbar;
	private OKHttp httpUrl;
	private MachineCategoryListAdapter machineCategoryAdapter;
	private TradeMarkAdapter tradeMarkAdapter;
	private AlertDialog machineDialog,trademarkDialog;
	private ListView chooseMachineListView,chooseMachineTrademarkListView;
	private View chooseMachineLayout,chooseTrademarkLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.new_machine_activity);
		initViews();
		initData();
		initEvents();

	}

	private void initEvents() {
		// TODO Auto-generated method stub
		mToolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		machine_address_located.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewMachineActivity.this,MapChoosePointActivity.class);
				intent.putExtra("activity", "new_machine_activity");
				startActivityForResult(intent, 6);
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
		chooseMachineTrademarkListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				trademark_id = Constants.machineTrademark.get(position).getTrademark_id();
				machine_brand.setText(Constants.machineTrademark.get(position).getTrademark_name());
				trademarkDialog.dismiss();
			}
		});
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					showChoice();
				} else {
					Intent intent = new Intent(NewMachineActivity.this,GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

		machine_type.setOnClickListener(new OnClickListener() {

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

		machine_brand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
                if(Constants.machineTrademark.size()==0){
                    getAllMachineTrademark();
                }else{
                    showMachineTrademark();
                }
			}
		});

		rl_machine_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NewMachineActivity.this,MachineIntroductionOrMessageActivity.class);
				intent.putExtra("machine_intro",machine_message.getText().toString());
				startActivityForResult(intent, 7);
			}
		});

		machine_picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChoice();
			}
		});

		info_preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkInfoIsValid()){
					PopWindow();
				}
			}
		});

		machine_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkInfoIsValid()){
					new SweetAlertDialog(NewMachineActivity.this, SweetAlertDialog.TIP_TYPE)
							.setTitleText("确认发布信息")
							.setContentText("确定发布您的农机信息")
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
									registerMachine();
								}
							})
							.show();
				}
			}
		});
	}

	private void showMachineCategory() {
		machineCategoryAdapter.notifyDataSetChanged();
		machineDialog.show();
		machineDialog.getWindow().setContentView(chooseMachineLayout);
	}

	private void showMachineTrademark() {
		tradeMarkAdapter.notifyDataSetChanged();
		trademarkDialog.show();
		trademarkDialog.getWindow().setContentView(chooseTrademarkLayout);
	}


	private void registerMachine() {
		// TODO Auto-generated method stub

		StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.PublishMachines,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						try {
							JSONObject jb = new JSONObject(response);
							String result = jb.getString("reCode");
							String message = jb.getString("message");
							if (result.equals("SUCCESS")) {
								batch_number = jb.getString("batch_number");
								if(machine_message.getText().toString().equals("") && Bimp.tempSelectBitmap.size()==0){
									ToastCustom.makeToast(getApplicationContext(), "发布成功");
									setResult(RESULT_OK);
									finish();

								}else{
									if(Bimp.tempSelectBitmap.size()!=0){
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
				map.put("longtitude", Constants.location.get(3));
				map.put("lantitude", Constants.location.get(2));
				map.put("simple_address", machine_address.getText().toString());
				map.put("address",machine_detail_address.getText().equals("")?machine_address.getText().toString():machine_detail_address.getText().toString());
				map.put("category_id",category_id);
				map.put("trademark_id", trademark_id);
				map.put("number", machine_number.getText().toString());
				map.put("work_price",work_price.getText().toString());
				map.put("machine_description_or_message",machine_message.getText().toString());
				map.put("imageSize",String.valueOf(Bimp.tempSelectBitmap.size()));
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
				if(Bimp.tempSelectBitmap.size() > 9){
					Looper.prepare();
					ToastCustom.makeToast(getApplicationContext(), "您最多只能选择9张图片");
					Looper.loop();
				}else{
					for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
						ImageVo imageVo = new ImageVo();
						imageVo.setImagePath(Bimp.tempSelectBitmap.get(i).getImagePath());
						String str = sp.getWorkerId().toString() + "_"
								+ System.currentTimeMillis() + "";
						imageVo.setImageName(str);
						PostParameter[] postParams;
						postParams = new PostParameter[6];
						postParams[0] = new PostParameter("imageName", str + ".png");
						postParams[1] = new PostParameter("worker_id", sp.getUserId());
						postParams[2] = new PostParameter("category_id", category_id);
						postParams[3] = new PostParameter("trademark_id", trademark_id);
						postParams[4] = new PostParameter("batch_number", batch_number);
						postParams[5] = new PostParameter("upload_time", uploadTime);
						InputStream is = ImageSettingUtil.compressJPG( Bimp.tempSelectBitmap.get(i).getImagePath());
						ImageSettingUtil.uploadImage(NewMachineActivity.this, i, postParams, imageVo, is, Constants.UploadSomeMachineImage);
					}
					setResult(RESULT_OK);
					finish();
				}
			}
		}.start();
	}

	protected boolean checkInfoIsValid() {
		// TODO Auto-generated method stub
		if(machine_address.getText().toString().equals("")){
			ToastCustom.makeToast(NewMachineActivity.this, "请选择农机地址");
			return false;
		}
		if(machine_type.getText().toString().equals("")){
			ToastCustom.makeToast(NewMachineActivity.this, "请选择农机种类");
			return false;
		}
		if(machine_brand.getText().toString().equals("")){
			ToastCustom.makeToast(NewMachineActivity.this, "请选择农机品牌");
			return false;
		}
		if(machine_number.getText().toString().equals("")){
			ToastCustom.makeToast(NewMachineActivity.this, "请填写农机数量");
			return false;
		}
		if(work_price.getText().toString().equals("")){
			ToastCustom.makeToast(NewMachineActivity.this, "请填写您的收费标准");
			return false;
		}
		if(!isNumber(work_price.getText().toString())){
			ToastCustom.makeToast(NewMachineActivity.this, "价格应为数字");
			return false;
		}
		return true;
	}

	public static boolean isNumber(String str){
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}

	protected void PopWindow() {
		// TODO Auto-generated method stub
		if (isOpen == true && pop.isShowing()){
			isOpen = false;
			pop.dismiss();
		}else{
			setPopWindowData();
		}
	}

	private void setPopWindowData() {
		// TODO Auto-generated method stub
		preview_address_1.setText(machine_address.getText().toString());
		preview_address_2.setText(machine_detail_address.getText().toString());
		preview_machine_type_1.setText(machine_type.getText().toString());
		preview_machine_trademark_1.setText(machine_brand.getText().toString());
		preview_number_1.setText(machine_number.getText().toString()+"台");
		preview_price.setText(work_price.getText().toString()+"元/亩");
		if(machine_message.getText().equals("")){
			rl_preview_message.setVisibility(View.GONE);
		}else{
			rl_preview_message.setVisibility(View.VISIBLE);
			preview_message.setText(machine_message.getText());
		}
		showPopPictures();
		pop = new PopupWindow(layout,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true) ;
		pop.setTouchable(true); // 设置popupwindow可点击
		pop.setOutsideTouchable(true);  // 设置popupwindow外部可点击
		pop.setFocusable(true); //获取焦点
		pop.update();
		pop.showAsDropDown(rl_address);
		pop.setBackgroundDrawable(new ColorDrawable(0));
		pop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				isOpen = false;
				pop.dismiss();
			}
		});
		isOpen = true;

		/****   如果点击了popupwindow的外部，popupwindow消失 ****/
		pop.setTouchInterceptor(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pop.dismiss();
					return true;
				}
				return false;
			}
		});
		preview_window.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
	}

	private void backgroundAlpha(float bgAlpha) {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	private void showPopPictures() {
		// TODO Auto-generated method stub
		if(Bimp.tempSelectBitmap.size()==0){
			preview_noScrollgridview.setVisibility(View.GONE);
		}else{
			preview_noScrollgridview.setVisibility(View.VISIBLE);
			if(Bimp.tempSelectBitmap.size()>0 && Bimp.tempSelectBitmap.size() <= 3){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(100));
				lp.setMargins(0, dip2px(10), 0, 0);
				preview_noScrollgridview.setLayoutParams(lp);
			}else if(Bimp.tempSelectBitmap.size()>=3 && Bimp.tempSelectBitmap.size() <= 6){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(190));
				lp.setMargins(0, dip2px(10), 0, 0);
				preview_noScrollgridview.setLayoutParams(lp);
			}else{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(280));
				lp.setMargins(0, dip2px(10), 0, 0);
				preview_noScrollgridview.setLayoutParams(lp);
			}
			imageAdapter.notifyDataSetChanged();
		}
	}

	protected void showChoice() {
		// TODO Auto-generated method stub
		Builder builder = new Builder(NewMachineActivity.this);
		builder.setItems(new String[] { "本机相册", "拍摄" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent;
						switch (which) {
							case 0:{
								intent = new Intent(NewMachineActivity.this,AlbumActivity.class);
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


	private void getMachineCategory() {
		Constants.machineCategory.clear();
		Map<String, String> map = new HashMap<String, String>();
		httpUrl.post(Url.GetAllMachineCategory, map, new BaseCallback<String>() {

			@Override
			public void onRequestBefore() {
				Log.i("kmj","Url.GetAllMachineCategory: " + Url.GetAllMachineCategory);
			}

			@Override
			public void onFailure(okhttp3.Request request, Exception e) {}

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
						Toast.makeText(NewMachineActivity.this, message,Toast.LENGTH_SHORT).show();
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

	private void getAllMachineTrademark() {
		// TODO Auto-generated method stub
		Constants.machineTrademark.clear();
		StringRequest stringrequest = new StringRequest(Request.Method.POST, Url.GetAllMachineTrademark,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						try {
							JSONObject jb = new JSONObject(response);
							String result = jb.getString("reCode");
							String message = jb.getString("message");

							if (result.equals("SUCCESS")) {
								JSONArray list = jb.getJSONArray("machineTrademark");
								for(int i=0; i < list.length(); i++){
									JSONObject object = list.getJSONObject(i);
									MachineTrademark mt=new MachineTrademark();
									mt.setTrademark_id(object.getString("trademark_id"));
									mt.setTrademark_name(object.getString("trademark"));
									Constants.machineTrademark.add(mt);
								}
								tradeMarkAdapter.notifyDataSetChanged();
								trademarkDialog.show();
								trademarkDialog.getWindow().setContentView(chooseTrademarkLayout);
							}else{
								Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
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
				return map;
			}
		};
		requestQueue.add(stringrequest);
	}



	private void initViews() {
		// TODO Auto-generated method stub
		layout = getLayoutInflater().inflate(R.layout.preview_window,null);
		preview_window = (LinearLayout)layout.findViewById(R.id.ll_preview_window);
		preview_address_1 = (TextView)layout.findViewById(R.id.tv_machine_preview_address_1);
		preview_address_2 = (TextView)layout.findViewById(R.id.tv_machine_preview_address_2);
		preview_machine_type_1 = (TextView)layout.findViewById(R.id.tv_published_preview_machine_type_1);
		preview_machine_trademark_1 = (TextView)layout.findViewById(R.id.tv_published_preview_machine_trademark_1);
		preview_number_1 = (TextView)layout.findViewById(R.id.tv_machine_preview_number_1);
		preview_price = (TextView)layout.findViewById(R.id.preview_price);
		rl_preview_message = (RelativeLayout)layout.findViewById(R.id.rl_machine_preview_message);
		preview_message = (TextView)layout.findViewById(R.id.machine_preview_message);
		preview_noScrollgridview = (GridView)layout.findViewById(R.id.preview_noScrollgridview);
		imageAdapter = new PreviewImageAdapter(getApplicationContext());
		preview_noScrollgridview.setAdapter(imageAdapter);

		rl_address = (RelativeLayout)findViewById(R.id.rl_address);
		machine_address_located = (ImageView) findViewById(R.id.machine_address_located);
		machine_address = (TextView)findViewById(R.id.tv_machine_address_1);
		machine_detail_address = (TextView)findViewById(R.id.tv_machine_address_2);

		machine_type = (TextView)findViewById(R.id.tv_published_machine_type_1);
		machine_brand = (TextView)findViewById(R.id.tv_machine_brand_1);
		machine_number = (TextView)findViewById(R.id.tv_machine_number_1);
		work_price = (EditText)findViewById(R.id.price);
		rl_machine_message = (RelativeLayout)findViewById(R.id.rl_machine_message);
		machine_message = (TextView)findViewById(R.id.tv_message);
		machine_picture = (RelativeLayout)findViewById(R.id.rl_iv_camera);
		camera =  (ImageView) findViewById(R.id.iv_camera);
		info_preview = (Button)findViewById(R.id.btn_machine_preview);
		machine_publish = (Button)findViewById(R.id.btn_publish_machine);
		noScrollgridview = (GridView)findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new MachineImageGridVewAdapter(getApplicationContext());
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		mToolbar = (Toolbar)findViewById(R.id.new_machine_toolbar);
        machineDialog = new AlertDialog.Builder(NewMachineActivity.this).create();
		chooseMachineLayout = getLayoutInflater().inflate(R.layout.machine_list_alert_dialog,null);
		chooseMachineListView = (ListView) chooseMachineLayout.findViewById(R.id.choose_machine_list);
		trademarkDialog = new AlertDialog.Builder(NewMachineActivity.this).create();
		chooseTrademarkLayout = getLayoutInflater().inflate(R.layout.trademark_list_alert_dialog,null);
		chooseMachineTrademarkListView = (ListView) chooseTrademarkLayout.findViewById(R.id.choose_trademark_list);
	}

	private void initData() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		httpUrl = OKHttp.getInstance();
		sp = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		machineCategoryAdapter = new MachineCategoryListAdapter(NewMachineActivity.this,Constants.machineCategory);
		chooseMachineListView.setAdapter(machineCategoryAdapter);
		tradeMarkAdapter = new TradeMarkAdapter(NewMachineActivity.this,Constants.machineTrademark);
		chooseMachineTrademarkListView.setAdapter(tradeMarkAdapter);
		Bimp.tempSelectBitmap.clear();
	}



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 6:
				if (resultCode == Activity.RESULT_OK ) {
					machine_address.setText(Constants.location.get(0));
					machine_detail_address.setText(Constants.location.get(1));
				}
				break;
			case 7:
				if (resultCode == RESULT_OK ) {
					machine_message.setText(data.getStringExtra("new_machine_intro"));
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
			noScrollgridview.setVisibility(View.GONE);
		}else{
			noScrollgridview.setVisibility(View.VISIBLE);
			if(Bimp.tempSelectBitmap.size()>0 && Bimp.tempSelectBitmap.size() < 3){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(100));
				lp.setMargins(0, dip2px(10), 0, 0);
				noScrollgridview.setLayoutParams(lp);
			}else if(Bimp.tempSelectBitmap.size()>=3 && Bimp.tempSelectBitmap.size() < 6){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(190));
				lp.setMargins(0, dip2px(10), 0, 0);
				noScrollgridview.setLayoutParams(lp);
			}else{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(280));
				lp.setMargins(0, dip2px(10), 0, 0);
				noScrollgridview.setLayoutParams(lp);
			}
			adapter.notifyDataSetChanged();
		}
	}

	public int dip2px(float dpValue) {

		final float scale =getApplicationContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setUploadProgress(int index, double x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getRecodeFromServer(int index, String reCode, String imageUrl) {
		// TODO Auto-generated method stub
		if (reCode.equalsIgnoreCase("success")) {
			if( index == Bimp.tempSelectBitmap.size() - 1){
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
