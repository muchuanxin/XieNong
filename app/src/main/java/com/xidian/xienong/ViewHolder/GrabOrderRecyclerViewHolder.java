package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.xidian.xienong.R;
import com.xidian.xienong.util.CircleImageView;
import com.xidian.xienong.util.RoundedImageView;

/**
 * Created by koumiaojuan on 2017/6/12.
 */

public class GrabOrderRecyclerViewHolder extends RecyclerView.ViewHolder{
    public CircleImageView farmer_order_photo;
    public TextView farmer_order_name;
    public TextView farmer_order_code;
    public TextView farmer_order_place;
    public TextView tv_grab_order;
    public TextView farmer_order_type;
    public TextView farmer_order_time;
    public TextView farmer_order_cropType;
    public TextView farmer_order_cropNumber;

    public GrabOrderRecyclerViewHolder(View itemView) {
        super(itemView);
        farmer_order_photo =(CircleImageView)itemView.findViewById(R.id.farmer_order_photo);
        farmer_order_name =(TextView)itemView.findViewById(R.id.farmer_order_name);
        farmer_order_code =(TextView)itemView.findViewById(R.id.farmer_order_code);
        farmer_order_place =(TextView)itemView.findViewById(R.id.farmer_order_place);
        tv_grab_order =(TextView)itemView.findViewById(R.id.btn_grab_order);
        farmer_order_type =(TextView)itemView.findViewById(R.id.farmer_order_type);
        farmer_order_time =(TextView)itemView.findViewById(R.id.farmer_order_time);
        farmer_order_cropType =(TextView)itemView.findViewById(R.id.farmer_order_cropType);
        farmer_order_cropNumber =(TextView)itemView.findViewById(R.id.farmer_order_cropNumber);
    }
}
