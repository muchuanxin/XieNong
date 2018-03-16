package com.xidian.xienong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.xidian.xienong.R;
import com.xidian.xienong.ViewHolder.BrandRecyclerViewHolder;
import com.xidian.xienong.model.Brand;
import com.xidian.xienong.model.FirstContent;
import com.xidian.xienong.model.FirstInfo;
import com.xidian.xienong.model.SecondContent;
import com.xidian.xienong.shoppingmall.brand.CommodityShowActivity;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by koumiaojuan on 2017/6/29.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandRecyclerViewHolder>{

//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//
//        void onItemLongClick(View view, int position);
//    }
//
//    public OnItemClickListener mOnItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }

    public LayoutInflater mLayoutInflater;
    public Context mContext = null;
    public List<FirstContent> contents;
    public List<FirstInfo> fis;
    public SharePreferenceUtil sp;
    public BrandCategoryAdapter adapter;


    public BrandAdapter(Context mContext, List<FirstInfo> fis, List<FirstContent> contents) {
        this.mContext = mContext;
        this.fis = fis;
        this.contents = contents;
        mLayoutInflater = LayoutInflater.from(mContext);
        sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
    }

    @Override
    public BrandRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.brand_recycle_item, parent, false);
        BrandRecyclerViewHolder mViewHolder = new BrandRecyclerViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final BrandRecyclerViewHolder holder, final int position) {
//        if (mOnItemClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.itemView, position);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override public boolean onLongClick(View v) {
//                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
//                    return true;
//                }
//            });
//        }

//        final Brand brand = data.get(position);
//        holder.brandName.setText(brand.getBrandName());

        holder.brandName.setText(fis.get(position).getFirstName());
        Glide.with(mContext).load(fis.get(position).getFirstPic()).centerCrop().placeholder(R.drawable.empty_picture).into(holder.imageView);

        boolean find = false;
        FirstContent userfulFC = null;
        for(FirstContent fc: contents){
            if(fc.getFirstCategoryName().equals(fis.get(position).getFirstName())){
                holder.noContent.setVisibility(View.GONE);
                userfulFC = fc;
                adapter = new BrandCategoryAdapter(fc.getSecondContentList(),mContext);
                find = true;
                break;
            }
        }
        if(!find){
            Log.i("kmj","find ssss: " + find);
            holder.noContent.setVisibility(View.VISIBLE);
            List<SecondContent> secondContents = new ArrayList<>();
            adapter = new BrandCategoryAdapter(secondContents,mContext);
        }

        holder.gridView.setAdapter(adapter);

        final FirstContent finalUserfulFC = userfulFC;
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SecondContent  sc = finalUserfulFC.getSecondContentList().get(position);
                Intent intent = new Intent(mContext,CommodityShowActivity.class);
                intent.putExtra("secondContent", (Serializable) sc);
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return fis.size();
    }
}
