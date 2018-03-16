package com.xidian.xienong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.Commodity;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/7/5.
 */

public class HotCommodityAdapter extends BaseAdapter {
    private List<Commodity> commodities ;
    private Context mContext;

    public HotCommodityAdapter(List<Commodity> commodities, Context mContext) {
        super();
        this.commodities = commodities;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commodities.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return commodities.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        HotCommodityHolder holder = null;
        if (convertView == null) {
            LayoutInflater layout = LayoutInflater.from(mContext);
            convertView = layout.inflate(R.layout.hot_commodity_grid_item, null);
            holder = new HotCommodityHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.iv_hot_commodity_url);
            holder.textviewname = (TextView) convertView.findViewById(R.id.tv_hot_commodity_name);
            holder.textviewdes = (TextView) convertView.findViewById(R.id.tv_hot_commodity_des);
            convertView.setTag(holder);
        } else {
            holder = (HotCommodityHolder) convertView.getTag();
        }


        Glide.with(mContext).load(commodities.get(position).getCommodityImageList().get(0).getCommodityImgUrl()).placeholder(R.drawable.empty_picture)
                .into(holder.imageview);
        holder.textviewname.setText(commodities.get(position).getCommodityName());
        holder.textviewdes.setText(commodities.get(position).getCommodityDescription());
        return convertView;
    }

}

class HotCommodityHolder {
    ImageView imageview;
    TextView textviewname;
    TextView textviewdes;
}

