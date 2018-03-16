package com.xidian.xienong.shoppingmall.classification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.SecondContentAdapter;
import com.xidian.xienong.model.FirstContent;
import com.xidian.xienong.model.FirstInfo;
import com.xidian.xienong.model.SecondContent;
import com.xidian.xienong.shoppingmall.brand.CommodityShowActivity;
import com.xidian.xienong.util.NoScrollGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.xidian.xienong.R.id.binggan;


public class SecondClassIndexListFragment extends Fragment {

    private FirstContent fc = null;
    private View view;
    private SecondContentAdapter adapter;
    private NoScrollGridView secondGrideView;
    private ImageView iv;
    private String url="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fc = (FirstContent) getArguments().get("first_content");
        url = (String) getArguments().get("url");
        view = inflater.inflate(R.layout.second_content_layout, null);
        iv = (ImageView)view.findViewById(R.id.first_image);
        Glide.with(getActivity()).load(url).centerCrop().placeholder(R.drawable.empty_picture).into(iv);
        secondGrideView = (NoScrollGridView)view.findViewById(R.id.class_second_gridview);
        adapter = new SecondContentAdapter(fc.getSecondContentList(),getActivity());
        secondGrideView.setAdapter(adapter);
        initEvents();
        return view;
    }

    private void initEvents() {
        secondGrideView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SecondContent sc = fc.getSecondContentList().get(position);
                Intent intent = new Intent(getActivity(), CommodityShowActivity.class);
                intent.putExtra("secondContent", (Serializable) sc);
                startActivity(intent);

            }
        });
    }
}
