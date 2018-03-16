package com.xidian.xienong.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Driver implements Serializable ,Comparable<Driver>{
	private String driver_id;
	private String driver_name;
	private String driver_sex;
	private String driver_telephone;
	private boolean isUsed;
	private String driver_identification; //身份证号
	private String driver_license_date;
	private String photoUrl;

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getDriver_telephone() {
		return driver_telephone;
	}
	public void setDriver_telephone(String driver_telephone) {
		this.driver_telephone = driver_telephone;
	}
	public String getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	public String getDriver_identification() {
		return driver_identification;
	}
	public void setDriver_identification(String driver_identification) {
		this.driver_identification = driver_identification;
	}
	
	public String getDriver_sex() {
		return driver_sex;
	}
	public void setDriver_sex(String driver_sex) {
		this.driver_sex = driver_sex;
	}
	public String getDriver_license_date() {
		return driver_license_date;
	}
	public void setDriver_license_date(String driver_license_date) {
		this.driver_license_date = driver_license_date;
	}


	@Override
	public int compareTo(@NonNull Driver o) {
		if(Integer.parseInt(this.driver_id) < Integer.parseInt(o.driver_id)){
			return 1;
		}else if(Integer.parseInt(this.driver_id) == Integer.parseInt(o.driver_id)){
			return 0;
		}else{
			return -1;
		}

	}
}
