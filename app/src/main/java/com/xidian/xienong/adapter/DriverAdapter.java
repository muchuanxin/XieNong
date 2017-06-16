package com.xidian.xienong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.Driver;

import java.util.List;

public class DriverAdapter extends BaseAdapter{
	
	private List<Driver> list;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;

	public DriverAdapter(Context context, List<Driver> list){
		this.mContext = context;
		this.list = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	public void setList(List<Driver> lists) {
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
			convertView = mInflater.inflate(R.layout.horizontal_driver_list_item, null);
			holder.name = (TextView)convertView.findViewById(R.id.list_driver_name);
			holder.tele = (TextView)convertView.findViewById(R.id.list_driver_tele);
			holder.photo = (ImageView)convertView.findViewById(R.id.driver_image);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.name.setText(list.get(position).getDriver_name());
		holder.tele.setText(list.get(position).getDriver_telephone());
//		Glide.with(mContext).load(list.get(position).getPhotoUrl()).centerCrop().placeholder(R.drawable.author).into(holder.photo);

		return convertView;
	}

	private static class ViewHolder {
		private TextView name;
		private TextView tele;
		private ImageView photo;
	}

}
