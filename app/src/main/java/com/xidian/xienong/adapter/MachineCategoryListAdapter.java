package com.xidian.xienong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.CroplandType;
import com.xidian.xienong.model.MachineCategory;

import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/7.
 */

public class MachineCategoryListAdapter extends BaseAdapter {
    private List<MachineCategory> list;
    private Context mContext;
    private LayoutInflater mInflater;

    public MachineCategoryListAdapter(Context context, List<MachineCategory> list){
        this.mContext = context;
        this.list = list;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    public void setList(List<MachineCategory> lists) {
        this.list = lists;
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

        MachineCategoryListAdapter.ViewHolder holder;
        if(convertView==null){
            holder = new MachineCategoryListAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.choose_list_item, null);
            holder.itemContent = (TextView)convertView.findViewById(R.id.item_content);
            convertView.setTag(holder);
        }else{
            holder=(MachineCategoryListAdapter.ViewHolder)convertView.getTag();
        }
        holder.itemContent.setText(list.get(position).getCategory_name());
        return convertView;
    }

    private static class ViewHolder {
        private TextView itemContent;
    }
}
