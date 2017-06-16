package com.xidian.xienong.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Resource;

import java.util.List;

public class DispatchedResourceAdapter extends BaseAdapter{
	
	private List<Resource> list;
	private Context mContext;
	private Activity activity;
	private LayoutInflater mInflater;
	private boolean isSelected = false;
	private int position=0;
	private Resource resource;

	public DispatchedResourceAdapter(Context context, List<Resource> list){
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

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		resource = list.get(position);
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dispatched_resource_list_item, null);
//			holder.delete=(Button)convertView.findViewById(R.id.delete_resource);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox_resource);
			holder.resource_info_machine = (TextView)convertView.findViewById(R.id.tv_dispatched_resource_machine);
			holder.resource_info_driver = (TextView)convertView.findViewById(R.id.tv_dispatched_resource_driver);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.resource_info_machine.setText(list.get(position).getMachine().getIdentification_number());
		holder.resource_info_driver.setText(list.get(position).getDriver().getDriver_name());
		if(this.position == position){
			holder.checkBox.setChecked( isSelected ? true : false );
		}else{
			holder.checkBox.setChecked(false);
		}
//		if(this.position == position){
//			if(this.isSelected == true){
//				holder.delete.setBackground(mContext.getResources().getDrawable(R.drawable.announce_state_corner1));
//				holder.delete.setTextColor(mContext.getResources().getColor(R.color.white));
//			}
//		}else{
//			holder.delete.setBackground(mContext.getResources().getDrawable(R.drawable.announce_state_corner2));
//			holder.delete.setTextColor(mContext.getResources().getColor(R.color.orange));
//		}
//		holder.delete.setOnClickListener(new OnClickListener() {
//			
//			@SuppressLint("NewApi") @Override
//			public void onClick(View v) {
//				((Button)v).setBackground(mContext.getResources().getDrawable(R.drawable.announce_state_corner1));
//				((Button)v).setTextColor(mContext.getResources().getColor(R.color.white));
				// TODO Auto-generated method stub
//				 new SweetAlertDialog(mContext, SweetAlertDialog.TIP_TYPE)
//		         .setTitleText("�Ƴ���Դ")
//		         .setContentText("���Ƿ��Ƴ���ũ����˾����")
//		         .setCancelText("����лл")
//		         .setConfirmText("�ã��Ƴ�")
//		         .showCancelButton(true)
//		         .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//		             @Override
//		             public void onClick(SweetAlertDialog sDialog) {
//		            	 sDialog.dismiss();
//		             }
//		         })
//		         .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//		             @Override
//		             public void onClick(SweetAlertDialog sDialog) {
//		            	 sDialog.dismiss();
//		            	 list.remove(resource);
//		            	 notifyDataSetChanged();
//		             }
//		         })
//		        .show();
//			}
//		});
		return convertView;
	}

	private static class ViewHolder {
		private Button delete;
		private TextView resource_info_machine;
		private TextView resource_info_driver;
		private CheckBox checkBox;
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
