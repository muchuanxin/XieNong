package com.xidian.xienong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Machine;

import java.util.List;

public class DispatchedMachineAdapter extends BaseAdapter{
	
	private List<Machine> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public DispatchedMachineAdapter(Context context, List<Machine> list){
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
			convertView = mInflater.inflate(R.layout.can_dispatched_machine_list_item, null);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
			holder.machine_id = (TextView)convertView.findViewById(R.id.avaliable_machine_id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(this.position == position){
			holder.checkBox.setChecked( isSelected ? true : false );
		}else{
			holder.checkBox.setChecked(false);
		}
		holder.machine_id.setText(list.get(position).getIdentification_number());

		return convertView;
	}

	private static class ViewHolder {
		private CheckBox checkBox ;
		private TextView machine_id;
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
