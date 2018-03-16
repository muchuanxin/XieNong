package com.xidian.xienong.shoppingmall.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.xidian.xienong.R;
import com.xidian.xienong.util.AreaHttpUtils;

/**
 * @author SoBan
 * @create 2016/12/7 14:05.
 */
public class BaseFragmentActivity extends AppCompatActivity {

    private AreaHttpUtils mUtils;
    //private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*mImageView = (ImageView) findViewById(R.id.iv_back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }

    public void setAreaType(final String type) {
        mUtils = new AreaHttpUtils(this);
        mUtils.setOnAreaListener(new AreaHttpUtils.OnAreaListener() {
            @Override
            public void onSuccess(String result) {
                setFragment(type, result);
            }

            @Override
            public void onFailure(String arg1) {

            }
        });
        Intent intent = this.getIntent();
        if (intent != null) {
            mUtils.getDistrict(intent.getIntExtra(AreaHttpUtils.AREACODE, 0));
        }
    }

    private void setFragment(String type, String result) {
        switch (type) {
            case AreaHttpUtils.PROVINCE:
                mUtils.newFragment(result, type, R.id.province_fragment);
                break;
            case AreaHttpUtils.CITY:
                mUtils.newFragment(result, type, R.id.city_fragment);
                break;
            case AreaHttpUtils.DISTRICT:
                mUtils.newFragment(result, type, R.id.district_fragment);
                break;
        }
    }
}
