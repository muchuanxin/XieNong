package com.xidian.xienong.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.xidian.xienong.R;
import com.xidian.xienong.tools.SweetAlertDialog;


public class NetWorkUtil {

	private Context context;

	public NetWorkUtil(Context context) {
		this.context = context;
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		System.out.println("connect: " + connect);
		if (connect == null) {
			return false;
		} else// get all network info
		{
			NetworkInfo[] info = connect.getAllNetworkInfo();
//			System.out.println("info: " + info);
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}


	public void setNetwork1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(R.string.netstate);
		builder.setMessage(R.string.setnetwork);
		builder.setPositiveButton(R.string.ok_set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					context.startActivity(new Intent(Settings.ACTION_SETTINGS));
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		builder.setNegativeButton(R.string.cancel_set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create();
		builder.show();
	}
	
	public void setNetwork() {
//		SweetAlertDialog.mWarningFrame.setVisibility(View.VISIBLE);
		 new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
		 .setTitleText("网络状态")
		 .setContentText("当前网络不可用，是否设置网络？")
		 .setCancelText("不，谢谢")
		 .setConfirmText("好，设置")
		 .showCancelButton(true)
         
         .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
             @Override
             public void onClick(SweetAlertDialog sDialog) {
            	 sDialog.dismiss();
             }
         })
         .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
             @Override
             public void onClick(SweetAlertDialog sDialog) {
            	 try {
 					context.startActivity(new Intent(Settings.ACTION_SETTINGS));
 				} catch (Exception e) {
 					System.out.println(e);
 				}
             }
         })
         .show();
	}


	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			System.out.println(ex);
		}
		return null;
	}

}
