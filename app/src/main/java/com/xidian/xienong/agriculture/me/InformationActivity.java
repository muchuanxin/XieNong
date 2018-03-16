package com.xidian.xienong.agriculture.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;


public class InformationActivity extends AppCompatActivity{

	private ImageView head_photo;
	private TextView name;
	private TextView tele;
	private SharePreferenceUtil sp;
	private String user_name="";
	private RelativeLayout rl_photo;
	private RelativeLayout rl_name;
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_activity);
		setTitle("个人信息");
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
		rl_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InformationActivity.this, ChangeNameActivity.class);
				startActivity(intent);
			}
		});
		rl_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InformationActivity.this, ChangePhotoActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		sp = new SharePreferenceUtil(InformationActivity.this, Constants.SAVE_USER);
		if(!sp.getHeadPhoto().equals("")){
			Glide.with(InformationActivity.this).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.portrait).into(head_photo);
		}
		name.setText(sp.getUserName());
		tele.setText(sp.getPhoneNumber());
	}

	private void initViews() {
		// TODO Auto-generated method stub
		rl_photo = (RelativeLayout)findViewById(R.id.rl_photo);
		rl_name = (RelativeLayout)findViewById(R.id.rl_name);
		head_photo = (ImageView)findViewById(R.id.information_headphoto);
		name = (TextView)findViewById(R.id.information_name);
		tele = (TextView)findViewById(R.id.information_tele);
		mToolbar = (Toolbar)findViewById(R.id.information_toolbar);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		name.setText(sp.getUserName());
		Glide.with(InformationActivity.this).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.portrait).into(head_photo);
		Log.i("liuhaoxian", "onResume" + sp.getHeadPhoto());
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}



}
