package com.xidian.xienong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.model.CommentBean;
import com.xidian.xienong.util.CircleImageView;

import java.util.List;

public class CommentAdapter extends BaseAdapter{
	
	private List<CommentBean> lists;
	private Context context;
	
	public CommentAdapter(List<CommentBean> lists,Context context) {
		super();
		this.lists = lists;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewholer = null;
		if (convertView == null) {
			viewholer = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
			viewholer.photo = (CircleImageView) convertView.findViewById(R.id.farmer_photo);
			viewholer.farmerName = (TextView) convertView.findViewById(R.id.farmer_name);
			viewholer.value = (RatingBar) convertView.findViewById(R.id.serve_value);
			viewholer.date = (TextView) convertView.findViewById(R.id.date);
			viewholer.content = (TextView) convertView.findViewById(R.id.comment);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHolder) convertView.getTag();
		}

		if(!lists.get(position).getFarmer_headPhoto().equals("") || lists.get(position).getFarmer_headPhoto() !=null ){
			Glide.with(context).load(lists.get(position).getFarmer_headPhoto()).centerCrop().placeholder(R.drawable.portrait).into(viewholer.photo);
		}
		if(lists.get(position).getInput_content().equals("")){
			viewholer.content.setVisibility(View.GONE);
		}else{
			viewholer.content.setText(lists.get(position).getInput_content());
			viewholer.content.setVisibility(View.VISIBLE);
		}
		viewholer.date.setText(lists.get(position).getComment_date());
		viewholer.farmerName.setText(lists.get(position).getFarmer_name());
		viewholer.value.setRating(Float.valueOf(lists.get(position).getServeValue()));
		return convertView;
	}

}

class ViewHolder {
	TextView farmerName;
	TextView date;
	RatingBar value;
	TextView content;
	CircleImageView photo;
}
