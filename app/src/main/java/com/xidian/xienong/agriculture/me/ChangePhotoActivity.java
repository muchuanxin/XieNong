package com.xidian.xienong.agriculture.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.ImageVo;
import com.xidian.xienong.photo.AlbumActivity;
import com.xidian.xienong.photo.Bimp;
import com.xidian.xienong.photo.ImageItem;
import com.xidian.xienong.photo.util.FileUtils;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.DialogFactory;
import com.xidian.xienong.util.ImageSettingUtil;
import com.xidian.xienong.util.ImageSettingUtil.ImageUploadDelegate;
import com.xidian.xienong.util.PostParameter;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import java.io.InputStream;

public class ChangePhotoActivity extends AppCompatActivity implements ImageUploadDelegate{

	private ImageButton back;
	private ImageButton more;
	private ImageView user_photo;
	private SharePreferenceUtil sp;
	private Dialog mDialog = null;
	private RequestQueue requestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_photo_activity);
		initViews();
		initDatas();
		initEvents();

	}

	private void initEvents() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChoice();
			}
		});
	}

	protected void showChoice() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(ChangePhotoActivity.this);
		builder.setItems(new String[] { "本机相册", "拍摄" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent;
						switch (which) {
							case 0:{
								intent = new Intent(ChangePhotoActivity.this,AlbumActivity.class);
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

	private void initDatas() {
		// TODO Auto-generated method stub
		sp = new SharePreferenceUtil(ChangePhotoActivity.this, Constants.SAVE_USER);
		if(!sp.getHeadPhoto().equals("")){
			Glide.with(ChangePhotoActivity.this).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.portrait).into(user_photo);
		}
		requestQueue = Volley.newRequestQueue(getApplicationContext());
	}

	private void initViews() {
		// TODO Auto-generated method stub
		back = (ImageButton)findViewById(R.id.btn_change_photo_back);
		more = (ImageButton)findViewById(R.id.btn_more);
		user_photo = (ImageView)findViewById(R.id.user_headphoto);

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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Constants.TAKE_PICTURE:
				Bimp.tempSelectBitmap.clear();
				if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUtils.saveBitmap(bm, fileName);

					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
					takePhoto.setImagePath(FileUtils.SDPATH + fileName+ ".JPEG");
					Bimp.tempSelectBitmap.add(takePhoto);
					Log.i("kmj","-----paths.get(i).jpg------------"+Bimp.tempSelectBitmap.get(0).getImagePath());
				}
				uploadPicture();
				break;
			case Constants.LOCAL_PHOTO:
				if(resultCode == Activity.RESULT_OK){
					uploadPicture();
				}
				break;
			default:
				break;
		}
	}

	private void uploadPicture() {
		// TODO Auto-generated method stub
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if(Bimp.tempSelectBitmap.size() > 1){
					Looper.prepare();
					ToastCustom.makeToast(getApplicationContext(), "您最多只能选择一张图片");
					Looper.loop();
				}else{
					Log.i("kmj","-----p1111--------");
					for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
						ImageVo imageVo = new ImageVo();
						imageVo.setImagePath(Bimp.tempSelectBitmap.get(i).getImagePath());
						String str = sp.getFarmerId().toString() + "_"
								+ System.currentTimeMillis() + "";
						imageVo.setImageName(str);
						PostParameter[] postParams;
						postParams = new PostParameter[3];
						postParams[0] = new PostParameter("imageName", str + ".png");
						postParams[1] = new PostParameter("user_id", sp.getUserId());
						InputStream is = ImageSettingUtil.compressJPG( Bimp.tempSelectBitmap.get(0).getImagePath());
						ImageSettingUtil.uploadImage(ChangePhotoActivity.this, i, postParams, imageVo, is, Constants.uploadHeadImage);
					}

				}
			}
		}.start();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(ChangePhotoActivity.this, "正在上传...");
		mDialog.show();
	}

	public void dismissRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	@Override
	public void setUploadProgress(int index, double x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getRecodeFromServer(int index, String reCode,String imageUrl) {
		// TODO Auto-generated method stub
		if (reCode.equalsIgnoreCase("success") && index == 0) {
			Looper.prepare();
			sp.setHeadPhoto(imageUrl);
			ToastCustom.makeToast(getApplicationContext(), "上传成功");
			finish();
			Looper.loop();
//			sp.setHeadPhoto(headPhoto);
		} else{
			Looper.prepare();
			ToastCustom.makeToast(ChangePhotoActivity.this, "上传失败，请重试！");
			Looper.loop();
		}
	}

}
