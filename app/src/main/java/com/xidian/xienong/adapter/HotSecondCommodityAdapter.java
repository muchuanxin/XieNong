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
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.SecondContent;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/7/6.
 */

public class HotSecondCommodityAdapter extends BaseAdapter {
    private List<SecondContent> secondContents ;
    private Context mContext;

    public HotSecondCommodityAdapter(List<SecondContent> secondContents, Context mContext) {
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
        HotSecondCommodityHolder holder = null;
        if (convertView == null) {
            LayoutInflater layout = LayoutInflater.from(mContext);
            convertView = layout.inflate(R.layout.hot_second_commodity_gride_item, null);
            holder = new HotSecondCommodityHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.iv_second_pic);
            holder.textviewSecond = (TextView) convertView.findViewById(R.id.tv_second_commodity);
            holder.textviewdes = (TextView) convertView.findViewById(R.id.tv_second_des);
            convertView.setTag(holder);
        } else {
            holder = (HotSecondCommodityHolder) convertView.getTag();
        }

        if(secondContents.get(position).getCommodities().get(0).getCommodityImageList().size() > 0){
            Glide.with(mContext).load(secondContents.get(position).getCommodities().get(0).getCommodityImageList().get(0).getCommodityImgUrl()).placeholder(R.drawable.empty_picture)
                    .into(holder.imageview);
        }

        if(secondContents.get(position).getSecondCategory().equals("暂无")){
            holder.textviewSecond.setText(secondContents.get(position).getCommodities().get(0).getCommodityName());
            holder.textviewdes.setText("预售");
        }else{
            holder.textviewSecond.setText(secondContents.get(position).getSecondCategory());
            holder.textviewdes.setText("月销"+secondContents.get(position).getSaleCount()+"笔");
        }

        return convertView;
    }

}

class HotSecondCommodityHolder {
    ImageView imageview;
    TextView textviewSecond;
    TextView textviewdes;
}
