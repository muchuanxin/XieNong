package com.xidian.xienong.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Machine;

import java.util.List;

public class MachineUsedAdapter  extends BaseAdapter{
	
	private List<Machine> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public MachineUsedAdapter(Context context, List<Machine> list){
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
			convertView = mInflater.inflate(R.layout.machine_used_list_item, null);
			//holder.isUsed=(CheckBox)convertView.findViewById(R.id.isUsed);
			holder.machine_number = (TextView)convertView.findViewById(R.id.machine_number);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
	
		//holder.isUsed.setChecked(list.get(position).getIsUsed() == true ? true : false );
	
		holder.machine_number.setText(list.get(position).getMachineIdentification());
        Log.i("kmj","----machine_number----" + holder.machine_number.getText());

		return convertView;
	}

	private static class ViewHolder {
		//private CheckBox isUsed ;
		private TextView machine_number;
	}
	
	public void setSelected(boolean isSelected,int position){
		this.isSelected = isSelected;
		this.position = position;
		notifyDataSetChanged();
	}
	

}
