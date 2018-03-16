package com.xidian.xienong.shoppingmall.classification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.model.Commodity;
import com.xidian.xienong.model.CommodityComment;
import com.xidian.xienong.model.CommodityImage;
import com.xidian.xienong.model.FirstContent;
import com.xidian.xienong.model.FirstInfo;
import com.xidian.xienong.model.ProduceParameter;
import com.xidian.xienong.model.SecondContent;
import com.xidian.xienong.model.User;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/28.
 */

public class ClassificationFragment extends Fragment {

    private View view;
    private CommodityCategoryFragment categoryFragment;
    private Context context;
    private OKHttp httpUrl;
    private List<FirstContent> contents = new ArrayList<>();
    List<FirstInfo> fis = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.classification_fragment, null);
        context=getActivity();
        httpUrl = OKHttp.getInstance();
        getCommodityCategory();
        return view;
    }

    private void getCommodityCategory() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("begin_date", Time.getAmonthAgoDay());
        map.put("end_date", Time.getToday());

        httpUrl.post(Url.GetCommodityCategory, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetCommodityCategory : " + Url.GetCommodityCategory);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseCommodityCategoryResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseCommodityCategoryResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                contents.clear();
                fis.clear();

                JSONArray typesList = jb.getJSONArray("types");
                for(int i=0; i < typesList.length(); i++){
                    JSONObject object = typesList.getJSONObject(i);
                    FirstInfo fi = new FirstInfo();
                    fi.setFirstId(object.getString("first_id"));
                    fi.setFirstName(object.getString("first_name"));
                    fi.setFirstPic(object.getString("first_pic"));
                    fis.add(fi);
                }

                JSONArray contentsList = jb.getJSONArray("categories");
                for(int i=0; i < contentsList.length(); i++){
                    JSONObject object = contentsList.getJSONObject(i);
                    FirstContent fc = new FirstContent();
                    fc.setFirstCategoryId(object.getString("first_cateory"));
                    fc.setFirstCategoryName(object.getString("first_cateory_name"));
                    fc.setFirstUrl(object.getString("first_cateory_pic"));

                    List<SecondContent> secondContents = new ArrayList<>();
                    JSONArray jsc = object.getJSONArray("second_category_list");

                    for(int j=0;j< jsc.length();j++){
                        JSONObject jso = jsc.getJSONObject(j);
                        SecondContent sc = new SecondContent();
                        sc.setSecondCategory(jso.getString("second_category"));
                        sc.setSaleCount(jso.getInt("saleCount"));
                        List<Commodity> secondCommodities = new ArrayList<>();
                        JSONArray commoditieslist = jso.getJSONArray("commodityList");
                        for(int k = 0 ; k <commoditieslist.length();k++ ){
                            JSONObject cl = commoditieslist.getJSONObject(k);
                            Commodity c = new Commodity();
                            c.setCommodityId(cl.getString("commodity_id"));
                            c.setCommodityName(cl.getString("commodity_name"));
                            c.setDealVolume(cl.getString("deal_volume"));
                            c.setPrice(cl.getString("on_shelve_price"));
                            c.setCurrentCount(cl.getString("current_count"));
                            c.setDiscount(cl.getString("discount"));
                            c.setTransportExpense(cl.getString("trans_expense"));
                            c.setCommodityDescription(cl.getString("commodity_description"));
                            c.setSpecification(cl.getString("specification"));

                            JSONObject  pp = cl.getJSONObject("produce_parameter");
                            ProduceParameter pro = new ProduceParameter();
                            pro.setWarranty(pp.getString("warranty"));
                            pro.setNetWeight(pp.getString("net_weight"));
                            pro.setPackagingManner(pp.getString("packaging_manner"));
                            pro.setProducingArea(pp.getString("producing_area"));
                            c.setProduceParameter(pro);

                            List<CommodityImage> ciList = new ArrayList<>();
                            JSONArray images = cl.getJSONArray("commodity_image");
                            for(int m=0; m< images.length();m++){
                                JSONObject jo = images.getJSONObject(m);
                                CommodityImage ci = new CommodityImage();
                                ci.setCommodityImgUrl(jo.getString("commodity_pic_url"));
                                ciList.add(ci);
                            }
                            c.setCommodityImageList(ciList);

                            List<CommodityComment> ccList = new ArrayList<>();
                            JSONArray ccs = cl.getJSONArray("commodity_comments");
                            for(int p=0; p< ccs.length();p++){
                                JSONObject jo = ccs.getJSONObject(p);
                                CommodityComment cc = new CommodityComment();
                                cc.setCommentId(jo.getString("comment_id"));
                                cc.setCommentContent(jo.getString("comment_content"));
                                cc.setCommentTime(jo.getString("comment_time"));
                                cc.setCommentLevel((float) jo.getDouble("comment_level"));
                                User user = new User();
                                user.setUserName(jo.getString("user_name"));
                                user.setHead_photo(jo.getString("head_photo"));
                                cc.setUser(user);
                                ccList.add(cc);
                            }
                            c.setCommodityCommentsList(ccList);
                            secondCommodities.add(c);
                        }
                        sc.setCommodities(secondCommodities);
                        secondContents.add(sc);
                    }
                    fc.setSecondContentList(secondContents);
                    contents.add(fc);
                }

                setTabSelection(0);
            }else{
                Toast.makeText(context, "获取商品失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //根据传入的index参数来设置选中的tab页
    @SuppressLint("NewApi")
    private void setTabSelection(int index) {

        // 开启一个Fragment事务
        FragmentManager manager =  getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                Log.i("kmj","fis size1 :" + fis.size());
                Log.i("kmj","fis size2 :" + contents.size());
                categoryFragment = new CommodityCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("types", (Serializable) fis);
                bundle.putSerializable("contents", (Serializable) contents);
                categoryFragment.setArguments(bundle);
                transaction.add(R.id.main_content, categoryFragment,"shopping_online");
        }
        transaction.commitAllowingStateLoss();
    }

    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction)
    {
        if (categoryFragment != null){
            transaction.remove(categoryFragment);
            categoryFragment = null;
        }
    }

    public void onResume(){
        super.onResume();
        getCommodityCategory();
    }
}
