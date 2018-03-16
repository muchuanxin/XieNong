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
import com.xidian.xienong.model.MallOrderMiddle;

import java.util.List;

/**
 * Created by MMY on 2017/7/14.
 */

public class OneMallOrderListItem extends BaseAdapter {

    private List<MallOrderMiddle> list;
    private Context mContext;
    private LayoutInflater mInflater;
    private int position=0;

    public OneMallOrderListItem(List<MallOrderMiddle> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.one_mall_order_list_item, null);
            holder.iv_photo=(ImageView)convertView.findViewById(R.id.mall_order_image_one);
            holder.tv_name = (TextView)convertView.findViewById(R.id.mall_order_comm_name_one);
            holder.tv_unit_price = (TextView)convertView.findViewById(R.id.mall_order_unit_price_one);
            holder.tv_specification = (TextView) convertView.findViewById(R.id.mall_order_comm_standard_one);
            holder.tv_number = (TextView) convertView.findViewById(R.id.mall_order_every_comm_number_one);
            holder.view_divider = (View) convertView.findViewById(R.id.view_divider);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getCommodityName());
        holder.tv_unit_price.setText("¥"+list.get(position).getPrice());
        holder.tv_specification.setText(list.get(position).getSpecification());
        holder.tv_number.setText("×"+list.get(position).getBuy_number());
		Glide.with(mContext).load(list.get(position).getImage_url()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.iv_photo);
        if(position==list.size()-1){
            holder.view_divider.setVisibility(View.GONE);
        }else{
            holder.view_divider.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private ImageView iv_photo;
        private TextView tv_name;
        private TextView tv_unit_price;
        private TextView tv_specification;
        private TextView tv_number;
        private View view_divider;

    }

}
