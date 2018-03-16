package com.xidian.xienong.shoppingmall.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xidian.xienong.R;
import com.xidian.xienong.adapter.AreaAdapter;
import com.xidian.xienong.model.AddressInfo;
import com.xidian.xienong.model.AreaInfo;
import com.xidian.xienong.model.ResultInfo;
import com.xidian.xienong.util.AreaHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SoBan
 * @create 2016/12/6 18:40.
 */
public class AreaFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View mView;
    private Bundle mArgs;
    private ListView mListView;
    private AreaAdapter mAdapter;
    private List<AreaInfo> mInfos = new ArrayList<>();
    private String sArea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_area, container, false);
            initViews();
        } else if (mView.getParent() != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
        return mView;
    }

    private void initViews() {
        mListView = (ListView) mView.findViewById(R.id.area_listview);
        mAdapter = new AreaAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        setData();
    }

    private void setData() {
        if (mArgs == null) {
            mArgs = this.getArguments();
            String result = mArgs.getString(AreaHttpUtils.RESULT);
            sArea = mArgs.getString(AreaHttpUtils.AREA);
            Gson gson = new Gson();
            ResultInfo resultInfo = gson.fromJson(result, ResultInfo.class);
            mInfos = resultInfo.getResult().get(0);
            mAdapter.setList(mInfos);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String area = mInfos.get(i).getFullname();
        switch (sArea) {
            case AreaHttpUtils.PROVINCE:
                startActivity(AreaCityActivity.class, i);
                AddressInfo.getInstance().setsProvince(area);
                break;
            case AreaHttpUtils.CITY:
                startActivity(AreaDistrictActivity.class, i);
                AddressInfo.getInstance().setsCity(area);
                break;
            case AreaHttpUtils.DISTRICT:
                AddressInfo.getInstance().setsDistrict(area);
                toAreaActivity();
                break;
        }
    }

    private void startActivity(Class<? extends FragmentActivity> cls, int position) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(AreaHttpUtils.AREACODE, mInfos.get(position).getId());
        getActivity().startActivityForResult(intent, 1235);
    }

    private void toAreaActivity() {
        //Intent mIntent = new Intent(getActivity(), AddAddressActivity.class);
        Intent mIntent = new Intent();
        mIntent.putExtra("address", AddressInfo.getInstance().getAddress());
        getActivity().setResult(5321, mIntent);
        getActivity().finish();
        //mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //getActivity().startActivity(mIntent);
    }
}
