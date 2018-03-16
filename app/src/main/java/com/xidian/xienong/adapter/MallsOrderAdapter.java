package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.MallButtomOrderRecyclerViewHolder;
import com.xidian.xienong.ViewHolder.MallContentOrderRecyclerViewHolder;
import com.xidian.xienong.ViewHolder.MallTopOrderRecyclerViewHolder;
import com.xidian.xienong.model.MallOrderBean;
import com.xidian.xienong.model.MallOrderBottom;
import com.xidian.xienong.model.MallOrderMiddle;
import com.xidian.xienong.model.MallOrderTop;
//import com.xidian.xienong.model.MallsOrderBean;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class MallsOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Item类型,int值.必须从0开始依次递增.
     * */
    int type = 0;
    private static final int TYPE_TOP = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_BOTTOM = 2;




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
   // public List<MallsOrderBean> data;
    public List<MallOrderBean>  data2;
    public SharePreferenceUtil sp;

   /* public MallsOrderAdapter(Context mContext, List<MallsOrderBean> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
    }*/
    public MallsOrderAdapter(Context mContext, List<MallOrderBean> data) {
        this.mContext = mContext;
        this.data2 = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = null;
        RecyclerView.ViewHolder v = null;
        if(viewType == TYPE_TOP){
            mView = mLayoutInflater.inflate(R.layout.mall_order_top, parent, false);
            v = new MallTopOrderRecyclerViewHolder(mView);
        }else if(viewType == TYPE_CONTENT){
            mView = mLayoutInflater.inflate(R.layout.mall_order_list_item, parent, false);
            v = new MallContentOrderRecyclerViewHolder(mView);
        }else{
            mView = mLayoutInflater.inflate(R.layout.mall_order_bottom, parent, false);
            v = new MallButtomOrderRecyclerViewHolder(mView);
        }
        return v;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
//        List<MallOrderBean> tempOrders = data.get(position).getMallTypeOrders();

        if (holder instanceof MallTopOrderRecyclerViewHolder) {
            /*((MallTopOrderRecyclerViewHolder) holder).order_generated_time.setText(tempOrders.get(0).getOrder_generated_time());
            ((MallTopOrderRecyclerViewHolder) holder).order_status.setText(tempOrders.get(0).getOrder_status());*/
            ((MallTopOrderRecyclerViewHolder) holder).order_generated_time.setText("下单时间："+((MallOrderTop)data2.get(position)).getOrder_generated_time());
            ((MallTopOrderRecyclerViewHolder) holder).order_status.setText(((MallOrderTop)data2.get(position)).getOrder_status());
        } else if (holder instanceof MallContentOrderRecyclerViewHolder) {
            /*for(int i=1;i<tempOrders.size()-1;i++){
                Glide.with(mContext).load(tempOrders.get(position).getImage_url()).centerCrop().placeholder(R.drawable.empty_picture).into(((MallContentOrderRecyclerViewHolder) holder).commodity_image);
                ((MallContentOrderRecyclerViewHolder) holder).commodity_name.setText(tempOrders.get(i).getCommodityName());
                ((MallContentOrderRecyclerViewHolder) holder).commodity_number.setText(tempOrders.get(i).getBuy_number());
                ((MallContentOrderRecyclerViewHolder) holder).unit_price.setText(tempOrders.get(i).getPrice());
                ((MallContentOrderRecyclerViewHolder) holder).commodity_standard.setText(tempOrders.get(i).getSpecification());
            }*/
            Glide.with(mContext).load(((MallOrderMiddle)data2.get(position)).getImage_url()).centerCrop().placeholder(R.drawable.empty_picture).into(((MallContentOrderRecyclerViewHolder) holder).commodity_image);
            ((MallContentOrderRecyclerViewHolder) holder).commodity_name.setText(((MallOrderMiddle)data2.get(position)).getCommodityName());
            ((MallContentOrderRecyclerViewHolder) holder).commodity_number.setText("×"+((MallOrderMiddle)data2.get(position)).getBuy_number());
            ((MallContentOrderRecyclerViewHolder) holder).unit_price.setText("¥"+((MallOrderMiddle)data2.get(position)).getPrice());
            ((MallContentOrderRecyclerViewHolder) holder).commodity_standard.setText(((MallOrderMiddle)data2.get(position)).getSpecification());
            /*if (position==data2.size()-2){
                ((MallContentOrderRecyclerViewHolder) holder).dividerView.setVisibility(View.GONE);
            }else
                ((MallContentOrderRecyclerViewHolder) holder).dividerView.setVisibility(View.VISIBLE);*/
        }else{
           //......
            /*((MallButtomOrderRecyclerViewHolder) holder).total_commodity_number.setText(((MallOrderBottom)tempOrders.get(tempOrders.size()-1)).getComm_number());
            ((MallButtomOrderRecyclerViewHolder) holder).transport_money.setText("¥"+((MallOrderBottom)tempOrders.get(tempOrders.size()-1)).getTransport_money());
            ((MallButtomOrderRecyclerViewHolder) holder).total_money.setText("¥"+((MallOrderBottom) tempOrders.get(tempOrders.size()-1)).getTotal_money());
            ((MallButtomOrderRecyclerViewHolder) holder).include_tran_price.setText("(含运费"+tempOrders.get(tempOrders.size()-1).getTransport_money()+")");
            String order_status=((MallOrderBottom)tempOrders.get(tempOrders.size()-1)).getStatus();
            boolean allIsEvaluated= ((MallOrderBottom)tempOrders.get(tempOrders.size()-1)).isEvalued();*/
            ((MallButtomOrderRecyclerViewHolder) holder).total_commodity_number.setText(((MallOrderBottom)data2.get(position)).getComm_number());
            ((MallButtomOrderRecyclerViewHolder) holder).transport_money.setText("¥"+((MallOrderBottom)data2.get(position)).getTransport_money());
            ((MallButtomOrderRecyclerViewHolder) holder).total_money.setText(("¥"+((MallOrderBottom)data2.get(position)).getTotal_money()));
            ((MallButtomOrderRecyclerViewHolder) holder).include_tran_price.setText("(含运费"+((MallOrderBottom)data2.get(position)).getTransport_money()+")");
            String order_status=((MallOrderBottom)data2.get(position)).getStatus();
            boolean allIsEvaluated= ((MallOrderBottom)data2.get(position)).isEvalued();
            if(order_status.equals("待付款")){
                ((MallButtomOrderRecyclerViewHolder) holder).ll_button.setVisibility(View.VISIBLE);
                ((MallButtomOrderRecyclerViewHolder) holder).bt_first.setVisibility(View.GONE);
                ((MallButtomOrderRecyclerViewHolder) holder).bt_second.setText("取消订单");
                ((MallButtomOrderRecyclerViewHolder) holder).bt_third.setText("付款");
            }else if(order_status.equals("待发货")||order_status.equals("交易关闭")){
                ((MallButtomOrderRecyclerViewHolder) holder).ll_button.setVisibility(View.GONE);
            }
            else if(order_status.equals("待收货")){
                ((MallButtomOrderRecyclerViewHolder) holder).ll_button.setVisibility(View.VISIBLE);
                ((MallButtomOrderRecyclerViewHolder) holder).bt_first.setVisibility(View.GONE);
                ((MallButtomOrderRecyclerViewHolder) holder).bt_second.setVisibility(View.GONE);
                ((MallButtomOrderRecyclerViewHolder) holder).bt_third.setText("查看物流");
            }else if(order_status.equals("交易成功")){
                if(allIsEvaluated){
                    ((MallButtomOrderRecyclerViewHolder) holder).ll_button.setVisibility(View.VISIBLE);
                    ((MallButtomOrderRecyclerViewHolder) holder).bt_first.setVisibility(View.GONE);
                    ((MallButtomOrderRecyclerViewHolder) holder).bt_second.setText("删除订单");
                    ((MallButtomOrderRecyclerViewHolder) holder).bt_third.setText("查看物流");
                }else{
                    ((MallButtomOrderRecyclerViewHolder) holder).ll_button.setVisibility(View.VISIBLE);
                    ((MallButtomOrderRecyclerViewHolder) holder).bt_first.setText("删除订单");
                    ((MallButtomOrderRecyclerViewHolder) holder).bt_second.setText("查看物流");
                    ((MallButtomOrderRecyclerViewHolder) holder).bt_third.setText("评价");
                }

            }

        }

    }


    @Override
    public int getItemCount() {
        return data2.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


   /* @Override
    public int getItemViewType(int position) {
        List<MallOrderBean> list = data.get(position).getMallTypeOrders();

        for(MallOrderBean orderBean : list){
            if(orderBean.getItemType() == 0){
                Log.e("www","orderBean.getItemType() == 0");
                type =  TYPE_TOP;
            }else if(orderBean.getItemType() == 1){
                Log.e("www","orderBean.getItemType() == 1");
                type = TYPE_CONTENT;
            }else{
                Log.e("www","orderBean.getItemType() == 2");
                type = TYPE_BOTTOM;
            }
        }
        return type ;
    }*/
    @Override
    public int getItemViewType(int position) {
        //public List<MallOrderBean> data;
       /*// if(data.get(position).getItemType()==0){
        List<MallOrderBean> list = data.get(position).getMallTypeOrders();

        if(list.get(position).getItemType() == 0){
            return TYPE_TOP ;
        }else if(list.get(position).getItemType() == 1){
            return  TYPE_CONTENT;
        }else{
            return  TYPE_BOTTOM;
        }*/

        if(data2.get(position).getItemType()==0){
            return TYPE_TOP;
        }else if(data2.get(position).getItemType()==1){
            return TYPE_CONTENT;
        }else{
            return TYPE_BOTTOM;
        }


    }

}
