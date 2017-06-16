package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.ConsultRecyclerViewHolder;
import com.xidian.xienong.model.Consult;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/16.
 */

public class ConsultAdapter extends RecyclerView.Adapter<ConsultRecyclerViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public MachineAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MachineAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private Context mContext = null;
    private List<Consult> data;
    private SharePreferenceUtil sp;
    public LayoutInflater mLayoutInflater;

    public ConsultAdapter(Context mContext, List<Consult> data) {
        this.mContext = mContext;
        this.data = data;
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ConsultRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.consult_item, parent, false);
        ConsultRecyclerViewHolder mViewHolder = new ConsultRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ConsultRecyclerViewHolder holder, final int position) {
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
        final Consult consult= data.get(position);
        Glide.with(mContext).load(consult.getImageUrl()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.imageView);
        holder.title.setText(consult.getTitle());
        holder.subTitle.setText(consult.getSubTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
