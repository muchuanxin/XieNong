package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xidian.xienong.R;

/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class MallTopOrderRecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView order_generated_time;
    public TextView order_status;

    public MallTopOrderRecyclerViewHolder(View itemView) {
        super(itemView);
        //top
        this.order_generated_time = (TextView)itemView.findViewById(R.id.mall_order_generate_time);
        this.order_status = (TextView)itemView.findViewById(R.id.mall_order_status);
    }
}
