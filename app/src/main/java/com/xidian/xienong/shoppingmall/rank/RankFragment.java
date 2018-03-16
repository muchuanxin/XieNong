package com.xidian.xienong.shoppingmall.rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.adapter.RankAdapter;
import com.xidian.xienong.model.Advertisement;
import com.xidian.xienong.model.Rank;
import com.xidian.xienong.network.BaseCallback;
import com.xidian.xienong.network.OKHttp;
import com.xidian.xienong.network.Url;
import com.xidian.xienong.util.RecyclerDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/28.
 */

public class RankFragment extends Fragment implements RankAdapter.OnItemClickListener{

    private View view;
    private RecyclerView rankRecyclerView;
    private RankAdapter adapter;
    private List<Rank> ranks = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private OKHttp httpUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_rank, null);
        initViews();
        initDatas();
        getRankInformation();
        return view;
    }


    private void getRankInformation() {
        Map<String, String> map = new HashMap<String, String>();
        httpUrl.post(Url.GetRankInformation, map, new BaseCallback<String>() {
            @Override
            public void onRequestBefore() {
                Log.i("kmj", "GetRankInformation : " + Url.GetRankInformation);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Log.i("kmj", "onFailure : " + e.toString());
            }

            @Override
            public void onSuccess(Response response, String resultResponse) {
                Log.i("kmj", "result : " + resultResponse);
                parseResponse(resultResponse);
            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                Log.i("kmj", "error : " + e.toString());
            }
        });
    }

    private void parseResponse(String resultResponse) {
        try {
            JSONObject jb = new JSONObject(resultResponse);
            String result = jb.getString("reCode");
            if (result.equals("SUCCESS")) {
                ranks.clear();
                JSONArray ranklist = jb.getJSONArray("ranks");
                for(int i=0; i < ranklist.length(); i++){
                    JSONObject object = ranklist.getJSONObject(i);
                    Rank rank = new Rank();
                    rank.setRankId(object.getString("classifction_id"));
                    rank.setRankName(object.getString("classifction_name"));
                    rank.setRankRule(object.getString("rank_rule"));
                    rank.setRankDetail(object.getString("rank_description"));
                    rank.setImgUrl(object.getString("classifction_pic_url"));
                    ranks.add(rank);
                }
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(), "获取排行失败，请重试", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initDatas() {
//        for(int i=0;i<4;i++){
//            Rank r = new Rank();
//            r.setRankName("年度热销水果");
//            r.setRankDetail("新鲜味十足，补充维生素C");
//            ranks.add(r);
//        }
        httpUrl = OKHttp.getInstance();
        adapter = new RankAdapter(getActivity(),ranks);
        adapter.setOnItemClickListener(this);
        mLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rankRecyclerView.setLayoutManager(mLayoutManager);
        rankRecyclerView.setAdapter(adapter);
        rankRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rankRecyclerView.addItemDecoration(new RecyclerDecoration(
                getActivity(), LinearLayoutManager.VERTICAL, R.drawable.rank_divider));
    }

    private void initViews() {
        rankRecyclerView = (RecyclerView)view.findViewById(R.id.rank_recyclerview);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(),RankInformationActivity.class);
        Log.i("kmj","----intent-----" + ranks.get(position).getRankId());
        intent.putExtra("classification_id",ranks.get(position).getRankId());
        intent.putExtra("title",ranks.get(position).getRankRule());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public void onResume(){
        super.onResume();
        getRankInformation();
    }
}
