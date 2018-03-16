package com.xidian.xienong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xidian.xienong.R;
import com.xidian.xienong.photo.Bimp;

public class PreviewImageAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context mContext;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public PreviewImageAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext = context;
	}


	public int getCount() {
		return Bimp.tempSelectBitmap.size();
	}

	public Object getItem(int arg0) {
		return Bimp.tempSelectBitmap.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.machine_image_item_1,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_machine_image_1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

}
