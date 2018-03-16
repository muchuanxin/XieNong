package com.xidian.xienong.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by koumiaojuan on 2017/6/17.
 */

public class MyCountDownTimer extends CountDownTimer {

    private TextView textView;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView getVerifyCode) {
        super(millisInFuture, countDownInterval);
        this.textView = getVerifyCode;
    }

    @Override
    public void onFinish() {
        textView.setText("获取验证码");
        textView.setClickable(true);
        textView.setTextColor(Color.WHITE);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setTextColor(Color.parseColor("#dcdcdc"));
        textView.setClickable(false);
        textView.setText("获取验证码("+millisUntilFinished / 1000+"s)");
    }

}
