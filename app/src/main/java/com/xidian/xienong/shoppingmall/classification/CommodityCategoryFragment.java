package com.xidian.xienong.shoppingmall.classification;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xidian.xienong.R;
import com.xidian.xienong.model.FirstContent;
import com.xidian.xienong.model.FirstInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommodityCategoryFragment extends Fragment {


    private FirstClassIndexListFragment firstFragment;
    private Handler handler;
    private List<FirstContent> fcList = new ArrayList<>();
    private List<FirstInfo> fis = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classification_fragment, container, false);
        fis = (List<FirstInfo>) getArguments().get("types");
        fcList = (List<FirstContent>) getArguments().get("contents");

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        firstFragment = new FirstClassIndexListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("types", (Serializable) fis);
        bundle.putSerializable("contents", (Serializable) fcList);
        firstFragment.setArguments(bundle);
        transaction.add(R.id.first_index, firstFragment);
        transaction.commit();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
