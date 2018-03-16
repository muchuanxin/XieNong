package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class CommodityRecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView commodityName;
    public TextView  price;
    public TextView  specification;
    public TextView  place;
    public ImageView commodityImage;


    public CommodityRecyclerViewHolder(View itemView) {
        super(itemView);
        commodityImage = (ImageView) itemView.findViewById(R.id.iv_commodity_img);
        commodityName = (TextView)itemView.findViewById(R.id.tv_commodity_name);
        price = (TextView)itemView.findViewById(R.id.tv_commodity_price);
    }
}
