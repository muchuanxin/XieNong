package com.xidian.xienong.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.MachineCategory;

import java.util.List;

public class SortItemAdapter extends BaseAdapter{
	private List<MachineCategory> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public SortItemAdapter(Context context, List<MachineCategory> list){
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.find_choose_list_item, null);
			holder.item = (TextView)convertView.findViewById(R.id.sort_item);
			holder.choosedIv = (ImageView)convertView.findViewById(R.id.iv_choose);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
	
		holder.item.setText(list.get(position).getCategory_name());
		if(this.position == position){
			holder.choosedIv.setBackgroundResource(R.drawable.item_choose);
		}else{
			holder.choosedIv.setBackgroundResource(R.drawable.item_unchoose);
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView item;
		private ImageView choosedIv;
	}
	
	public void setPosition(int position){
		this.position = position;
		notifyDataSetChanged();
	}
	
	


}
