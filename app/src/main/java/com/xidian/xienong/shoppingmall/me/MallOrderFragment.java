package com.xidian.xienong.shoppingmall.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xidian.xienong.R;
import com.xidian.xienong.home.LoginActivity;
import com.xidian.xienong.util.Constants;
import com.xidian.xienong.util.SharePreferenceUtil;

/**
 * Created by koumiaojuan on 2017/6/28.
 */

public class MallOrderFragment extends Fragment {
    private View view;
    private RelativeLayout rl_all_order;
    private RelativeLayout rl_address;
    private LinearLayout ll_waiting_to_pay,ll_waiting_to_send,ll_waiting_to_get,ll_waiting_to_evaluate;
    private TextView name;
    private TextView jifen;
    private SharePreferenceUtil sp;
    private boolean status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_mall_shopping, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = new SharePreferenceUtil(getActivity(), Constants.SAVE_USER);
        status = !"".equals(sp.getUserId());

        initViews();
        initEvents();

    }

    void initViews(){
        rl_all_order = (RelativeLayout) view.findViewById(R.id.rl_order);
        rl_address = (RelativeLayout) view.findViewById(R.id.rl_address);
        name = (TextView) view.findViewById(R.id.me_name);
        jifen = (TextView) view.findViewById(R.id.jifen);
        ll_waiting_to_pay = (LinearLayout) view.findViewById(R.id.ll_waiting_to_pay);
        ll_waiting_to_send = (LinearLayout) view.findViewById(R.id.ll_waiting_to_send);
        ll_waiting_to_get = (LinearLayout) view.findViewById(R.id.ll_waiting_to_get);
        ll_waiting_to_evaluate = (LinearLayout)view.findViewById(R.id.ll_waiting_to_evaluate);

        if (!status){
            name.setText("登录/注册");
        }
        else {
            name.setText(sp.getUserName());
            jifen.setText("积分："+sp.getTotalIntegral());
        }
    }

    void initEvents(){
        rl_all_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    Intent intent = new Intent();
                  //  intent.setClass(getContext(),AllMallOrderActivity.class);
                    intent.setClass(getContext(),AllTheMallOrderActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginActivity.class);
                    intent.putExtra("clickView", "AllMallOrderActivity");
                    startActivity(intent);
                }
            }
        });
        rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    Intent intent = new Intent();
                    intent.setClass(getContext(),MyAddressActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginActivity.class);
                    intent.putExtra("clickView", "MyAddressActivity");
                    startActivity(intent);
                }
            }
        });
        ll_waiting_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    Intent intent = new Intent();
                    //intent.setClass(getContext(),AllMallOrderActivity.class);
                    intent.setClass(getContext(),AllTheMallOrderActivity.class);
                    intent.putExtra("currentItem",1);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginActivity.class);
                    intent.putExtra("clickView", "AllMallOrderActivity");
                    intent.putExtra("currentItem",1);
                    startActivity(intent);
                }
            }
        });
        ll_waiting_to_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    Intent intent = new Intent();
                   // intent.setClass(getContext(),AllMallOrderActivity.class);
                    intent.setClass(getContext(),AllTheMallOrderActivity.class);
                    intent.putExtra("currentItem",2);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginActivity.class);
                    intent.putExtra("clickView", "AllMallOrderActivity");
                    intent.putExtra("currentItem",2);
                    startActivity(intent);
                }
            }
        });
        ll_waiting_to_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    Intent intent = new Intent();
                  //  intent.setClass(getContext(),AllMallOrderActivity.class);
                    intent.setClass(getContext(),AllTheMallOrderActivity.class);
                    intent.putExtra("currentItem",3);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginActivity.class);
                    intent.putExtra("clickView", "AllMallOrderActivity");
                    intent.putExtra("currentItem",3);
                    startActivity(intent);
                }
            }
        });
        ll_waiting_to_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status){
                    Intent intent = new Intent();
                  //  intent.setClass(getContext(),AllMallOrderActivity.class);
                    intent.setClass(getContext(),AllTheMallOrderActivity.class);
                    intent.putExtra("currentItem",4);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(),LoginActivity.class);
                    intent.putExtra("clickView", "AllMallOrderActivity");
                    intent.putExtra("currentItem",4);
                    startActivity(intent);
                }
            }
        });

    }

}
