package com.xidian.xienong.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xidian.xienong.R;

import java.util.List;
import java.util.Map;

/**
 * Created by xinye on 2017/7/25.
 */

public class SelectAddressAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list;

    public SelectAddressAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_select_addr, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.telephone = (TextView) convertView.findViewById(R.id.telephone);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        Map<String, String> map = list.get(position);
        holder.name.setText(map.get("consignee_name"));
        holder.telephone.setText(map.get("phone"));
        if ("1".equals(map.get("is_default"))){
            holder.address.setText(Html.fromHtml("<font color='#FE5400'>[默认地址]</font>"+map.get("area")+" "+map.get("detail")));
        }
        else {
            holder.address.setText(map.get("area")+" "+map.get("detail"));
        }

        return convertView;
    }

    private class ViewHolder{
        TextView name;
        TextView telephone;
        TextView address;
    }
}
