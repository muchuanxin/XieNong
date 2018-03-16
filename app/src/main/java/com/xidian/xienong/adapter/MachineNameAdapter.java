package com.xidian.xienong.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.MachineIdentify;

import java.util.List;

public class MachineNameAdapter extends BaseAdapter{
	
	private List<MachineIdentify> names;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public MachineNameAdapter(Context context, List<MachineIdentify> names){
		this.mContext = context;
		this.names = names;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return names.size();
	}
	@Override
	public Object getItem(int position) {
		return names.get(position);
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
			convertView = mInflater.inflate(R.layout.machine_name_list_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.machine_identify);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		Log.i("kmj","--identify=---" + names.get(position).getIdentify());
		holder.name.setText(names.get(position).getIdentify().toString());

		return convertView;
	}

	private static class ViewHolder {
		private TextView name ;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}

}
