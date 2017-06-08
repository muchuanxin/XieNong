package com.xidian.xienong.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.xidian.xienong.R;
import com.xidian.xienong.agriculture.announcement.NewAnnounceActivity;

/**
 * Created by koumiaojuan on 2017/6/8.
 */

public class SnackbarUtil {
    // android-support-design兼容包中新添加的一个类似Toast的控件。
    // make()中的第一个参数，可以写当前界面中的任意一个view对象。
    private static Snackbar mSnackbar;

    public static void show(final Context context,View view, String msg, int flag) {

        if (flag == 0) { // 短时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        } else { // 长时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        }

        mSnackbar.show();
        // Snackbar中有一个可点击的文字，这里设置点击所触发的操作。
        mSnackbar.setAction(R.string.close, new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Snackbar在点击“关闭”后消失
                mSnackbar.dismiss();
            }
        });
//        mSnackbar.setAction(R.string.confirm, new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                // Snackbar在点击“关闭”后消失
//                mSnackbar.dismiss();
//            }
//        });
        mSnackbar.setAction(R.string.confirm, new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Snackbar在点击“关闭”后消失
                mSnackbar.dismiss();
                Intent intent = new Intent(context, NewAnnounceActivity.class);
                context.startActivity(intent);
            }
        });

    }
}
