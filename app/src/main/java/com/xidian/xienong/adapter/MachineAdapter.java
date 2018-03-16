package com.xidian.xienong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.MachineRecyclerViewHolder;
import com.xidian.xienong.model.Machine;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class MachineAdapter extends RecyclerView.Adapter<MachineRecyclerViewHolder>{

	public interface OnItemClickListener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	public OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}

	private Context mContext = null;
	private List<Machine> data;
	private SharePreferenceUtil sp;
	private MachineImageAdapter adapter;
	private ArrayList<String> urls = new ArrayList<String>();
	public LayoutInflater mLayoutInflater;

	public MachineAdapter(Context mContext, List<Machine> data) {
		this.mContext = mContext;
		this.data = data;
		sp = new SharePreferenceUtil(mContext,Constants.SAVE_USER);
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public MachineRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View mView = mLayoutInflater.inflate(R.layout.machine_list_item, parent, false);
		MachineRecyclerViewHolder mViewHolder = new MachineRecyclerViewHolder(mView);
		return mViewHolder;
	}


	@Override
	public void onBindViewHolder(final MachineRecyclerViewHolder holder, final int position) {

		if (mOnItemClickListener != null) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					mOnItemClickListener.onItemClick(holder.itemView, position);
				}
			});

			holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override public boolean onLongClick(View v) {
					mOnItemClickListener.onItemLongClick(holder.itemView, position);
					return true;
				}
			});
		}
		final Machine machine= data.get(position);
		if(!sp.getHeadPhoto().equals("") || sp.getHeadPhoto() != null){
			Glide.with(mContext).load(sp.getHeadPhoto()).centerCrop().placeholder(R.drawable.author).into(holder.machine_publisher_photo);
		}
		holder.machine_publisher_name.setText(sp.getUserName());
		holder.register_machine_type.setText("农机类型："+machine.getCategory_name());
		holder.register_machine_trademark.setText("农机品牌："+machine.getTrademark_name());
		holder.register_machine_number.setText("农机数量："+ machine.getMachineNumber()+"台");
		holder.machine_publish_time.setText(machine.getUploadTime());
		holder.machine_state.setText(machine.getIsChecked());
		if(machine.getIsChecked().equals("审核通过")){
			holder.gridView.setVisibility(View.VISIBLE);
			if(machine.getMachineImage().size() == 0){
				holder.gridView.setVisibility(View.GONE);
			}else if(machine.getMachineImage().size() > 0 && machine.getMachineImage().size() <=3){
				holder.gridView.setVisibility(View.VISIBLE);
				Log.i("kmj","-----machine.getMachineImage().size()---"+machine.getMachineImage().size());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(85));
				lp.setMargins(dip2px(55), 0, 10, 0);
				holder.gridView.setLayoutParams(lp);
				holder.gridView.setNumColumns(3);
			}else if(machine.getMachineImage().size()==4){
				holder.gridView.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(190), dip2px(170));
				lp.setMargins(dip2px(55), 0, 10, 0);
				holder.gridView.setLayoutParams(lp);
				holder.gridView.setNumColumns(2);
			}else if(machine.getMachineImage().size() > 4 && machine.getMachineImage().size() <=6){
				holder.gridView.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(170));
				lp.setMargins(dip2px(55), 0, 10, 0);
				holder.gridView.setLayoutParams(lp);
				holder.gridView.setNumColumns(3);
			}else{
				holder.gridView.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(255));
				lp.setMargins(dip2px(55), 0, 10, 0);
				holder.gridView.setLayoutParams(lp);
				holder.gridView.setNumColumns(3);
			}
			adapter = new MachineImageAdapter(mContext, machine.getMachineImage());
			holder.gridView.setAdapter(adapter);
		}else{
			holder.gridView.setVisibility(View.GONE);
		}
	}

	public int dip2px(float dpValue) {

		final float scale =mContext.getResources().getDisplayMetrics().density;
		Log.i("kmj","-----scale--" + scale);
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}


}
