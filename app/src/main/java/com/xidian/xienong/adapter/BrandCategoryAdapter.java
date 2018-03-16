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
import com.xidian.xienong.model.CommodityImage;
import com.xidian.xienong.model.SecondContent;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/29.
 */

public class BrandCategoryAdapter extends BaseAdapter {

    private List<SecondContent> list;
    private Context mContext;
    private LayoutInflater mInflater;

    public BrandCategoryAdapter(List<SecondContent> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.second_content_grid_item, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.content_pic);
            holder.contentName = (TextView)convertView.findViewById(R.id.content_name);
            holder.contentDetail = (TextView)convertView.findViewById(R.id.content_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


//
        holder.contentName.setText(list.get(position).getSecondCategory());
        holder.contentDetail.setText("月销"+list.get(position).getSaleCount()+"笔");
        if(list.get(position).getCommodities().size()>0){
            List<CommodityImage> images = list.get(position).getCommodities().get(0).getCommodityImageList();
            if(images.size()>0) {
                Glide.with(mContext).load(images.get(0).getCommodityImgUrl()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.image);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView contentName;
        TextView contentDetail;

    }
}
