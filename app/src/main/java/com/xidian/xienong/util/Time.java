package com.xidian.xienong.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Time {
	public static int compare_date(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	public static int compare_with_now_date(String date) { 
        Date date_now=new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now =df.format(date_now);
        try {
            Date dt1 = df.parse(now);
            Date dt2 = df.parse(date);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	public static  String getNowTime(){
		String result="";
		String second = "";
		String minute = "";
		String hour = "";
		Calendar cal=Calendar.getInstance();
		if(cal.get(Calendar.SECOND) < 10){
			second = "0" + cal.get(Calendar.SECOND);
		}else{
			second = cal.get(Calendar.SECOND)+"";
		}
		
		if(cal.get(Calendar.MINUTE) < 10){
			minute = "0" + cal.get(Calendar.MINUTE);
		}else{
			minute = cal.get(Calendar.MINUTE)+"";
		}
		
		if(cal.get(Calendar.HOUR_OF_DAY)<10){
			hour = "0" + cal.get(Calendar.HOUR_OF_DAY);
		}else{
			hour = cal.get(Calendar.HOUR_OF_DAY)+"";
		}
		result=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(cal.DATE)+" "+hour+":"+minute+":"+second;
		
		return result;
	}
	
	public static int compare_time(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	public static String getTime(){
		String result="";
		Calendar cal=Calendar.getInstance();
		result=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(cal.DATE)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
		return result;
	}
	
	public static String getToday(){
		String result="",month="",day="";
		Calendar cal=Calendar.getInstance();
		if((cal.get(Calendar.MONTH)+1) < 10){
			month = "0"+(cal.get(Calendar.MONTH)+1);
		}else{
			month = (cal.get(Calendar.MONTH)+1)+"";
		}
		if(cal.get(cal.DATE) < 10){
			day = "0"+cal.get(cal.DATE);
		}else{
			day = cal.get(cal.DATE)+"";
		}
		result=cal.get(Calendar.YEAR)+"-"+month+"-"+day;
		return result;
	}
	

}
