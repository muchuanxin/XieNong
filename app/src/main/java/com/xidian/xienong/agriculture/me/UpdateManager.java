package com.xidian.xienong.agriculture.me;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.xienong.R;
import com.xidian.xienong.application.ConnectUtil;
import com.xidian.xienong.tools.SweetAlertDialog;
import com.xidian.xienong.util.PostParameter;
import com.xidian.xienong.util.TakePhotoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * 应用程序更新工具包
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2012-6-29
 * @modified Bryan 2013-7-26
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL   = 1;

	private static UpdateManager updateManager;

	private Context mContext;
	//通知对话框
	private Dialog noticeDialog;
	//下载对话框
	private Dialog downloadDialog;
	//'已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	//进度条
	private ProgressBar mProgress;
	//显示下载数值
	private TextView mProgressText;
	//查询动画
	private ProgressDialog mProDialog;
	//进度值
	private int progress;
	//下载线程
	private Thread downLoadThread;
	//终止标记
	private boolean interceptFlag;
	//返回的安装包url
	private String apkUrl = "";
	//下载包保存路径
	private String savePath = "";
	//apk保存完整路径
	private String apkFilePath = "";
	//临时下载文件路径
	private String tmpFilePath = "";
	//下载文件大小
	private String apkFileSize;
	//已下载文件大小
	private String tmpFileSize;

	private String curVersionName = "";
	private int curVersionCode;
	private String newVersionName="";
	private String newVersionCode="";
	private String downloadUrl="";
	private Handler handler;
	private String 	saveFilePath = "";

	private Handler mHandler = new Handler(){

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWN_UPDATE:
					mProgress.setProgress(progress);
					mProgressText.setText(tmpFileSize + "/" + apkFileSize);
					break;
				case DOWN_OVER:
					downloadDialog.dismiss();
					installApk();
					break;
				case DOWN_NOSDCARD:
					downloadDialog.dismiss();
					Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
					break;
			}
		};
	};

	/**
	 * 单例,获取UpdateManager实例
	 * @return
	 */
	public static UpdateManager getUpdateManager() {
		if(updateManager == null){
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	/**
	 * 检查App更新
	 * @param context
	 * @param isShowMsg 是否显示提示消息
	 */

	public void checkAppUpdate(Context context, final boolean isShowMsg){
		this.mContext = context;
		getCurrentVersion();
		if(isShowMsg){
			if(mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...", true, true);
			else if(mProDialog.isShowing() || (latestOrFailDialog!=null && latestOrFailDialog.isShowing()))
				return;
		}
		handler = new Handler(){
			public void handleMessage(Message msg) {
				//进度条对话框不显示 - 检测结果也不显示
				if(mProDialog != null && !mProDialog.isShowing()){
					return;
				}
				//关闭并释放释放进度条对话框
				if(isShowMsg && mProDialog != null){
					mProDialog.dismiss();
					mProDialog = null;
				}
				//显示检测结果
				if(msg.what == 1){
					String message = "", reCode = "";
					JSONObject result = (JSONObject) msg.obj;
					try {
						reCode = result.getString("reCode");
						message = result.getString("message");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (reCode.equalsIgnoreCase("SUCCESS")) {

						try {
							apkUrl =result.getString("newVersionUrl");
							newVersionName = result.getString("newVersionName");
							newVersionCode = result.getString("newVersionCode");
							showNoticeDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else
					{
						showLatestOrFailDialog(DIALOG_TYPE_LATEST);
					}
				}else if(isShowMsg){
					showLatestOrFailDialog(DIALOG_TYPE_LATEST);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				if (ConnectUtil
						.isNetworkAvailable(mContext)) {
					PostParameter[] postParams = new PostParameter[1];
					postParams[0] = new PostParameter("currVersion", curVersionName);
					String jsonStr = ConnectUtil.httpRequest(
							ConnectUtil.CheckNewVersion, postParams,
							ConnectUtil.POST);
					if (jsonStr == null || jsonStr.equals("")) {
						msg.what = 0;// 失败
						msg.obj = "fail";
						handler.sendMessage(msg);
					} else {

						try {
							JSONObject result = new JSONObject(jsonStr);
							// String msg= result.getString("reCode");
							msg.what = 1;
							msg.obj = result;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				else {
					msg.what = -1;
					msg.obj = "网络不可用";
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 检查App更新，这里的更新是在Activity的OnCreate方法里面启动的，
	 * 每次登录后自动弹出更新提醒，可以勾选不在提示功能
	 */
	public void checkAppUpdateAuto (final Context context){
		this.mContext = context;
		getCurrentVersion();
		handler = new Handler(){
			public void handleMessage(Message msg) {
				//显示检测结果
				if(msg.what == 1){
					String message = "", reCode = "";
					JSONObject result = (JSONObject) msg.obj;
					try {
						reCode = result.getString("reCode");
						message = result.getString("message");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (reCode.equalsIgnoreCase("SUCCESS")) {
//						mUpdate = new Update();
						try {
//							mUpdate.setDownloadUrl(result.getString("newVersionUrl"));
							apkUrl = result.getString("newVersionUrl");
							newVersionName = result.getString("newVersionName");
							newVersionCode = result.getString("newVersionCode");

//							updateMsg = mUpdate.getUpdateLog();
							showNoticeDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else
					{
						//showLatestOrFailDialog(DIALOG_TYPE_LATEST);
					}
				}

			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				if (ConnectUtil
						.isNetworkAvailable(mContext)) {
					PostParameter[] postParams = new PostParameter[1];
					postParams[0] = new PostParameter("currVersion", curVersionName);
					String jsonStr = ConnectUtil.httpRequest(
							ConnectUtil.CheckNewVersion, postParams,
							ConnectUtil.POST);
					if (jsonStr == null || jsonStr.equals("")) {
						msg.what = 0;// 失败
						msg.obj = "fail";
						handler.sendMessage(msg);
					} else {

						try {
							JSONObject result = new JSONObject(jsonStr);
							// String msg= result.getString("reCode");
							msg.what = 1;
							msg.obj = result;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				else {
					msg.what = -1;
					msg.obj = "网络不可用";
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			//关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		SweetAlertDialog sweet =  new SweetAlertDialog(mContext, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
		sweet.setTitleText("温馨提示");
		if (dialogType == DIALOG_TYPE_LATEST) {
			sweet.setContentText("您当前已经是最新版本");
		}else if (dialogType == DIALOG_TYPE_FAIL) {
			sweet.setContentText("无法获取版本更新信息");
		}
		sweet.setCustomImage(R.drawable.custom_img);
		sweet.show();
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion(){
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog(){
		new SweetAlertDialog(mContext, SweetAlertDialog.TIP_TYPE)
				.setTitleText("温馨提示")
				.setContentText("携农有新的版本")
				.setCancelText("以后再说")
				.setConfirmText("立即更新")
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
						sDialog.dismiss();
						showDownloadDialog();
					}
				})
				.show();
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog(){
		Builder builder = new Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			//			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			//			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		//		@Override
		public void run() {
			try {

				String apkName = newVersionName+".apk";
				String tmpApk = newVersionName+".tmp";
				TakePhotoUtil takePhotoUtil = new TakePhotoUtil(mContext,"Update");


				if(takePhotoUtil.getImgPathParent().equals("")){
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return ;
				}
				apkFilePath = takePhotoUtil.getImgPathParent();
				saveFilePath = apkFilePath;
				tmpFilePath = apkFilePath +"/"+ tmpApk;
				apkFilePath = apkFilePath +"/"+ apkName;

				File ApkFile = new File(apkFilePath);

				//是否已下载更新文件
				if(ApkFile.exists()){
					ApkFile.delete();
				}


				//输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				//显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				//进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do{
					int numread = is.read(buf);
					count += numread;
					//进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					//当前进度值
					progress =(int)(((float)count / length) * 100);
					//更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if(numread <= 0){
						//下载完成 - 将临时下载文件转成APK文件
						if(tmpFile.renameTo(ApkFile)){
							//通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf,0,numread);
				}while(!interceptFlag);//点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * @param
	 */
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(apkFilePath);
		System.out.println("apkFilePath---" + "file://" + apkfile.toString());

		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}






}
