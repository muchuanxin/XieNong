package com.xidian.xienong.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.util.CircleImageView;
import com.xidian.xienong.util.RoundedImageView;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class OrderRecyclerViewHolder extends RecyclerView.ViewHolder{

    public CircleImageView publisher_photo;
    public TextView publisher_name;
    public TextView publisher_time;
    public TextView type;
    public TextView reservation_time;
    public TextView iv_receive;

    public OrderRecyclerViewHolder(View itemView) {
        super(itemView);
        publisher_photo = (CircleImageView)itemView.findViewById(R.id.order_publisher_photo);
        publisher_name = (TextView)itemView.findViewById(R.id.order_publisher_name);
        publisher_time =(TextView)itemView.findViewById(R.id.order_publisher_time);
        iv_receive =(TextView)itemView.findViewById(R.id.order_publisher_state);
        type =(TextView)itemView.findViewById(R.id.order_wanted_type);
        reservation_time =(TextView)itemView.findViewById(R.id.order_reserve_time);
    }
}
