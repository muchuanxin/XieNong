package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.util.CircleImageView;

/**
 * Created by koumiaojuan on 2017/6/16.
 */

public class ConsultRecyclerViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView  title;
    public TextView subTitle;

    public ConsultRecyclerViewHolder(View itemView) {
        super(itemView);
        imageView =(ImageView)itemView.findViewById(R.id.consult_image);
        title =(TextView)itemView.findViewById(R.id.tv_consult_title);
        subTitle =(TextView)itemView.findViewById(R.id.tv_consult_subtitle);
    }
}
