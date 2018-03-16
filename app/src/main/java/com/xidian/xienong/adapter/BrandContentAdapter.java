package com.xidian.xienong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.FirstInfo;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/29.
 */

public class BrandContentAdapter extends BaseAdapter {

    private List<FirstInfo> brands;
    private Context context;
    private LayoutInflater mInflater;
    private int position=0;

    public BrandContentAdapter(List<FirstInfo> brands, Context context) {
        this.brands = brands;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return brands.size();
    }

    @Override
    public Object getItem(int position) {
        return brands.get(position);
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
            convertView = mInflater.inflate(R.layout.brand_gridview_item, parent, false);
            holder.brandName = (TextView) convertView.findViewById(R.id.tv_gride_brandname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.brandName.setText(brands.get(position).getFirstName());
        if(this.position == position){
            holder.brandName.setBackgroundResource(R.drawable.green_corner);
        }else{
            holder.brandName.setBackgroundResource(R.drawable.transparent_corner);
        }
        return convertView;
    }

    public void setSelected(int position){
        this.position = position;
        notifyDataSetChanged();
    }



    class ViewHolder {
       TextView brandName;
    }

}
