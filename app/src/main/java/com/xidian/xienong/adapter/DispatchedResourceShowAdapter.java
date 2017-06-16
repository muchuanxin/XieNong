package com.xidian.xienong.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Resource;

import java.util.List;

public class DispatchedResourceShowAdapter extends BaseAdapter{
	
	private List<Resource> list;
	private Context mContext;
	private Activity activity;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;
	private Resource resource;

	public DispatchedResourceShowAdapter(Context context, List<Resource> list){
		this.mContext = context;
		this.list = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
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
		resource = list.get(position);
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dispatched_resource_show_list_item, null);
			holder.resource_info = (TextView)convertView.findViewById(R.id.tv_dispatched_resource_show);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.resource_info.setText(list.get(position).getMachine().getIdentification_number()+"     "+list.get(position).getDriver().getDriver_name());
		return convertView;
	}

	private static class ViewHolder {
		private TextView resource_info;
	}
	
	public void setSelected(boolean isSelected,int position){
		this.isSelected = isSelected;
		this.position = position;
		notifyDataSetChanged();
	}
	
	public void setPosition(int position){
		this.position = position;
		notifyDataSetChanged();
	}
	

}
