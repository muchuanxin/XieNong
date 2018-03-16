package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.RankRecyclerViewHolder;
import com.xidian.xienong.home.HomePageActivity;
import com.xidian.xienong.model.Rank;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class RankAdapter extends RecyclerView.Adapter<RankRecyclerViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public LayoutInflater mLayoutInflater;
    public Context mContext = null;
    public List<Rank> data;
    public SharePreferenceUtil sp;
//    private int[] images = {R.mipmap.strawberries,R.mipmap.yingtao,R.mipmap.zaguo,R.mipmap.orange};

    public RankAdapter(Context mContext, List<Rank> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
    }

    @Override
    public RankRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.rank_recycler_item, parent, false);
        RankRecyclerViewHolder mViewHolder = new RankRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RankRecyclerViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
        Rank rank = data.get(position);
        holder.rankName.setText(rank.getRankRule());
        holder.rankDetail.setText(rank.getRankDetail());
        Glide.with(mContext).load(rank.getImgUrl()).centerCrop().into(  holder.background);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
