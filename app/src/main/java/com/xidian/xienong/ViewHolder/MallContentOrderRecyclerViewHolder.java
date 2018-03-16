package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;

/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class MallContentOrderRecyclerViewHolder extends RecyclerView.ViewHolder{

    public ImageView commodity_image;
    public TextView commodity_name;
    public TextView unit_price;
    public TextView commodity_standard;
    public TextView commodity_number;
    public View dividerView;


    public MallContentOrderRecyclerViewHolder(View itemView) {
        super(itemView);
        //content
        this.commodity_image = (ImageView)itemView.findViewById(R.id.mall_order_image);
        this.commodity_name = (TextView)itemView.findViewById(R.id.mall_order_comm_name);
        this.unit_price = (TextView)itemView.findViewById(R.id.mall_order_unit_price);
        this.commodity_standard = (TextView)itemView.findViewById(R.id.mall_order_comm_standard);
        this.commodity_number = (TextView)itemView.findViewById(R.id.mall_order_every_comm_number);
        this.dividerView = (View)itemView.findViewById(R.id.divider_view);
    }
}
