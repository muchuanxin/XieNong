package com.xidian.xienong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Driver;

import java.util.List;

public class DriverUsedAdapter extends BaseAdapter{
	
	private List<Driver> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public DriverUsedAdapter(Context context, List<Driver> list){
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

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.driver_used_list_item, null);
			holder.driverName = (TextView)convertView.findViewById(R.id.list_driver_name);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
	
		//holder.isUsed.setChecked(list.get(position).isUsed() == true ? true : false );
		holder.driverName.setText(list.get(position).getDriver_name());

		return convertView;
	}

	private static class ViewHolder {
		private CheckBox isUsed ;
		private TextView driverName;
	}

}
