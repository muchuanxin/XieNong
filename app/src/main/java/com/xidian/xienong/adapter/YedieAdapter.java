package com.xidian.xienong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.CartCommodity;

import java.util.List;

/**
 * Created by xinye on 2017/7/14.
 */

public class YedieAdapter extends BaseAdapter {

    private Context context;
    private List<CartCommodity> list;

    public YedieAdapter(Context context, List<CartCommodity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CartCommodity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_order, null);
            holder.imageView = (ImageView)convertView.findViewById(R.id.picture);
            holder.name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.price = (TextView)convertView.findViewById(R.id.tv_price);
            holder.number = (TextView)convertView.findViewById(R.id.tv_number);
            holder.one_total = (TextView)convertView.findViewById(R.id.one_total);
            holder.one_number = (TextView)convertView.findViewById(R.id.one_number);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        Glide.with(context).load(list.get(position).getCommodity_pic_url()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.imageView);
        holder.name.setText(list.get(position).getCommodity_name());
        double discount_price  = list.get(position).getOrigin_price() * list.get(position).getDiscount();
        holder.price.setText("￥"+String.format("%.2f", discount_price)+"元");
        holder.number.setText("x"+list.get(position).getAdded_total_quantities());
        holder.one_total.setText("共"+list.get(position).getAdded_total_quantities()+"件商品");
        holder.one_number.setText("小计：￥"+String.format("%.2f", discount_price * list.get(position).getAdded_total_quantities())+"元");

        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView price;
        TextView number;
        TextView one_total;
        TextView one_number;
    }
}
