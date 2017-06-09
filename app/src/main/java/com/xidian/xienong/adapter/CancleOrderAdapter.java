package com.xidian.xienong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.CancleReason;

import java.util.List;

public class CancleOrderAdapter extends BaseAdapter{
	
	private List<CancleReason> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public CancleOrderAdapter(Context context, List<CancleReason> list){
		this.mContext = context;
		this.list = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	public void setList(List<CancleReason> lists) {
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
			convertView = mInflater.inflate(R.layout.cancle_order_list_item, null);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.cancle_order_checkBox);
			holder.reason = (TextView)convertView.findViewById(R.id.cancle_order_reason);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(this.position == position){
			holder.checkBox.setChecked( isSelected ? true : false );
			this.position = position;
		}else{
			holder.checkBox.setChecked(false);
		}
		holder.reason.setText(list.get(position).getReason());

		return convertView;
	}

	private static class ViewHolder {
		private CheckBox checkBox ;
		private TextView reason;
	}
	
	public void setSelected(boolean isSelected,int position){
		this.isSelected = isSelected;
		this.position = position;
		notifyDataSetChanged();
	}
	
	public int getSelected(){
		return this.position;
	}


}
