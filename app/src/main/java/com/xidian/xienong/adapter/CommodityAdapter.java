package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.CommodityRecyclerViewHolder;
import com.xidian.xienong.model.Commodity;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/30.
 */

public class CommodityAdapter extends RecyclerView.Adapter<CommodityRecyclerViewHolder>{

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
    public List<Commodity> data;

    public CommodityAdapter(Context mContext, List<Commodity> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public CommodityRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.stagged_commodity_list_item, parent, false);
        CommodityRecyclerViewHolder mViewHolder = new CommodityRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final CommodityRecyclerViewHolder holder, final int position) {


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
        Commodity commodity = data.get(position);
        holder.commodityName.setText(commodity.getCommodityName());
        int weiht = (int) Float.parseFloat(commodity.getProduceParameter().getNetWeight());
        holder.price.setText(String.format("%.2f", Double.parseDouble(commodity.getPrice())*Double.parseDouble(commodity.getDiscount()))+"元/"+weiht+"kg");
        if(commodity.getCommodityImageList().size() > 0){

            Glide.with(mContext).load(commodity.getCommodityImageList().get(0).getCommodityImgUrl()).placeholder(R.drawable.empty_picture)
                    .into(holder.commodityImage);
        }

        holder.commodityImage.getLayoutParams().height = (position % 2)*100 + 400;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
