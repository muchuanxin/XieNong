package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.util.NoScrollGridView;

/**
 * Created by koumiaojuan on 2017/6/29.
 */

public class BrandRecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView brandName;
    public ImageView imageView;
    public GridView gridView;
    public TextView noContent;

    public BrandRecyclerViewHolder(View itemView) {
        super(itemView);
        brandName = (TextView)itemView.findViewById(R.id.tv_brand_name_1);
        imageView = (ImageView)itemView.findViewById(R.id.iv_brand_pic);
        gridView = (NoScrollGridView)itemView.findViewById(R.id.brand_gridview_1);
        noContent = (TextView)itemView.findViewById(R.id.tv_no_content);
    }
}
