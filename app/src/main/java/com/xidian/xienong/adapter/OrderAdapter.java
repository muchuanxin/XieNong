package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.OrderRecyclerViewHolder;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderRecyclerViewHolder>{

    public LayoutInflater mLayoutInflater;
    public Context mContext = null;
    public List<OrderBean> data;
    public SharePreferenceUtil sp;

    public OrderAdapter(Context mContext, List<OrderBean> data) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.data = data;
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
    }

    @Override
    public OrderRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.order_list_item, parent, false);
        OrderRecyclerViewHolder mViewHolder = new OrderRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderRecyclerViewHolder holder, int position) {
        final OrderBean order= data.get(position);
        if(!sp.getHeadPhoto().equals("") || sp.getHeadPhoto() != null){
            Glide.with(mContext).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.author).into(holder.publisher_photo);
        }
        if(order.getOrderState().equals("待接单") || order.getOrderState().equals("已取消")){
            holder.publisher_name.setText("我");
        }else{
            holder.publisher_name.setText(order.getWorker_name());
        }
        holder.type.setText("农机类型："+order.getMachine_category());
        holder.reservation_time.setText("预约时间："+ order.getReservation_time());

        holder.publisher_time.setText(order.getUpload_time());
        if(order.getOrderState().equals("已完成")){
            holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.orange));
            if(order.isEvaluate() == true){
                holder.iv_receive.setText("已评价");
                holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.green));
            }else{
                holder.iv_receive.setText("待评价");
                holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        }else{
            if(order.getOrderState().equals("已接单")){
                if(order.getAdviceState().equals("0")){
                    holder.iv_receive.setText("已接单");
                }else if(order.getAdviceState().equals("1")){
                    holder.iv_receive.setText("申请取消");
                    holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.qianlan));
                }else if(order.getAdviceState().equals("2")){
                    holder.iv_receive.setText("已同意取消");
                    holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.red));
                }else{
                    holder.iv_receive.setText("已拒绝取消");
                    holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.red));
                }
            }else{
                holder.iv_receive.setText(order.getOrderState());
                holder.iv_receive.setTextColor(mContext.getResources().getColor(R.color.orange));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
