package com.xidian.xienong.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.Worker;

import java.util.List;

public class WorkerAdapter extends BaseAdapter{
	
	private List<Worker> list;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public WorkerAdapter(Context context, List<Worker> list){
		this.mContext = context;
		this.list = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	public void setList(List<Worker> lists) {
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
		Worker worker = list.get(position);
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.find_machine_list_item, null);
			holder.image = (ImageView)convertView.findViewById(R.id.item_image);
			holder.name = (TextView)convertView.findViewById(R.id.item_name);
			holder.type = (TextView)convertView.findViewById(R.id.item_type);
			holder.number = (TextView)convertView.findViewById(R.id.item_number);
			holder.distance = (TextView)convertView.findViewById(R.id.item_distance);
			holder.ratingBar = (RatingBar)convertView.findViewById(R.id.rating_bar);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
	
		if(worker.getMachineImages().size() !=0){
			Glide.with(mContext).load(worker.getMachineImages().get(0).getUrl()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.image);
		}
		((LayerDrawable)holder.ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(Color.YELLOW, Mode.SRC_ATOP);
		holder.name.setText(worker.getWorkerName());
		holder.type.setText(worker.getCategory_name());
		holder.number.setText(worker.getMachine_number()+"");
		holder.distance.setText(worker.getDistance()+"km");
		holder.ratingBar.setRating(worker.getEvaluateVaule());
		return convertView;
	}

	private static class ViewHolder {
		private ImageView image;
		private TextView name;
		private TextView type;
		private TextView number;
		private TextView distance;
		private RatingBar ratingBar;
	}

}
