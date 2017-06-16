package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.GrabOrderRecyclerViewHolder;
import com.xidian.xienong.model.OrderBean;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/12.
 */

public class GrabOrderAdapter extends RecyclerView.Adapter<GrabOrderRecyclerViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OrderAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public LayoutInflater mLayoutInflater;
    public Context mContext = null;
    public List<OrderBean> data;
    public SharePreferenceUtil sp;

    public GrabOrderAdapter(Context mContext, List<OrderBean> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
    }

    @Override
    public GrabOrderRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.grab_order_list_item, parent, false);
        GrabOrderRecyclerViewHolder mViewHolder = new GrabOrderRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final GrabOrderRecyclerViewHolder holder, final int position) {
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
        final OrderBean order= data.get(position);
        if(!order.getHeadphoto().equals("") ||order.getHeadphoto() != null){
            Glide.with(mContext).load(order.getHeadphoto()).centerCrop().placeholder(R.drawable.author).into(holder.farmer_order_photo);
        }
        holder.farmer_order_name.setText(order.getFarmer_name());
        if(order.getOrderState().equals("待接单")){
            holder.farmer_order_code.setText("距我"+order.getDistance()+"km");
            holder.farmer_order_code.setTextColor(mContext.getResources().getColor(R.color.orange));
        }else{
            holder.farmer_order_code.setText(order.getOrderCode());
            holder.farmer_order_code.setTextColor(mContext.getResources().getColor(R.color.gray));
        }

        holder.farmer_order_place.setText(order.getCrop_address());
        holder.farmer_order_type.setText("农机类型："+order.getMachine_category());
        holder.farmer_order_time.setText("预约时间："+ order.getReservation_time());
        holder.farmer_order_cropType.setText("农田类型："+order.getCropland_type());
        holder.farmer_order_cropNumber.setText("农田亩数："+ order.getCropland_number()+"亩");

        if(order.getOrderState().equals("已接单")){
            if(order.getMachines().size()==0){
                if(order.getAdviceState().equals("0")){
                    holder.tv_grab_order.setText("派单");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.verify_corner));
                }else if(order.getAdviceState().equals("1")){
                    holder.tv_grab_order.setText("申请取消");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.blue_corner));
                }else if(order.getAdviceState().equals("2")){
                    holder.tv_grab_order.setText("同意取消");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.red_corner));
                }else{
                    holder.tv_grab_order.setText("拒绝取消");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.red_corner));
                }

            }else{
                if(order.getAdviceState().equals("0")){
                    holder.tv_grab_order.setText("待作业");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.corner));
                }else if(order.getAdviceState().equals("1")){
                    holder.tv_grab_order.setText("申请取消");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.blue_corner));
                }else if(order.getAdviceState().equals("2")){
                    holder.tv_grab_order.setText("同意取消");
                    holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.red_corner));
                }else if(order.getAdviceState().equals("-1")){
                    if(order.getCancleMethod().equals("0")){
                        holder.tv_grab_order.setText("待作业");
                        holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.corner));
                    }else{
                        holder.tv_grab_order.setText("拒绝取消");
                        holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.red_corner));

                    }
                }


            }
        }else if(order.getOrderState().equals("待接单")){
            holder.tv_grab_order.setText("抢单");
            holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.corner));
        }else if(order.getOrderState().equals("作业中")){
            holder.tv_grab_order.setText("作业中");
            holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.corner));
        }else{
            if(order.getCancleReason().equals("")){
                holder.tv_grab_order.setText("已完成");
                holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.gray_corner));
            }else{
                holder.tv_grab_order.setText("已取消");
                holder.tv_grab_order.setBackground(mContext.getResources().getDrawable(R.drawable.gray_corner));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
