package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.util.CircleImageView;

/**
 * Created by koumiaojuan on 2017/6/15.
 */

public class MachineRecyclerViewHolder extends RecyclerView.ViewHolder{

    public CircleImageView machine_publisher_photo;
    public TextView machine_publisher_name;
    public TextView machine_publish_time;
    public TextView machine_state;
    public TextView register_machine_type;
    public TextView register_machine_trademark;
    public TextView register_machine_number;
    public ImageButton btn_modify;
    public GridView gridView;


    public MachineRecyclerViewHolder(View itemView) {
        super(itemView);
        machine_publisher_photo =(CircleImageView)itemView.findViewById(R.id.machine_publisher_photo);
        machine_publisher_name =(TextView)itemView.findViewById(R.id.machine_publisher_name);
        machine_publish_time =(TextView)itemView.findViewById(R.id.machine_publish_time);
        machine_state =(TextView)itemView.findViewById(R.id.machine_state);
        register_machine_type =(TextView)itemView.findViewById(R.id.register_machine_type);
        register_machine_number =(TextView)itemView.findViewById(R.id.register_machine_number);
        btn_modify = (ImageButton)itemView.findViewById(R.id.btn_modify);
        gridView = (GridView) itemView.findViewById(R.id.machinePicgridview);
        register_machine_trademark = (TextView)itemView.findViewById(R.id.register_machine_trademark);
    }
}
