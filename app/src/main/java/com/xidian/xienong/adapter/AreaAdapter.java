package com.xidian.xienong.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.AreaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SoBan
 * @create 2016/12/6 18:15.
 */
public class AreaAdapter extends BaseAdapter {

    private final Context mContext;
    private int lastPosition;
    private List<AreaInfo> mList = new ArrayList<>();

    public AreaAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<AreaInfo> list) {
        if (list != null && list.size() > 0) {
            mList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AreaInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_area_list, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_area);
            convertView.setTag(holder);
        } else if (convertView.getParent() != null) {
            ((ViewGroup) convertView.getParent()).removeView(convertView);
        }
        holder = (ViewHolder) convertView.getTag();
        AreaInfo item = mList.get(position);
        holder.textView.setText(item.getFullname());
        if (lastPosition < position && lastPosition != 0) {
            ObjectAnimator.ofFloat(convertView, "translationY", convertView.getHeight() * 2, 0).setDuration(500).start();
        }
        lastPosition = position;
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
