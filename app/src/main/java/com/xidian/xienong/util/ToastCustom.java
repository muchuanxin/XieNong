package com.xidian.xienong.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastCustom {
	static public Toast getToast(Context context,String str){
		 Toast toast = Toast.makeText(context,
					str, Toast.LENGTH_SHORT);
				   toast.setGravity(Gravity.CENTER, 0, 0);

				   return toast;
	 }
	static public void makeToast(Context context,String str){
		 Toast toast = Toast.makeText(context,
					str, Toast.LENGTH_SHORT);
				   toast.setGravity(Gravity.CENTER, 0, 0);
				   toast.show();
				   
	 }
}
