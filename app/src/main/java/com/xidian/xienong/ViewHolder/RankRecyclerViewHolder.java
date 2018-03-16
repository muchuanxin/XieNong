package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xidian.xienong.R;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class RankRecyclerViewHolder extends RecyclerView.ViewHolder{


    public ImageView background;
    public TextView rankName;
    public TextView rankDetail;

    public RankRecyclerViewHolder(View itemView) {
        super(itemView);
        background = (ImageView)itemView.findViewById(R.id.iv_rank_backround);
        rankName = (TextView)itemView.findViewById(R.id.tv_rank_name);
        rankDetail = (TextView)itemView.findViewById(R.id.tv_rank_detail);
    }
}
