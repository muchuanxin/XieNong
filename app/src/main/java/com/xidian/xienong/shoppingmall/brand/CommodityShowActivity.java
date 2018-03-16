package com.xidian.xienong.shoppingmall.brand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.CommodityAdapter;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.SecondContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class CommodityShowActivity extends AppCompatActivity implements CommodityAdapter.OnItemClickListener {

    private Toolbar toolBar;
    private Intent previousIntent;
    private List<Commodity> list = new ArrayList<>();
    private RecyclerView commodityRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int SPAN_COUNT = 2;
    private CommodityAdapter adapter;
    private String contentName;
    private SecondContent secondContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_show_activity);
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        previousIntent = getIntent();
        secondContent = (SecondContent)previousIntent.getSerializableExtra("secondContent");
        contentName = secondContent.getSecondCategory();
        getSupportActionBar().setTitle(contentName);
        list = secondContent.getCommodities();
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        adapter = new CommodityAdapter(CommodityShowActivity.this,list);

        commodityRecyclerView.setAdapter(adapter);
        commodityRecyclerView.setLayoutManager(mLayoutManager);
        adapter.setOnItemClickListener(this);
    }

    private void initViews() {
        toolBar = (Toolbar)findViewById(R.id.commodity_show_toolbar);
        commodityRecyclerView = (RecyclerView)findViewById(R.id.commodity_recyclerview);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(CommodityShowActivity.this,CommodityDetailActivity.class);
        intent.putExtra("commodity",list.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
