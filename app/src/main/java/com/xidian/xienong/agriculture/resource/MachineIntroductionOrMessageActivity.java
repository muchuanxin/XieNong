package com.xidian.xienong.agriculture.resource;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xidian.xienong.R;

public class MachineIntroductionOrMessageActivity extends AppCompatActivity{
	
	private EditText machine_intro;
	private ImageButton back;
	private Button save;
	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.machine_introduction_or_message_activity);
		initViews();
		initDatas();
		initEvents();
		
	}

	private void initEvents() {
		// TODO Auto-generated method stub
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				intent.putExtra("new_machine_intro", machine_intro.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		machine_intro.setText(getIntent().getStringExtra("machine_intro"));
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mToolbar = (Toolbar)findViewById(R.id.machine_intro_toolbar);
		save = (Button)findViewById(R.id.btn_save_intro);
		machine_intro = (EditText)findViewById(R.id.tv_machine_intro);
	}

}
