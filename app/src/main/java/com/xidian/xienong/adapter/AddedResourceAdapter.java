package com.xidian.xienong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Resource;

import java.util.List;

public class AddedResourceAdapter extends BaseAdapter{
	
	private List<Resource> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public AddedResourceAdapter(Context context, List<Resource> list){
		this.mContext = context;
		this.list = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	public void setList(List<Resource> lists) {
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

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.added_machine_driver_list_item, null);
			holder.machineName = (TextView)convertView.findViewById(R.id.added_machine_name);
			holder.name = (TextView)convertView.findViewById(R.id.added_driver_name);
			holder.tele = (TextView)convertView.findViewById(R.id.added_driver_tele);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.machineName.setText(list.get(position).getMachine().getIdentification_number());
		holder.name.setText(list.get(position).getDriver().getDriver_name());
		holder.tele.setText(list.get(position).getDriver().getDriver_telephone());

		return convertView;
	}

	private static class ViewHolder {
		private TextView machineName;
		private TextView name;
		private TextView tele;
	}

}
