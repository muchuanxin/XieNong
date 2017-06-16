package com.xidian.xienong.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhotoUtil {
	private Context context;
	private String SDState;
	private String dir;
	public String imgPathParent;// 拍照时图片存储的文件�?
	private String imgPath;// 拍照时图片存储的路径
	private Uri imgUri;// 拍照时图片的URI

	public static boolean hasSDCard=true;
	private String imageName;
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public TakePhotoUtil(Context context,String dir) {

		this.context = context;
		this.dir=dir;
		// 执行拍照前，应该先判断SD卡是否存�?
		SDState = Environment.getExternalStorageState();
//		if(SDState.equals(Environment.MEDIA_MOUNTED)){
//			hasSDCard=true;
//		}
//		else
//			hasSDCard=false;
	}
	
	public String getImgPathParent() {
		String path = null;
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			path = Environment.getExternalStorageDirectory()+ "/HelpOfWorker/"+dir;
			File file=new File(path);
			if(!file.exists()){
				Log.i("liuhaoxian","make dir");
				file.mkdirs();
				
			}
			else
				Log.i("liuhaoxian","dir exist");
			path=file.getAbsolutePath();
			Log.i("liuhaoxian","path-------->"+path);
			/**
			 * 
			 */
			//path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

		}else {

			return "";
//			String rootPath=context.getFilesDir().getAbsolutePath();
//			//Log.i("liuhaoxian","path="+rootPath);
//			File file=new File(rootPath+"/"+dir);
//			if(!file.exists()){
//				Log.i("liuhaoxian","make dir");
//				file.mkdir();
//				
//			}
//			else
//				Log.i("liuhaoxian","dir exist");
//			path=file.getAbsolutePath();
//			Log.i("liuhaoxian","path-------->"+path);
			//File file = context.getDir("yuerhuoban", Context.MODE_PRIVATE);
			//path = file.getAbsolutePath(); 

		}
		return path;
	}
	
	public String getImgPath() {
		imgPathParent = getImgPathParent();
		
		//规范化照片名�?
		Date date = new Date();
		String dateStr = (new SimpleDateFormat("yyyyMMdd")).format(date);
		String timeStr = (new SimpleDateFormat("hhmmss")).format(date);
		String imgName = "/" + dateStr + "_" + timeStr + ".jpg";
		String path = imgPathParent + imgName;
		this.imageName=dateStr + "_" + timeStr + ".jpg";
		File imgFile = new File(path);
		if (!imgFile.exists()) {
			File imgFileParent = imgFile.getParentFile();
			imgFileParent.mkdirs();
		}
		return path;
	}

	public Uri getImgUri() {
		imgPath = getImgPath();
		
		imgUri = Uri.fromFile(new File(imgPath));
		
		return imgUri;
	}
	
	public static boolean isExist(String imgPath) {
		File imgFile = new File(imgPath);
		if (!imgFile.exists()) {
			return false;
		}else {
			return true;
		}
	}
}
