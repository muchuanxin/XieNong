package com.xidian.xienong.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xidian.xienong.R;

/**
 * Created by koumiaojuan on 2017/7/28.
 */

public class MallButtomOrderRecyclerViewHolder extends RecyclerView.ViewHolder{


    public LinearLayout ll_button;
    public TextView transport_money;
    public TextView total_commodity_number;
    public TextView total_money;
    public TextView include_tran_price;
    public TextView bt_first; //detele /cancle
    public TextView bt_second;
    public TextView bt_third;



    public MallButtomOrderRecyclerViewHolder(View itemView) {
        super(itemView);


        //bottom

        this.ll_button = (LinearLayout)itemView.findViewById(R.id.ll_button);
        this.transport_money = (TextView)itemView.findViewById(R.id.mall_order_tran_price);
        this.total_commodity_number = (TextView)itemView.findViewById(R.id.mall_order_comm_number);
        this.total_money =  (TextView)itemView.findViewById(R.id.mall_order_total_money);
        this.include_tran_price =  (TextView)itemView.findViewById(R.id.mall_order_include_tran_price);
        this.bt_first = (TextView)itemView.findViewById(R.id.mall_order_delete_1);
        this.bt_second = (TextView)itemView.findViewById(R.id.mall_order_trans_2);
        this.bt_third =  (TextView)itemView.findViewById(R.id.mall_order_evaluate_3);

    }


}
