package com.xidian.xienong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.xidian.xienong.R;
import com.xidian.xienong.model.MachineCategory;

import java.util.List;

public class CategoryAdapter extends BaseAdapter{
	
	private List<MachineCategory> categorys;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;
	private String selectCategory = "";

	public CategoryAdapter(Context context, List<MachineCategory> categorys){
		this.mContext = context;
		this.categorys = categorys;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return categorys.size();
	}
	@Override
	public Object getItem(int position) {
		return categorys.get(position);
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
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.category=(Button)convertView.findViewById(R.id.btn_category);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
	
		if(position == selectIndex){
			convertView.setSelected(true);
			holder.category.setTextColor(Color.parseColor("#ef8000"));
		}else{
			convertView.setSelected(false);
			holder.category.setTextColor(Color.GRAY);
		}
		
		holder.category.setText(categorys.get(position).getCategory_name());

		return convertView;
	}

	private static class ViewHolder {
		private Button category ;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
	
}