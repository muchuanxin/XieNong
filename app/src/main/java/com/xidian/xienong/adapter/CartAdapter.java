package com.xidian.xienong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.CartCommodity;
import com.xidian.xienong.shoppingmall.brand.CartActivity;
import com.xidian.xienong.util.ToastCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinye on 2017/7/12.
 */

public class CartAdapter extends BaseAdapter {

    private List<CartCommodity> comms;
    private Context context;
    private Map<Integer, TextView> reduce_map;
    private Map<Integer, TextView> plus_map;
    private Map<Integer, TextView> number_map;
    private Map<Integer, Boolean> check_map;

    public CartAdapter(Context context, List<CartCommodity> comms) {
        this.context = context;
        this.comms = comms;
        reduce_map = new HashMap<>();
        plus_map = new HashMap<>();
        number_map = new HashMap<>();
        check_map = new HashMap<>();
        for (int i=0; i<this.comms.size(); i++){
            check_map.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return comms.size();
    }

    @Override
    public CartCommodity getItem(int position) {
        return comms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_cart, null);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
            holder.imageView = (ImageView)convertView.findViewById(R.id.image);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.price = (TextView)convertView.findViewById(R.id.price);
            holder.specification = (TextView)convertView.findViewById(R.id.specification);
            holder.reduce = (TextView)convertView.findViewById(R.id.reduce);
            holder.number = (TextView)convertView.findViewById(R.id.cart_number);
            holder.plus = (TextView)convertView.findViewById(R.id.plus);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        if (reduce_map.get(position) != holder.reduce){
            reduce_map.put(position, holder.reduce);
        }
        if (plus_map.get(position) != holder.plus)
            plus_map.put(position, holder.plus);
        if (number_map.get(position) != holder.number)
            number_map.put(position, holder.number);

        Glide.with(context).load(comms.get(position).getCommodity_pic_url()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.imageView);
        holder.name.setText(comms.get(position).getCommodity_name());
        double discount_price  = comms.get(position).getOrigin_price() * comms.get(position).getDiscount();
        holder.price.setText("￥ "+String.format("%.2f", discount_price));
        holder.specification.setText(comms.get(position).getSpecification());
        holder.number.setText(comms.get(position).getUpdate_quantities()+"");

        CartActivity cartActivity = (CartActivity) context;
        if (cartActivity.isStatus()){
            holder.reduce.setVisibility(View.INVISIBLE);
            holder.plus.setVisibility(View.INVISIBLE);
        }
        else {
            holder.reduce.setVisibility(View.VISIBLE);
            holder.plus.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(check_map.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_map.put(position, isChecked);
                CartActivity cartActivity = (CartActivity) context;
                if (cartActivity.isStatus()){
                    cartActivity.calculateTotalNumber();
                    cartActivity.calculateTotalPrice();
                }
                cartActivity.checkAllCheck();
            }
        });

        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(holder.number.getText().toString());
                if (number>1){
                    holder.number.setText(""+(number-1));
                    comms.get(position).setUpdate_quantities(comms.get(position).getUpdate_quantities()-1);
                }
                else {
                    ToastCustom.makeToast(context, "购买数量不能低于1件");
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(holder.number.getText().toString());
                if (number<comms.get(position).getCurrent_quantities()){
                    holder.number.setText(""+(number+1));
                    comms.get(position).setUpdate_quantities(comms.get(position).getUpdate_quantities()+1);
                }
                else {
                    ToastCustom.makeToast(context, "不能超过最大产品数量");
                }
            }
        });

        return convertView;
    }

    public void showReduceNumberPlus(boolean status){
        if (status){
            for (int key : reduce_map.keySet()){
                reduce_map.get(key).setVisibility(View.VISIBLE);
                plus_map.get(key).setVisibility(View.VISIBLE);
                //number_map.get(key).setVisibility(View.VISIBLE);
            }
        }
        else {
            for (int key : reduce_map.keySet()){
                reduce_map.get(key).setVisibility(View.INVISIBLE);
                plus_map.get(key).setVisibility(View.INVISIBLE);
                //number_map.get(key).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void allCheck(Boolean isChecked){
        for (int i=0; i<this.comms.size(); i++){
            check_map.put(i, isChecked);
        }
        CartAdapter.this.notifyDataSetChanged();
        CartActivity cartActivity = (CartActivity) context;
        if (cartActivity.isStatus()){
            cartActivity.calculateTotalNumber();
            cartActivity.calculateTotalPrice();
        }
    }

    public Map<Integer, Boolean> getCheck_map() {
        return check_map;
    }

    public Map<Integer, TextView> getNumber_map() {
        return number_map;
    }

    private class ViewHolder{
        CheckBox checkBox;
        ImageView imageView;
        TextView name;
        TextView price;
        TextView specification;
        TextView reduce;
        TextView number;
        TextView plus;
    }
}
