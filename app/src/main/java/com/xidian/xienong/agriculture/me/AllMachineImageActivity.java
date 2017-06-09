package com.xidian.xienong.agriculture.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.GridViewAdapter;
import com.xidian.xienong.model.MachineImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/9.
 */

public class AllMachineImageActivity extends AppCompatActivity{

    private Toolbar machineImageToolbar;
    private GridView gv;
    private List<MachineImage> images = new ArrayList<MachineImage>();
    private GridViewAdapter gva;
    private ArrayList<String> urls = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_image_activity);
        initViews();
        initData();
        initEvents();
    }

    private void initEvents() {
        machineImageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(AllMachineImageActivity.this, ImagePagerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("httpurl", (Serializable)urls);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        setSupportActionBar(machineImageToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        images = (List<MachineImage>) getIntent().getExtras().get("image_url");
        gva = new GridViewAdapter(images, getApplicationContext());
        gv.setAdapter(gva);
        for(int i=0;i < images.size(); i++){
            urls.add(images.get(i).getUrl());
        }
    }

    private void initViews() {
        machineImageToolbar = (Toolbar)findViewById(R.id.machine_image_toolbar);
        gv = (GridView) findViewById(R.id.allpic_gridview);
    }
}
