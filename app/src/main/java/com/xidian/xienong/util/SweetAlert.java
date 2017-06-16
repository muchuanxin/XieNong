package com.xidian.xienong.util;

import android.content.Context;

import com.xidian.xienong.tools.SweetAlertDialog;

public class SweetAlert {

	public static void ToastNetworkDialog(Context context){
		new SweetAlertDialog(context)
				.setTitleText("温馨提示")
				.setContentText("当前网络不可用！")
				.show();
	}

	public static void ToastWarningConfirmDialog(Context context){
		new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("温馨提醒")
				.setContentText("您暂时还没有相关评论!")
				.setConfirmText("是的，确定!")
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						// reuse previous dialog instance
						sDialog.dismiss();
					}
				})
				.show();
	}




}
