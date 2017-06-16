package com.xidian.xienong.model;

import java.io.Serializable;


public class ImageVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer picLocId;
	private Integer picSerId;
	private Integer albumLocId;
	private Integer albumSerId;
	private String picName;
	private String picDate;

	private String picExplain="";
	private double picSize;
	
	private int progress = 0;
	
	private String imagePath,imageName,remarks;
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public double getPicSize() {
		return picSize;
	}
	public void setPicSize(double picSize) {
		this.picSize = picSize;
	}
	private String picLocUrl=null;
	private String picSerUrl;
	public Integer getPicLocId() {
		return picLocId;
	}
	public void setPicLocId(Integer picLocId) {
		this.picLocId = picLocId;
	}
	public Integer getPicSerId() {
		return picSerId;
	}
	public void setPicSerId(Integer picSerId) {
		this.picSerId = picSerId;
	}
	public Integer getAlbumLocId() {
		return albumLocId;
	}
	public void setAlbumLocId(Integer albumLocId) {
		this.albumLocId = albumLocId;
	}
	public Integer getAlbumSerId() {
		return albumSerId;
	}
	public void setAlbumSerId(Integer albumSerId) {
		this.albumSerId = albumSerId;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getPicDate() {
		return picDate;
	}
	public void setPicDate(String picDate) {
		this.picDate = picDate;
	}
	public String getPicExplain() {
		return picExplain;
	}
	public void setPicExplain(String picExplain) {
		this.picExplain = picExplain;
	}
	public String getPicLocUrl() {
		return picLocUrl;
	}
	public void setPicLocUrl(String picLocUrl) {
		this.picLocUrl = picLocUrl;
	}
	public String getPicSerUrl() {
		return picSerUrl;
	}
	public void setPicSerUrl(String picSerUrl) {
		this.picSerUrl = picSerUrl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
