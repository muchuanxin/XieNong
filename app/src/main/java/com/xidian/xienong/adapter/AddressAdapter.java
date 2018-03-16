package com.xidian.xienong.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.shoppingmall.me.AddAddressActivity;
import com.xidian.xienong.shoppingmall.me.ChangeAddressActivity;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;
import com.xidian.xienong.util.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xinye on 2017/7/20.
 */

public class AddressAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list;

    public AddressAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_address, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.telephone = (TextView) convertView.findViewById(R.id.telephone);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.edit = (LinearLayout) convertView.findViewById(R.id.edit);
            holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Map<String, String> map = list.get(position);
        holder.name.setText(map.get("consignee_name"));
        holder.telephone.setText(map.get("phone"));
        holder.address.setText(map.get("area")+" "+map.get("detail"));
        if ("1".equals(map.get("is_default"))){
            holder.checkbox.setChecked(true);
            holder.checkbox.setTextColor(0xFFFE5400);
            holder.checkbox.setText("默认地址");
        }
        else {
            holder.checkbox.setChecked(false);
            holder.checkbox.setTextColor(0xFF040404);
            holder.checkbox.setText("设为默认");
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map2 = new HashMap<String, String>();
                map2.put("address_id", map.get("address_id"));
                OKHttp httpUrl = OKHttp.getInstance();
                httpUrl.post(Url.DeleteMyAddress, map2, new BaseCallback<String>() {
                    @Override
                    public void onRequestBefore() {
                        Log.i("mcx", "DeleteMyAddress : " + Url.DeleteMyAddress);
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        Log.i("mcx", "onFailure : " + e.toString());
                    }

                    @Override
                    public void onSuccess(Response response, String resultResponse) {
                        Log.i("mcx", "result : " + resultResponse);
                        try {
                            JSONObject jb = new JSONObject(resultResponse);
                            String result = jb.getString("reCode");
                            if (result.equals("SUCCESS")) {
                                ToastCustom.makeToast(context, "删除成功");
                                list.remove(position);
                                AddressAdapter.this.notifyDataSetChanged();
                            }
                            else {
                                ToastCustom.makeToast(context, "删除失败");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response response, int errorCode, Exception e) {
                        Log.i("mcx", "error : " + e.toString());
                    }
                });

            }
        });

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    SharePreferenceUtil sp = new SharePreferenceUtil(context, Constants.SAVE_USER);
                    Map<String, String> map2 = new HashMap<String, String>();
                    map2.put("user_id", sp.getUserId());
                    map2.put("address_id", map.get("address_id"));
                    OKHttp httpUrl = OKHttp.getInstance();
                    httpUrl.post(Url.SetDefaultAddress, map2, new BaseCallback<String>() {
                        @Override
                        public void onRequestBefore() {
                            Log.i("mcx", "SetDefaultAddress : " + Url.SetDefaultAddress);
                        }

                        @Override
                        public void onFailure(Request request, Exception e) {
                            Log.i("mcx", "onFailure : " + e.toString());
                        }

                        @Override
                        public void onSuccess(Response response, String resultResponse) {
                            Log.i("mcx", "result : " + resultResponse);
                            try {
                                JSONObject jb = new JSONObject(resultResponse);
                                String result = jb.getString("reCode");
                                if (result.equals("SUCCESS")) {
                                    ToastCustom.makeToast(context, "设置成功");
                                    for (Map<String, String> temp : list){
                                        temp.put("is_default", "0");
                                    }
                                    map.put("is_default", "1");
                                    AddressAdapter.this.notifyDataSetChanged();
                                }
                                else {
                                    ToastCustom.makeToast(context, "设置失败");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response response, int errorCode, Exception e) {
                            Log.i("mcx", "error : " + e.toString());
                        }
                    });


                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeAddressActivity.class);
                intent.putExtra("address_id", map.get("address_id"));
                intent.putExtra("name", map.get("consignee_name"));
                intent.putExtra("telephone", map.get("phone"));
                intent.putExtra("address", map.get("area"));
                intent.putExtra("address_detail", map.get("detail"));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        TextView name;
        TextView telephone;
        TextView address;
        CheckBox checkbox;
        LinearLayout edit;
        LinearLayout delete;
    }
}
