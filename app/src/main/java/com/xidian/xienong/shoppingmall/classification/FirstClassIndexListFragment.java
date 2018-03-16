package com.xidian.xienong.shoppingmall.classification;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xidian.xienong.R;
import com.xidian.xienong.model.FirstContent;
import com.xidian.xienong.model.FirstInfo;
import com.xidian.xienong.model.SecondContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstClassIndexListFragment extends ListFragment {

    private FragmentManager manager;
    private SimpleAdapter adapter;
    private List<Map<String, String>> list = new ArrayList<>();;
    private FragmentTransaction transaction;
    private List<FirstContent> fcList = new ArrayList<>();
    private List<FirstInfo> fis = new ArrayList<>();
    private String[] names = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
        fis =  (List<FirstInfo>) getArguments().get("types");
        fcList = (List<FirstContent>) getArguments().get("contents");
        initDatas();

//        String[] names = new String[]{"水果蔬菜","畜牧水产","粮油米面","农副加工","亩木花草","农资农机","中草药"};
//        list = new ArrayList<>();
//        for (String s : names){
//            Map<String, String> map = new HashMap<>();
//            map.put("name", s);
//            list.add(map);
//        }

    }

    private void initDatas() {
        names = new String[fis.size()];
        for(int i=0;i< fis.size();i++){
            names[i] = fis.get(i).getFirstName();
        }
        for (String s : names){
            Map<String, String> map = new HashMap<>();
            map.put("name", s);
            list.add(map);
        }
        adapter = new SimpleAdapter(getActivity(), list, R.layout.list_view_first_class, new String[]{"name"},
                new int[]{R.id.first_content_name});
        setListAdapter(adapter);
        changeFragment(names[0]);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        changeFragment(names[position]);
    }



    public void changeFragment(String firstContent){
        int position = getClickedPosition(firstContent);
        FirstContent fc= getSecondContents(firstContent);
        transaction = manager.beginTransaction();
        SecondClassIndexListFragment secondFragment = new SecondClassIndexListFragment();
        Bundle bundle = new Bundle();

        bundle.putSerializable("url", fis.get(position).getFirstPic());
        bundle.putSerializable("first_content", (Serializable) fc);
        secondFragment.setArguments(bundle);
        transaction.replace(R.id.second_index, secondFragment);
        transaction.commit();
    }

    private int getClickedPosition(String firstContent) {
        int position = 0;
        for(FirstInfo fi : fis){
            if(fi.getFirstName().equals(firstContent)){
                position = fis.indexOf(fi);
            }
        }
        return  position;
    }

    private FirstContent getSecondContents(String firstContent) {
        boolean isFind = false;
        FirstContent  some_fc = new FirstContent();
        for(FirstContent fc : fcList){
            if(fc.getFirstCategoryName().equals(firstContent)){
                some_fc = fc;
                isFind = true;
            }
        }
        if(!isFind){
            List<SecondContent> list = new ArrayList<>();
            some_fc.setSecondContentList(list);
        }
        return some_fc;
    }

}
