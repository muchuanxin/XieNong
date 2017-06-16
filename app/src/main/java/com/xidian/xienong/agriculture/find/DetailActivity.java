package com.xidian.xienong.agriculture.find;

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
import com.xidian.xienong.agriculture.me.AllCommentActivity;
import com.xidian.xienong.agriculture.me.AllMachineImageActivity;
import com.xidian.xienong.model.Worker;

import java.io.Serializable;


public class DetailActivity extends AppCompatActivity {
	
	private ImageView machine_picture;
	private RelativeLayout rlMachineInfo;
	private TextView picNum;
	private TextView workerName;
	private TextView workerAddress;
	private TextView machineType;
	private TextView machineNumber;
	private TextView workPrice;
	private RelativeLayout record;
	private RelativeLayout comment;
	private Worker worker;
	private RelativeLayout detialMessage;
	private TextView tvMessage;
    private Toolbar mToolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worker_detail_activity);
		setTitle("详情");
	/*	showDetailViews();*/
		initViews();
		initDatas();
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
		machine_picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DetailActivity.this,AllMachineImageActivity.class);
				intent.putExtra("image_url", (Serializable) worker.getMachineImages());
				startActivity(intent);
			}
		});

		comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DetailActivity.this,AllCommentActivity.class);
				intent.putExtra("flag","detail_activity");
				intent.putExtra("id",worker.getWorkerId());
				startActivity(intent);
			}
		});

	}

	private void initDatas() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		worker = (Worker) getIntent().getSerializableExtra("worker");
		if(worker.getMachineImages().size()!=0){
			rlMachineInfo.setVisibility(View.VISIBLE);
			machine_picture.setVisibility(View.VISIBLE);
			Glide.with(getApplicationContext()).load(worker.getMachineImages().get(0).getUrl()).centerCrop().placeholder(R.drawable.empty_picture).into(machine_picture);
			picNum.setText(worker.getMachineImages().size()+"张效果图");
		}else{
			rlMachineInfo.setVisibility(View.GONE);
			machine_picture.setVisibility(View.GONE);
		}
		workerName.setText(worker.getWorkerName());
		workerAddress.setText(worker.getAddress());
		machineType.setText(worker.getCategory_name());
		machineNumber.setText(worker.getMachine_number()+"台");
		workPrice.setText(worker.getWork_price()+"元/亩");
		if(worker.getMachineDescription().equals("")){
			detialMessage.setVisibility(View.GONE);
		}else{
			detialMessage.setVisibility(View.VISIBLE);
			tvMessage.setText(worker.getMachineDescription());
		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		rlMachineInfo = (RelativeLayout)findViewById(R.id.rl_detail_picture);
		machine_picture = (ImageView)findViewById(R.id.iv_detail_picture);
		picNum = (TextView)findViewById(R.id.detail_numofpic);
		workerName =  (TextView)findViewById(R.id.tv_detail_name);
		workerAddress = (TextView)findViewById(R.id.tv_worker_machine_address);
		machineType  = (TextView)findViewById(R.id.tv_worker_machine_type);
		machineNumber = (TextView)findViewById(R.id.tv_worker_machine_num);
		workPrice = (TextView)findViewById(R.id.tv_worker_machine_price);
		record = (RelativeLayout)findViewById(R.id.rl_worker_machine_record);
		comment = (RelativeLayout)findViewById(R.id.rl_comment);
		detialMessage = (RelativeLayout)findViewById(R.id.rl_detail_message);
		tvMessage = (TextView)findViewById(R.id.detail_message);
		mToolbar=(Toolbar)findViewById(R.id.machine_detail_toolbar);
	}

}