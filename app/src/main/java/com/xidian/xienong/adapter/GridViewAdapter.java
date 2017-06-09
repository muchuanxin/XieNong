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
import com.xidian.xienong.model.MachineImage;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/9.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<MachineImage> images ;
    private Context mContext;

    public GridViewAdapter(List<MachineImage> images, Context mContext) {
        super();
        this.images = images;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (convertView == null) {
            LayoutInflater layout = LayoutInflater.from(mContext);
            convertView = layout.inflate(R.layout.item_ablum_gridview, null);
            holder = new Holder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.item_ablum_gridview_image);
            holder.textviewname = (TextView) convertView.findViewById(R.id.item_ablum_gridview_name);
            holder.textviewcount = (TextView) convertView.findViewById(R.id.item_ablum_gridview_count);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Glide.with(mContext).load(images.get(position).getUrl()).placeholder(R.drawable.empty_picture)
                .into(holder.imageview);
        return convertView;
    }

}

class Holder {
    ImageView imageview;
    TextView textviewname;
    TextView textviewcount;
}
