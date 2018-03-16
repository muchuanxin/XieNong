package com.xidian.xienong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.MallOrderBean;
import com.xidian.xienong.model.MallOrderBottom;
import com.xidian.xienong.model.MallOrderMiddle;
import com.xidian.xienong.model.MallOrderTop;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.List;

/**
 * Created by MMY on 2017/7/12.
 */

public class MallOrderAdapter extends BaseAdapter {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public MallOrderAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MallOrderAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public LayoutInflater mLayoutInflater;
    public Context context = null;
    public List<MallOrderBean> data;
    public SharePreferenceUtil sp;
    /**
     * Item类型,int值.必须从0开始依次递增.
     * */
    private static final int TYPE_TOP = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_BOTTOM = 2;
    /**
     * Item Type 的数量
     * */
    private static final int TYPE_ITEM_COUNT = 3;


    public MallOrderAdapter(Context context, List<MallOrderBean> data) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        sp = new SharePreferenceUtil(context, Constants.SAVE_USER);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopViewHolder topViewHolder=null;
        MiddleViewHolder middleViewHolder=null;
        BottomViewHolder bottomViewHolder=null;
        switch (getItemViewType(position)){
            case TYPE_TOP:
                topViewHolder=new TopViewHolder();
                if(convertView==null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.mall_order_top,null);
                    //convertView = View.inflate(context, R.layout.mall_order_top,null);
                    topViewHolder.tv_time=(TextView)convertView.findViewById(R.id.mall_order_generate_time);
                    topViewHolder.tv_order_status=(TextView)convertView.findViewById(R.id.mall_order_status);
                    convertView.setTag(topViewHolder);
                }else{
                    topViewHolder=(TopViewHolder)convertView.getTag();
                }
               // topViewHolder.tv_order_status.setText(data.get(position).getOrder_status());
                topViewHolder.tv_time.setText("下单时间："+((MallOrderTop)data.get(position)).getOrder_generated_time());
                topViewHolder.tv_order_status.setText(((MallOrderTop)data.get(position)).getOrder_status());
                break;
            case TYPE_CONTENT:
                middleViewHolder=new MiddleViewHolder();
                if(convertView==null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.mall_order_list_item,null);
                   // convertView = View.inflate(context, R.layout.mall_order_list_item,null);
                    middleViewHolder.iv_comm_image = (ImageView)convertView.findViewById(R.id.mall_order_image);
                    middleViewHolder.tv_comm_name = (TextView)convertView.findViewById(R.id.mall_order_comm_name);
                    middleViewHolder.tv_unit_price = (TextView)convertView.findViewById(R.id.mall_order_unit_price);
                    middleViewHolder.tv_comm_standard=(TextView)convertView.findViewById(R.id.mall_order_comm_standard);
                    middleViewHolder.tv_every_comm_number = (TextView)convertView.findViewById(R.id.mall_order_every_comm_number);
                    middleViewHolder.dividerView = (View)convertView.findViewById(R.id.divider_view);
                    convertView.setTag(middleViewHolder);
                }else{
                    middleViewHolder=(MiddleViewHolder) convertView.getTag();
                }
                Glide.with(context).load(((MallOrderMiddle)data.get(position)).getImage_url()).centerCrop().placeholder(R.drawable.empty_picture).into(middleViewHolder.iv_comm_image);
                middleViewHolder.tv_comm_name.setText(((MallOrderMiddle)data.get(position)).getCommodityName());
                middleViewHolder.tv_unit_price.setText("¥"+((MallOrderMiddle)data.get(position)).getPrice());
                middleViewHolder.tv_comm_standard.setText(((MallOrderMiddle)data.get(position)).getSpecification());
                middleViewHolder.tv_every_comm_number.setText("×"+((MallOrderMiddle)data.get(position)).getBuy_number());
                if (position==data.size()-2){
                    middleViewHolder.dividerView.setVisibility(View.GONE);
                }else
                    middleViewHolder.dividerView.setVisibility(View.VISIBLE);
                break;
            case TYPE_BOTTOM:
                bottomViewHolder=new BottomViewHolder();
                if(convertView==null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.mall_order_bottom,null);
                    //convertView = View.inflate(context, R.layout.mall_order_bottom,null);
                    bottomViewHolder.tv_all_comm_number=(TextView)convertView.findViewById(R.id.mall_order_comm_number);
                    bottomViewHolder.tv_trans_price=(TextView)convertView.findViewById(R.id.mall_order_tran_price);
                    bottomViewHolder.tv_total_money=(TextView)convertView.findViewById(R.id.mall_order_total_money);
                    bottomViewHolder.tv_include_tran_money=(TextView)convertView.findViewById(R.id.mall_order_include_tran_price) ;
                    bottomViewHolder.bt_first = (TextView) convertView.findViewById(R.id.mall_order_delete_1);
                    bottomViewHolder.bt_second = (TextView) convertView.findViewById(R.id.mall_order_trans_2);
                    bottomViewHolder.bt_third = (TextView) convertView.findViewById(R.id.mall_order_evaluate_3);
                    bottomViewHolder.ll_button = (LinearLayout)convertView.findViewById(R.id.ll_button);
                    convertView.setTag(bottomViewHolder);
                }else{
                    bottomViewHolder=(BottomViewHolder)convertView.getTag();
                }

                bottomViewHolder.tv_all_comm_number.setText(((MallOrderBottom)data.get(position)).getComm_number());
                bottomViewHolder.tv_trans_price.setText("¥"+((MallOrderBottom)data.get(position)).getTransport_money());
                bottomViewHolder.tv_total_money.setText("¥"+((MallOrderBottom)data.get(position)).getTotal_money());
                bottomViewHolder.tv_include_tran_money.setText("(含运费"+((MallOrderBottom)data.get(position)).getTransport_money()+")");
                String order_status=((MallOrderBottom)data.get(position)).getStatus();
                boolean allIsEvaluated= ((MallOrderBottom)data.get(position)).isEvalued();

                if(order_status.equals("待付款")){
                    bottomViewHolder.ll_button.setVisibility(View.VISIBLE);
                    bottomViewHolder.bt_first.setVisibility(View.GONE);
                    bottomViewHolder.bt_second.setText("取消订单");
                    bottomViewHolder.bt_third.setText("付款");
                }else if(order_status.equals("待发货")||order_status.equals("交易关闭")){
                    bottomViewHolder.ll_button.setVisibility(View.GONE);
                }
                else if(order_status.equals("待收货")){
                    bottomViewHolder.ll_button.setVisibility(View.VISIBLE);
                    bottomViewHolder.bt_first.setVisibility(View.GONE);
                    bottomViewHolder.bt_second.setVisibility(View.GONE);
                    bottomViewHolder.bt_third.setText("查看物流");
                }else if(order_status.equals("交易成功")){
                    if(allIsEvaluated){
                        bottomViewHolder.ll_button.setVisibility(View.VISIBLE);
                        bottomViewHolder.bt_first.setVisibility(View.GONE);
                        bottomViewHolder.bt_second.setText("删除订单");
                        bottomViewHolder.bt_third.setText("查看物流");
                    }else{
                        bottomViewHolder.ll_button.setVisibility(View.VISIBLE);
                        bottomViewHolder.bt_first.setText("删除订单");
                        bottomViewHolder.bt_second.setText("查看物流");
                        bottomViewHolder.bt_third.setText("评价");
                    }

                }
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getItemType()==0){
            return TYPE_TOP;
        }else if(data.get(position).getItemType()==1){
            return TYPE_CONTENT;
        }else
            return TYPE_BOTTOM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_ITEM_COUNT;
    }

    private class TopViewHolder{
        TextView tv_time;
        TextView tv_order_status;
    }

    private class MiddleViewHolder{
        public ImageView iv_comm_image;
        public TextView tv_comm_name,tv_unit_price,tv_comm_standard,tv_every_comm_number;
        View dividerView;
    }

    private class BottomViewHolder{
        public TextView tv_trans_price,tv_all_comm_number,tv_total_money,tv_include_tran_money;
        public TextView bt_first,bt_second,bt_third;
        LinearLayout ll_button;
    }

}

