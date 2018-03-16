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
import com.xidian.xienong.model.SecondContent;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/7/6.
 */

public class SecondContentAdapter extends BaseAdapter {

    private List<SecondContent> secondContents ;
    private Context mContext;

    public SecondContentAdapter(List<SecondContent> secondContents, Context mContext) {
        super();
        this.secondContents = secondContents;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return secondContents.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return secondContents.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        SecondContentHolder holder = null;
        if (convertView == null) {
            LayoutInflater layout = LayoutInflater.from(mContext);
            convertView = layout.inflate(R.layout.class_second_content_gride_item, null);
            holder = new SecondContentHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.ib_pic);
            holder.textviewSecond = (TextView) convertView.findViewById(R.id.tv_second_name);
            convertView.setTag(holder);
        } else {
            holder = (SecondContentHolder) convertView.getTag();
        }

        if(secondContents.get(position).getCommodities().get(0).getCommodityImageList().size() > 0){
            Glide.with(mContext).load(secondContents.get(position).getCommodities().get(0).getCommodityImageList().get(0).getCommodityImgUrl()).placeholder(R.drawable.empty_picture)
                    .into(holder.imageview);
        }

        holder.textviewSecond.setText(secondContents.get(position).getSecondCategory());
        return convertView;
    }

}

class SecondContentHolder {
    ImageView imageview;
    TextView textviewSecond;
}
