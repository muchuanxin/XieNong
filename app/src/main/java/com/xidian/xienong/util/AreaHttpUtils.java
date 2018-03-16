package com.xidian.xienong.util;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.DistrictChildrenParam;
import com.xidian.xienong.shoppingmall.me.AreaFragment;
import com.xidian.xienong.tools.SweetAlertDialog;

//import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author SoBan
 * @create 2016/12/7 09:19.
 */
public class AreaHttpUtils {

    public final static String RESULT = "result";
    public final static String AREACODE = "areacode";

    public final static String AREA = "area";
    public final static String PROVINCE = "province";
    public final static String CITY = "city";
    public final static String DISTRICT = "district";

    private FragmentActivity mActivity;
    private OnAreaListener mOnAreaListener;
    private SweetAlertDialog mLoadingDialog;

    public AreaHttpUtils(FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * 请求获取地区数据
     * @param pDistrict 地区编码id
     */
    public void getDistrict(int pDistrict) {
        TencentSearch tencentSearch = new TencentSearch(mActivity);
        DistrictChildrenParam districtChildrenParam = new DistrictChildrenParam();
        //如果不设置id，则获取全部数据
        districtChildrenParam.id(pDistrict);
        showProgressDialog("加载中...");
        tencentSearch.getDistrictChildren(districtChildrenParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    return;
                }
                String result = new Gson().toJson(arg1);
                mOnAreaListener.onSuccess(result);
                dismissProgressDialog();
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                mOnAreaListener.onFailure(arg1);
                dismissProgressDialog();
            }
        });
    }

    /**
     * 新建一个Fragment
     * @param result 接口返回的数据
     * @param area 数据是属于省份或者城市或区域
     * @param fragmentId fragment的省份或者城市或区域 布局
     */
    public void newFragment(String result, String area, int fragmentId) {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(fragmentId);

        Bundle args = new Bundle();
        args.putString(RESULT, result);
        args.putString(AREA, area);
        if (fragment == null) {
            fragment = Fragment.instantiate(mActivity, AreaFragment.class.getName(), args);
            fm.beginTransaction().add(fragmentId, fragment).commit();
        }
    }

    public OnAreaListener getOnAreaListener() {
        return mOnAreaListener;
    }

    public void setOnAreaListener(OnAreaListener mOnAreaListener) {
        this.mOnAreaListener = mOnAreaListener;
    }

    public interface OnAreaListener {
        void onSuccess(String result);

        void onFailure(String arg1);
    }

    /**
     * 显示sweetalertDialog加载框
     * @param message 显示的信息
     */
    public void showProgressDialog(String message) {
        mLoadingDialog = new SweetAlertDialog(mActivity, SweetAlertDialog.PROGRESS_TYPE);
        mLoadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mLoadingDialog.setTitleText(message);
        mLoadingDialog.show();
    }

    public void dismissProgressDialog() {
        if (mLoadingDialog != null || mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
